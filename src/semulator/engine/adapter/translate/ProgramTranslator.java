package semulator.engine.adapter.translate;


import semulator.engine.adapter.xml.gen.SInstruction;
import semulator.engine.adapter.xml.gen.SProgram;
import semulator.engine.adapter.xml.gen.SInstructionArgument;
import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.*;
import semulator.engine.logic.instruction.basic.DecreaseInstruction;
import semulator.engine.logic.instruction.basic.IncreaseInstruction;
import semulator.engine.logic.instruction.basic.JumpNotZeroInstruction;
import semulator.engine.logic.instruction.basic.NoOpInstruction;
import semulator.engine.logic.instruction.synthetic.*;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.label.LabelImpl;
import semulator.engine.logic.program.Program;
import semulator.engine.logic.program.ProgramImpl;
import semulator.engine.logic.variable.Variable;
import semulator.engine.logic.variable.VariableImpl;
import semulator.engine.logic.variable.VariableType;

import java.util.*;
import java.util.stream.Collectors;


public final class ProgramTranslator {

    private ProgramTranslator() {}

        public static final class Result {
            public final Program program;
            public final List<String> errors;

            Result(Program program, List<String> errors) {
                this.program = program;
                this.errors = errors;
            }
        }

        public static Result translate(SProgram sProgram) {

            List<String> errors = new ArrayList<>();
            String programName = safe(sProgram.getName(), "Unnamed Program");

            Map<String, Variable> varsByName = new LinkedHashMap<>();
            Map<String, Label> labelsByName = new LinkedHashMap<>();
            List<Instruction> code = new ArrayList<>();


            List<SInstruction> sInstructions = Optional.ofNullable(sProgram.getInstructions()).orElse(List.of());

            for (int idx = 0; idx < sInstructions.size(); idx++) {
                SInstruction si = sInstructions.get(idx);

                Label lineLabel = null;
                String lineLabelName = trimToNull(si.getLabel());
                if (lineLabelName != null) {
                    lineLabel = labelsByName.computeIfAbsent(lineLabelName, LabelImpl::new);
                }

                Variable variable = null;
                String variableName = trimToNull(si.getVariable());
                if (variableName != null) {
                    variable = varsByName.computeIfAbsent(variableName, ProgramTranslator::newVarStrict);
                }


                String type = safe(si.getType(), "basic");
                String name = safe(si.getName(), "NEUTRAL");

                //get all the variables from arguments
                Map<String,String> args = argumentsMap(si.getArguments());

                Instruction instruction = null;

                if (type.equalsIgnoreCase("basic")) {
                    switch (name) {
                        case "INCREASE" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "INCREASE missing variable"));
                                instruction = new NoOpInstruction(newVarStrict("y1"), lineLabel, nn(args));
                            } else {
                                instruction = new IncreaseInstruction(variable, lineLabel, nn(args));
                            }
                        }
                        case "DECREASE" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "DECREASE missing variable"));
                                instruction = new NoOpInstruction(variable, lineLabel, nn(args));

                            } else {
                                instruction = new DecreaseInstruction(variable,lineLabel, nn(args));
                            }
                        }
                        case "JUMP_NOT_ZERO" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "JUMP_NOT_ZERO missing variable"));
                                instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                            } else {
                                String targetLabelName = args.getOrDefault("JNZLabel", "");
                                if (targetLabelName.isBlank()) {
                                    errors.add(errorMessageByLine(idx, "JUMP_NOT_ZERO missing JNZLabel argument"));
                                    instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                } else {
                                    Label targetLabel;
                                    targetLabel = labelsByName.computeIfAbsent(targetLabelName, LabelImpl::new);
                                    instruction = new JumpNotZeroInstruction(variable, targetLabel,lineLabel, nn(args));
                                }
                            }
                        }
                        case "NEUTRAL" -> {

                            instruction = new NoOpInstruction(variable != null ? variable : newVarStrict("y"),lineLabel, nn(args));
                        }
                        default -> {
                            errors.add(errorMessageByLine(idx, "Unknown basic instruction: " + name));
                            instruction = new NoOpInstruction(variable != null ? variable : newVarStrict("y"),lineLabel, nn(args));
                        }
                    }
                } else {
                    // synthetic instructions
                    switch (name) {

                        case "ASSIGNMENT" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "ASSIGNMENT missing target variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"), lineLabel, nn(args));
                            } else {
                                String otherVarName = args.getOrDefault("assignedVariable", "");
                                if (otherVarName.isBlank()) {
                                    errors.add(errorMessageByLine(idx, "ASSIGNMENT missing 'assignedVariable'"));
                                    instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                } else {
                                    try {
                                        Variable otherVar = varsByName.computeIfAbsent(otherVarName, ProgramTranslator::parseStrictVariable);
                                        instruction = new AssignmentInstruction(variable, otherVar, lineLabel, nn(args));
                                    }
                                    catch (IllegalArgumentException ex) {
                                        errors.add(errorMessageByLine(idx, "ASSIGNMENT bad 'otherVariable': " + otherVarName));
                                        instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                        break;
                                    }
                                }
                            }
                        }

                        case "CONSTANT_ASSIGNMENT" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "CONSTANT_ASSIGNMENT missing target variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"), lineLabel, nn(args));
                            } else {
                                String valStr = args.getOrDefault("constantValue", "");
                                try {
                                    long value = Long.parseLong(valStr);
                                    instruction = new ConstantAssignmentInstruction(variable, value, lineLabel, nn(args));
                                } catch (NumberFormatException ex) {
                                    errors.add(errorMessageByLine(idx,
                                            "CONSTANT_ASSIGNMENT bad 'constantValue': " + valStr));
                                    instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                }
                            }
                        }

                        case "ZERO_VARIABLE" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "ZERO_VARIABLE missing variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"), lineLabel, nn(args));
                            } else {
                                instruction = new ZeroVariableInstruction(variable, lineLabel, nn(args));
                            }
                        }

                        case "GOTO_LABEL" -> {
                            String toName = args.getOrDefault("gotoLabel", "");
                            if (toName.isBlank()) {
                                errors.add(errorMessageByLine(idx, "GOTO_LABEL missing 'gotoLabel'"));
                                instruction = new NoOpInstruction(variable != null ? variable : newVarStrict("y"), lineLabel, nn(args));
                            } else {
                                Label to = labelsByName.computeIfAbsent(toName, LabelImpl::new);
                                instruction = new GoToInstruction(variable != null ? variable : newVarStrict("y"), to, lineLabel, nn(args));
                            }
                        }

                        case "JUMP_ZERO" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "JUMP_ZERO missing variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"), lineLabel, nn(args));
                            } else {
                                String targetLabelName = args.getOrDefault("JZLabel", "");
                                if (targetLabelName.isBlank()) {
                                    errors.add(errorMessageByLine(idx, "JUMP_ZERO missing 'JZLabel'"));
                                    instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                } else {
                                    Label target = labelsByName.computeIfAbsent(targetLabelName, LabelImpl::new);
                                    instruction = new JumpZeroInstruction(variable, target, lineLabel, nn(args));
                                }
                            }
                        }

                        case "JUMP_EQUAL_CONSTANT" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "JUMP_EQUAL_CONSTANT missing variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"), lineLabel, nn(args));
                            } else {
                                String targetLabelName = args.getOrDefault("JEConstantLabel", "");
                                String constStr        = args.getOrDefault("constantValue", "");
                                if (targetLabelName.isBlank()) {
                                    errors.add(errorMessageByLine(idx, "JUMP_EQUAL_CONSTANT missing 'JEQLabel'"));
                                    instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                } else {
                                    try {
                                        long constant = Long.parseLong(constStr);
                                        Label target = labelsByName.computeIfAbsent(targetLabelName, LabelImpl::new);
                                        instruction = new JumpEqualConstantInstruction(variable, target, constant, lineLabel, nn(args));
                                    } catch (NumberFormatException ex) {
                                        errors.add(errorMessageByLine(idx,
                                                "JUMP_EQUAL_CONSTANT bad 'constantValue': " + constStr));
                                        instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                    }
                                }
                            }
                        }

                        case "JUMP_EQUAL_VARIABLE" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "JUMP_EQUAL_VARIABLE missing variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"), lineLabel, nn(args));
                            } else {
                                String targetLabelName = args.getOrDefault("JEVariableLabel", "");
                                String otherVarName    = args.getOrDefault("variableName", "");
                                if (targetLabelName.isBlank()) {
                                    errors.add(errorMessageByLine(idx, "JUMP_EQUAL_VARIABLE missing 'JEQLabel'"));
                                    instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                } else if (otherVarName.isBlank()) {
                                    errors.add(errorMessageByLine(idx, "JUMP_EQUAL_VARIABLE missing 'otherVariable'"));
                                    instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                } else {
                                    Label target  = labelsByName.computeIfAbsent(targetLabelName, LabelImpl::new);
                                    try {
                                        Variable other = varsByName.computeIfAbsent(otherVarName, ProgramTranslator::parseStrictVariable);
                                        instruction = new JumpEqualVariableInstruction(variable, target, other, lineLabel, nn(args));
                                    }
                                    catch (IllegalArgumentException ex) {
                                        errors.add(errorMessageByLine(idx, "JUMP_EQUAL_VARIABLE bad 'otherVariable': " + otherVarName));
                                        instruction = new NoOpInstruction(variable, lineLabel, nn(args));
                                        break;
                                    }
                                }
                            }
                        }

                        default -> {
                            errors.add(errorMessageByLine(idx, "Unknown synthetic instruction: " + name));
                            instruction = new NoOpInstruction(
                                    (variable != null ? variable : newVarStrict("y")),
                                    lineLabel,
                                    nn(args)
                            );
                        }
                    }

                }

                //attachLabelIfSupported(instruction, lineLabel);
                code.add(instruction);
            }


            ProgramImpl program = new ProgramImpl.Builder()
                    .withName(programName)
                    .withInstructions(code)
                    .withVariables(varsByName.values())
                    .withLabels(labelsByName.values())
                    .withExitLabel(labelsByName.get("EXIT"))
                    .build();

            return new Result(program, errors);
        }






    private static Variable parseStrictVariable(String name) {

        char c0 = Character.toLowerCase(name.charAt(0));
        switch (c0) {
            case 'y':
                if (name.length() == 1) {
                    return Variable.RESULT;
                }

            case 'x': {
                int idx = parseStrictIndex(name, 1, name); // דורש ספרות בלבד אחרי X
                return new VariableImpl(VariableType.INPUT, idx);
            }

            case 'z': {
                int idx = parseStrictIndex(name, 1, name); // דורש ספרות בלבד אחרי Z
                return new VariableImpl(VariableType.WORK, idx); // אם יש אצלך WORK/INTERMEDIATE – תעדכן כאן
            }

            default:
                throw new IllegalArgumentException(
                        "Unsupported variable '" + name + "'. Only xN, zN, or y are allowed");

        }
    }




    private static int parseStrictIndex(String s, int start, String original) {
        if (s.length() <= start) {
            throw new IllegalArgumentException("Missing index for variable '" + original + "'");
        }
        int i = start;
        while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
        if (i != s.length()) {
            throw new IllegalArgumentException("Invalid characters after index in '" + original + "'");
        }
        try {
            int idx = Integer.parseInt(s.substring(start, i));
             if (idx < 1) throw new IllegalArgumentException("Index must be >= 1 in '" + original + "'");
            return idx;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric index for variable '" + original + "'");
        }
    }






    private static String safe(String s, String def) { return (s == null || s.isBlank()) ? def : s; }

        private static String trimToNull(String s) {
            return (s == null) ? null : (s.trim().isEmpty() ? null : s.trim());
        }

        private static String errorMessageByLine(int line, String msg) {
            return String.format("Error in line %d: %s", line, msg);
        }

        private static Map<String,String> argumentsMap(List<SInstructionArgument> arguments) {
            if (arguments == null) {
                return Map.of();
            }
            return arguments.stream()
                    .collect(Collectors.toUnmodifiableMap(
                            SInstructionArgument::getName,
                            a -> safe(a.getValue(), "")
                    ));
        }

        private static Variable newVarStrict(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Variable name cannot be null");
            }
            VariableType variableType;

            if (name.equals("y")) {
                variableType = VariableType.RESULT;
                return new VariableImpl(variableType, 1);

            }
            else if (name.startsWith("x")) {
                variableType = VariableType.INPUT;
                try {
                    int number = Integer.parseInt(name.substring(1));
                    return new VariableImpl(variableType, number);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Variable suffix must be a number");
                }            }
            else {
                variableType = VariableType.WORK;
                try {
                    int number = Integer.parseInt(name.substring(1));
                    return new VariableImpl(variableType, number);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Variable suffix must be a number");
                }
            }


        }

        private static void attachLabelIfSupported(Instruction instruction, Label label) {
            if (label == null) return;
            try {
                var setLabelMethod = instruction.getClass().getMethod("setLabel", Label.class);
                setLabelMethod.invoke(instruction, label);
            } catch (ReflectiveOperationException ignored) {}
        }

        private static Map<String, String> nn(Map<String, String> m) {
        return (m == null) ? Map.of() : m;
    }


}

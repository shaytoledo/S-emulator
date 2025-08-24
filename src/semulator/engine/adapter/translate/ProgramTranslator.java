package semulator.engine.adapter.translate;


import semulator.engine.adapter.xml.gen.SInstruction;
import semulator.engine.adapter.xml.gen.SProgram;
import semulator.engine.adapter.xml.gen.SInstructionArgument;
import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.*;
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


                // need to decide what to do what the type
                String type = safe(si.getType(), "basic");
                String name = safe(si.getName(), "NEUTRAL");
                Map<String,String> args = argumentsMap(si.getArguments());

                Instruction instruction = null;

                if (type.equalsIgnoreCase("basic")) {
                    switch (name) {
                        case "INCREASE" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "INCREASE missing variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"));
                            } else {
                                instruction = new IncreaseInstruction(variable);
                            }
                        }
                        case "DECREASE" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "DECREASE missing variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"));
                            } else {
                                instruction = new DecreaseInstruction(variable);
                            }
                        }
                        case "JUMP_NOT_ZERO" -> {
                            if (variable == null) {
                                errors.add(errorMessageByLine(idx, "JUMP_NOT_ZERO missing variable"));
                                instruction = new NoOpInstruction(newVarStrict("y"));
                            } else {
                                String targetLabelName = args.getOrDefault("JNZLabel", "");
                                if (targetLabelName.isBlank()) {
                                    errors.add(errorMessageByLine(idx, "JUMP_NOT_ZERO missing JNZLabel argument"));
                                    instruction = new NoOpInstruction(variable);
                                } else {
                                    Label targetLabel;
                                    if (targetLabelName.equals("EXIT")) {
                                        targetLabel = labelsByName.computeIfAbsent("EXIT", LabelImpl::new);
                                    }
                                    else {
                                        targetLabel = labelsByName.computeIfAbsent(targetLabelName, LabelImpl::new);
                                        instruction = new JumpNotZeroInstruction(variable, targetLabel);
                                    }
                                }
                            }
                        }
                        case "NEUTRAL" -> {
                            instruction = new NoOpInstruction(variable != null ? variable : newVarStrict("y"));
                        }
                        default -> {
                            errors.add(errorMessageByLine(idx, "Unknown basic instruction: " + name));
                            instruction = new NoOpInstruction(variable != null ? variable : newVarStrict("y"));
                        }
                    }
                } else {
                    instruction = new SyntheticPlaceholder(name, variable != null ? variable : newVarStrict("y"), args);
                }

                attachLabelIfSupported(instruction, lineLabel);

                code.add(instruction);
            }

            for (int idx = 0; idx < sInstructions.size(); idx++) {
                SInstruction si = sInstructions.get(idx);
                Map<String,String> argsMap = argumentsMap(si.getArguments());
                String name = safe(si.getName(), "");
                switch (name) {
                    case "GOTO_LABEL" -> {
                        String targetLabelName = argsMap.getOrDefault("gotoLabel", "");
                        if (!targetLabelName.isBlank() && !targetLabelName.equals("EXIT") && !labelsByName.containsKey(targetLabelName)) {
                            errors.add(errorMessageByLine(idx, "Unknown goto label: " + targetLabelName));
                        }
                    }
                    case "JUMP_ZERO" -> {
                        String targetLabelName = argsMap.getOrDefault("JZLabel", "");
                        if (!targetLabelName.isBlank() && !targetLabelName.equals("EXIT") && !labelsByName.containsKey(targetLabelName)) {
                            errors.add(errorMessageByLine(idx, "Unknown JZ label: " + targetLabelName));
                        }
                    }
                    case "JUMP_EQUAL_CONSTANT" -> {
                        String targetLabelName = argsMap.getOrDefault("JEConstantLabel", "");
                        if (!targetLabelName.isBlank() && !targetLabelName.equals("EXIT") && !labelsByName.containsKey(targetLabelName)) {
                            errors.add(errorMessageByLine(idx, "Unknown JEConstantLabel label: " + targetLabelName));
                        }

                        String constantValue = argsMap.get("constantValue");
                        if (constantValue == null || constantValue.isBlank()) {
                            errors.add(errorMessageByLine(idx, "Missing constantValue for JUMP_EQUAL_CONSTANT"));
                        } else {
                            try {
                                Integer.parseInt(constantValue);
                            } catch (NumberFormatException e) {
                                errors.add(errorMessageByLine(idx, "Invalid constantValue: " + constantValue));
                            }
                        }
                    }
                    case "JUMP_EQUAL_VARIABLE" -> {
                        String targetLabelName = argsMap.getOrDefault("JEVariableLabel", "");
                        if (!targetLabelName.isBlank() && !targetLabelName.equals("EXIT") && !labelsByName.containsKey(targetLabelName)) {
                            errors.add(errorMessageByLine(idx, "Unknown JEVariableLabel label: " + targetLabelName));
                        }

                        String cmpVariableName = argsMap.get("JEVariable");
                        if (cmpVariableName == null || cmpVariableName.isBlank()) {
                            errors.add(errorMessageByLine(idx, "Missing JEVariable for JUMP_EQUAL_VARIABLE"));
                        } else {
                            varsByName.computeIfAbsent(cmpVariableName, ProgramTranslator::newVarStrict);
                        }
                    }
                    default -> {}
                }
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

        public static final class SyntheticPlaceholder implements Instruction {
            private final String name;
            private final Variable variable;
            private final Map<String,String> argsMap;
            private Label label;

            public SyntheticPlaceholder(String name, Variable variable, Map<String,String> argsMap) {
                this.name = name;
                this.variable = variable;
                this.argsMap = Map.copyOf(argsMap);
            }

            @Override
            public String getName() { return name; }

            @Override
            public Variable getVariable() { return variable; }

            @Override
            public Label execute(ExecutionContext context) {
                throw new IllegalStateException(
                        "Synthetic instruction '" + name + "' must be expanded before execution"
                );
            }


            //get cycles??
            @Override
            public int cycles() {
                return 1;
            }

            public boolean isBasic() { return false; }

            public  int getCycles() { return 1; }
            public Label getLabel() { return label; }
            public void setLabel(Label label) { this.label = label; }

            public String toDisplayString() {
                return switch (name) {
                    case "ASSIGNMENT" -> variable.getRepresentation() + " <- " + argsMap.getOrDefault("assignedVariable","?");
                    case "ZERO_VARIABLE" -> variable.getRepresentation() + " <- 0";
                    case "CONSTANT_ASSIGNMENT" -> variable.getRepresentation() + " <- " + argsMap.getOrDefault("constantValue","?");
                    case "GOTO_LABEL" -> "GOTO " + argsMap.getOrDefault("gotoLabel","?");
                    case "JUMP_ZERO" -> "JZ " + variable.getRepresentation() + " -> " + argsMap.getOrDefault("JZLabel","?");
                    case "JUMP_EQUAL_CONSTANT" ->
                            "JE " + variable.getRepresentation() + " == " + argsMap.getOrDefault("constantValue","?") +
                                    " -> " + argsMap.getOrDefault("JEConstantLabel","?");
                    case "JUMP_EQUAL_VARIABLE" ->
                            "JE " + variable.getRepresentation() + " == " + argsMap.getOrDefault("JEVariable","?") +
                                    " -> " + argsMap.getOrDefault("JEVariableLabel","?");
                    default -> name + "(" + (variable != null ? variable.getRepresentation() : "") + ")";
                };
            }


        }

    }

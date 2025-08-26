package semulator.engine.logic.execution;

import semulator.engine.logic.instruction.Instruction;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.program.Program;
import semulator.engine.logic.variable.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramExecutorImpl implements ProgramExecutor{

    private final Program program;
    private ExecutionContext context;


    public ProgramExecutorImpl(Program program) {
        this.program = program;
    }

    @Override
    public long run(List<Long> inputs) {

        final List<Long> safeInputs;
        if (inputs == null || inputs.isEmpty()) {
            safeInputs = java.util.Collections.emptyList();
        } else {
            safeInputs = new java.util.ArrayList<>(inputs.size());
            for (Long v : inputs) {
                safeInputs.add(v != null ? v : 0L);
            }
        }


        context = new EexecutionContextImpl(safeInputs); // create the context with inputs.

        Instruction currentInstruction = program.getInstructions().isEmpty()
                ? null
                : program.getInstructions().get(0);

        if (currentInstruction == null) {
            return context.getVariableValue(Variable.RESULT);
        }
//        Label nextLabel;
//        do {
//            nextLabel = currentInstruction.execute(context);
//
//            if (nextLabel == FixedLabel.EMPTY) {
//                currentInstruction = program.getNextInstructionLabel(currentInstruction);
//                if(currentInstruction == null) {
//                    nextLabel = FixedLabel.EXIT; // if there is no next instruction, exit
//                }
//                // set currentInstruction to the next instruction in line
//            } else if (nextLabel.getLabelRepresentation() != FixedLabel.EXIT.getLabelRepresentation()) {
//                // need to find the instruction at 'nextLabel' and set current instruction to it
//                currentInstruction = program.getInstructionByLabel(nextLabel);
//            }
//        } while (nextLabel != FixedLabel.EXIT);
//
//        return context.getVariableValue(Variable.RESULT);






        Label nextLabel;
        do {
            nextLabel = currentInstruction.execute(context);

            if (isEmpty(nextLabel)) {
                currentInstruction = program.getNextInstructionLabel(currentInstruction);
                if (currentInstruction == null) {
                    nextLabel = FixedLabel.EXIT; // אין עוד הוראה
                }
            } else if (!isExit(nextLabel)) {
                currentInstruction = program.getInstructionByLabel(nextLabel);
//                if (currentInstruction == null) {
//                    throw new IllegalStateException("Jump target label not found: " + labelToString(nextLabel));
//                }
            }
        } while (!isExit(nextLabel));


               return context.getVariableValue(Variable.RESULT);



    }






    private static boolean isExit(Label l) {
        if (l == null) return false;
        if (l == FixedLabel.EXIT) return true;
        String rep = l.getLabelRepresentation();
        return rep != null && rep.equalsIgnoreCase("EXIT");
    }

    private static boolean isEmpty(Label l) {
        if (l == null) return false;
        if (l == FixedLabel.EMPTY) return true;
        String rep = l.getLabelRepresentation();
        return rep != null && rep.equalsIgnoreCase("EMPTY");
    }







    @Override
    public Map<String, Long> variablesState() {
//        return result;Map<Variable, Long> state = context.getVariablesState();
////        Map<String, Long> result = new HashMap<>();
////
////        for (Map.Entry<Variable, Long> entry : state.entrySet()) {
////            String varName = entry.getKey().getRepresentation();
////            result.put(varName, entry.getValue());
////        }
        return context.getVariablesState();
////
    }

    @Override
    public long getTotalCycles() {
        return program.calculateCycles();
    }
}

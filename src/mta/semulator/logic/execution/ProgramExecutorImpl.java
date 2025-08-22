package mta.semulator.logic.execution;

import mta.semulator.logic.instruction.SInstruction;
import mta.semulator.logic.label.FixedLabel;
import mta.semulator.logic.label.Label;
import mta.semulator.logic.program.SProgram;
import mta.semulator.logic.variable.Variable;

import java.util.Map;

public class ProgramExecutorImpl implements ProgramExecutor{

    private final SProgram program;

    public ProgramExecutorImpl(SProgram program) {
        this.program = program;
    }

    @Override
    public long run(Long... inputs) {

        ExecutionContext context = new EexecutionContextImpl(inputs); // create the context with inputs.

        SInstruction currentInstruction = program.getInstructions().get(0);
        Label nextLabel;
        do {
            nextLabel = currentInstruction.execute(context);

            if (nextLabel == FixedLabel.EMPTY) {
                currentInstruction = program.getNextInstructionLabel(currentInstruction);
                if(currentInstruction == null) {
                    nextLabel = FixedLabel.EXIT; // if there is no next instruction, exit
                }
                // set currentInstruction to the next instruction in line
            } else if (nextLabel != FixedLabel.EXIT) {
                // need to find the instruction at 'nextLabel' and set current instruction to it
                currentInstruction = program.getInstructionByLabel(nextLabel);
            }
        } while (nextLabel != FixedLabel.EXIT);

        return context.getVariableValue(Variable.RESULT);
    }

    @Override
    public Map<Variable, Long> variableState() {
        return Map.of();
    }
}

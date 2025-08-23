package semulator.engine.logic.execution;

import semulator.engine.logic.instruction.SInstruction;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.program.Program;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class ProgramExecutorImpl implements ProgramExecutor{

    private final Program program;

    public ProgramExecutorImpl(Program program) {
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

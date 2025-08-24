package semulator.engine.logic.instruction.basic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

public class NoOpInstruction extends AbstractInstruction {

    public NoOpInstruction(Variable variable) {
        super(InstructionData.NO_OP, variable);
    }

    public NoOpInstruction(Variable variable, Label label) {
        super(InstructionData.NO_OP, variable, label);
    }

    @Override
    public Label execute(ExecutionContext context) {
        return FixedLabel.EMPTY;

    }
}

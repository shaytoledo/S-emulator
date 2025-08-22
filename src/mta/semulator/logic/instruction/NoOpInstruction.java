package mta.semulator.logic.instruction;

import mta.semulator.logic.execution.ExecutionContext;
import mta.semulator.logic.label.FixedLabel;
import mta.semulator.logic.label.Label;
import mta.semulator.logic.variable.Variable;

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

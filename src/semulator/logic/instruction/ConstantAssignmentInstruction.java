package semulator.logic.instruction;

import semulator.logic.execution.ExecutionContext;
import semulator.logic.label.FixedLabel;
import semulator.logic.label.Label;
import semulator.logic.variable.Variable;

public class ConstantAssignmentInstruction extends AbstractInstruction {

    final Long constant;

    public ConstantAssignmentInstruction(Variable variable, Long value) {
        this(variable ,value, FixedLabel.EMPTY);
    }

    public ConstantAssignmentInstruction(Variable variable, Long value, Label label) {
        super(InstructionData.CONSTANT_ASSIGNMENT, variable, label);
        this.constant = value;
    }

    @Override
    public Label execute(ExecutionContext context) {
        context.updateVariable(getVariable(), constant);
        return FixedLabel.EMPTY;
    }
}

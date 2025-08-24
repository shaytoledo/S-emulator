package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

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

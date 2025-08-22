package mta.semulator.logic.instruction;

import mta.semulator.logic.execution.ExecutionContext;
import mta.semulator.logic.label.FixedLabel;
import mta.semulator.logic.label.Label;
import mta.semulator.logic.variable.Variable;

public class JumpNotZeroInstruction extends AbstractInstruction{

    private final Label jnzLabel;

    public JumpNotZeroInstruction(Variable variable, Label jnzLabel) {
        this(variable, jnzLabel, FixedLabel.EMPTY);
    }

    public JumpNotZeroInstruction(Variable variable, Label jnzLabel, Label label) {
        super(InstructionData.JUMP_NOT_ZERO, variable, label);
        this.jnzLabel = jnzLabel;
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue != 0) {
            return jnzLabel;
        }
        return FixedLabel.EMPTY;

    }
}

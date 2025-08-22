package semulator.logic.instruction;

import semulator.logic.execution.ExecutionContext;
import semulator.logic.label.FixedLabel;
import semulator.logic.label.Label;
import semulator.logic.variable.Variable;

public class JunpZeroInstruction extends AbstractInstruction {

    private final Label jnzLabel;

    public JunpZeroInstruction(Variable variable, Label jnzLabel) {
        this(variable, jnzLabel, FixedLabel.EMPTY);
    }

    public JunpZeroInstruction(Variable variable, Label jnzLabel, Label label) {
        super(InstructionData.JUMP_ZERO, variable, label);
        this.jnzLabel = jnzLabel;
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue == 0) {
            return jnzLabel;
        }
        return FixedLabel.EMPTY;

    }
}

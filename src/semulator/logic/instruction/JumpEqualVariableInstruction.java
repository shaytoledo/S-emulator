package semulator.logic.instruction;

import semulator.logic.execution.ExecutionContext;
import semulator.logic.label.FixedLabel;
import semulator.logic.label.Label;
import semulator.logic.variable.Variable;

public class JumpEqualVariableInstruction extends AbstractInstruction {

    Label jnzLabel;
    Variable other;

    public JumpEqualVariableInstruction(Variable variable, Label jnzLabel, Variable other) {
        this(variable,jnzLabel, other, FixedLabel.EMPTY);
    }

    public JumpEqualVariableInstruction(Variable variable, Label jnzLabel, Variable other, Label label) {
        super(InstructionData.JUMP_EQUAL_VARIABLE, variable, label);
        this.jnzLabel = jnzLabel;
        this.other = other;

    }

    @Override
    public Label execute(ExecutionContext context) {
        if (context.getVariableValue(getVariable()) == context.getVariableValue(other)) {
            return jnzLabel;
        }
        return FixedLabel.EMPTY;
    }
}

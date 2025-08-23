package semulator.engine.logic.instruction;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

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

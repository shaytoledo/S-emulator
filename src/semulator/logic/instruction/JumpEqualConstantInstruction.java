package semulator.logic.instruction;

import semulator.logic.execution.ExecutionContext;
import semulator.logic.label.FixedLabel;
import semulator.logic.label.Label;
import semulator.logic.variable.Variable;

public class JumpEqualConstantInstruction extends AbstractInstruction {

    Long constant;
    Label jnzLabel;

    public JumpEqualConstantInstruction(Variable variable, Label jnzLabel, Long constant) {
        this(variable,jnzLabel, constant, FixedLabel.EMPTY);
    }

    public JumpEqualConstantInstruction(Variable variable, Label jnzLabel, Long constant, Label label) {
        super(InstructionData.JUMP_EQUAL_CONSTANT, variable, label);
        this.constant = constant;
        this.jnzLabel = jnzLabel;

    }

    @Override
    public Label execute(ExecutionContext context) {
        if (context.getVariableValue(getVariable()) == constant) {;
            return jnzLabel;
        }
        return FixedLabel.EMPTY;
    }
}

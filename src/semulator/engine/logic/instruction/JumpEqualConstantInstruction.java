package semulator.engine.logic.instruction;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

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

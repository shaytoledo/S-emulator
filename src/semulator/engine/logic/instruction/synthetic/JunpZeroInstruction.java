package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

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

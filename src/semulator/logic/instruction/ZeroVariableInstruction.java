package semulator.logic.instruction;

import semulator.logic.execution.ExecutionContext;
import semulator.logic.label.FixedLabel;
import semulator.logic.label.Label;
import semulator.logic.variable.Variable;

public class ZeroVariableInstruction extends AbstractInstruction{

public ZeroVariableInstruction(Variable variable) {
    super(InstructionData.ZERO_VARIABLE, variable);
}

    public ZeroVariableInstruction(Variable variable, Label label) {
        super(InstructionData.INCREASE, variable, label);
    }

    @Override
    public Label execute(ExecutionContext context) {
        context.updateVariable(getVariable(), 0L);
        return FixedLabel.EMPTY;
    }
}

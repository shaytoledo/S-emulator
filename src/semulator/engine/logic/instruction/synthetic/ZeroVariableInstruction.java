package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

public class ZeroVariableInstruction extends AbstractInstruction {

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

package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class ZeroVariableInstruction extends AbstractInstruction {

    public ZeroVariableInstruction(Variable var, Map<String,String> argsMap) {
        super(InstructionData.ZERO_VARIABLE, var, argsMap);
    }

    public ZeroVariableInstruction(Variable var, Label label, Map<String,String> argsMap) {
        super(InstructionData.ZERO_VARIABLE, var, label, argsMap);
    }
    @Override
    public Label execute(ExecutionContext context) {
        context.updateVariable(getVariable(), 0L);
        return FixedLabel.EMPTY;
    }


    @Override
    public String toDisplayString() {
        return getVariable().getRepresentation() + " <- 0";
    }
}

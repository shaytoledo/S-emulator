package semulator.engine.logic.instruction.basic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class DecreaseInstruction extends AbstractInstruction {

    public DecreaseInstruction(Variable var, Label lineLabel, Map<String,String> argsMap) {
        super(InstructionData.DECREASE, var, lineLabel, argsMap);
        basic = true;
    }

    public DecreaseInstruction(Variable var, Map<String,String> argsMap) {
        super(InstructionData.DECREASE, var, argsMap);
        basic = true;
    }


    @Override
    public Label execute(ExecutionContext context) {

        long variableValue = context.getVariableValue(getVariable());
        variableValue = Math.max(0, variableValue - 1);
        context.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }

    @Override
    public String toDisplayString() {
        return getVariable().getRepresentation() + " <- " + getVariable().getRepresentation() + " - 1";
    }

}
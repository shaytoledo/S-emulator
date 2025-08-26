package semulator.engine.logic.instruction.basic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class NoOpInstruction extends AbstractInstruction {

    public NoOpInstruction(Variable var, Label lineLabel, Map<String,String> argsMap) {
        super(InstructionData.NO_OP, var, lineLabel, argsMap);
        basic = true;
    }

    public NoOpInstruction(Variable var, Map<String,String> argsMap) {
        super(InstructionData.NO_OP, var, argsMap);
        basic = true;
    }


    @Override
    public Label execute(ExecutionContext context) {
        return FixedLabel.EMPTY;
    }

    @Override
    public String toDisplayString() {
        return getVariable().getRepresentation() + " <- " + getVariable().getRepresentation();
    }
}

package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class GoToInstruction extends AbstractInstruction {

    private final Label target;

    public GoToInstruction(Variable var, Label target, Map<String,String> argsMap) {
        super(InstructionData.GOTO_LABEL, var, argsMap);
        this.target = target;
    }

    public GoToInstruction(Variable var, Label target, Label lineLabel, Map<String,String> argsMap) {
        super(InstructionData.GOTO_LABEL, var, lineLabel, argsMap);
        this.target = target;
    }

    @Override
    public Label execute(ExecutionContext context) {
        return target;
    }


    @Override
    public String toDisplayString() {
        return  "GOTO " + argsMap.getOrDefault("gotoLabel","?");
    }
}

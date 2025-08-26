package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class JumpEqualConstantInstruction extends AbstractInstruction {

    private final Label target;
    private final long constant;

    public JumpEqualConstantInstruction(Variable var, Label target, long constant, Map<String,String> argsMap) {
        super(InstructionData.JUMP_EQUAL_CONSTANT, var, argsMap);
        this.target = target;
        this.constant = constant;
    }

    public JumpEqualConstantInstruction(Variable var, Label target, long constant, Label lineLabel, Map<String,String> argsMap) {
        super(InstructionData.JUMP_EQUAL_CONSTANT, var, lineLabel, argsMap);
        this.target = target;
        this.constant = constant;
    }


    @Override
    public Label execute(ExecutionContext context) {
        if (context.getVariableValue(getVariable()) == constant) {;
            return target;
        }
        return FixedLabel.EMPTY;
    }


    @Override
    public String toDisplayString() {
        return "JE " + getVariable().getRepresentation() + " == " + argsMap.getOrDefault("constantValue","?") +" -> " + argsMap.getOrDefault("JEConstantLabel","?");
    }
}

package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class JumpEqualVariableInstruction extends AbstractInstruction {

    Label jnzLabel;
    Variable other;

    public JumpEqualVariableInstruction(Variable var, Label target, Variable other, Map<String,String> argsMap) {
        super(InstructionData.JUMP_EQUAL_VARIABLE, var, argsMap);
        this.jnzLabel = target;
        this.other = other;
    }

    public JumpEqualVariableInstruction(Variable var, Label target, Variable other, Label lineLabel, Map<String,String> argsMap) {
        super(InstructionData.JUMP_EQUAL_VARIABLE, var, lineLabel, argsMap);
        this.jnzLabel = target;
        this.other = other;
    }


    @Override
    public Label execute(ExecutionContext context) {
        if (context.getVariableValue(getVariable()) == context.getVariableValue(other)) {
            return jnzLabel;
        }
        return FixedLabel.EMPTY;
    }


    @Override
    public String toDisplayString() {
        return "JE " + getVariable().getRepresentation() + " == " + argsMap.getOrDefault("variableName","?") + " -> " + argsMap.getOrDefault("JEVariableLabel","?");
    }
}

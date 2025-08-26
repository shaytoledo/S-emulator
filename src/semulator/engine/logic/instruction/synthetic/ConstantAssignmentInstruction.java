package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class ConstantAssignmentInstruction extends AbstractInstruction {

    private final long constant;

    public ConstantAssignmentInstruction(Variable target, long constant, Map<String,String> argsMap) {
        super(InstructionData.CONSTANT_ASSIGNMENT, target, argsMap);
        this.constant = constant;
    }

    public ConstantAssignmentInstruction(Variable target, long constant, Label label, Map<String,String> argsMap) {
        super(InstructionData.CONSTANT_ASSIGNMENT, target, label, argsMap);
        this.constant = constant;
    }

    @Override
    public Label execute(ExecutionContext context) {
        context.updateVariable(getVariable(), constant);
        return FixedLabel.EMPTY;
    }


    @Override
    public String toDisplayString() {
        return getVariable().getRepresentation() + " <- " + argsMap.getOrDefault("constantValue","?");
    }
}

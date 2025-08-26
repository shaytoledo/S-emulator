package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public class AssignmentInstruction extends AbstractInstruction {

    Variable assignedVariable;

    public AssignmentInstruction(Variable target, Variable source, Map<String,String> argsMap) {
        super(InstructionData.ASSIGNMENT, target, argsMap);
        this.assignedVariable = source;
    }

    public AssignmentInstruction(Variable target, Variable source, Label label, Map<String,String> argsMap) {
        super(InstructionData.ASSIGNMENT, target, label, argsMap);
        this.assignedVariable = source;
    }


    @Override
    public Label execute(ExecutionContext context) {
        long assignedValue = context.getVariableValue(assignedVariable);
        context.updateVariable(getVariable(), assignedValue);
        return FixedLabel.EMPTY;
    }


    @Override
    public String toDisplayString() {
        //return getVariable().getRepresentation() + " <- " + argsMap.getOrDefault("assignedVariable","?");
        return getVariable().getRepresentation() + " <- " + assignedVariable.getRepresentation();

    }
}

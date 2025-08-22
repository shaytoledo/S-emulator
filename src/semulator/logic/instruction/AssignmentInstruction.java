package semulator.logic.instruction;

import semulator.logic.execution.ExecutionContext;
import semulator.logic.label.FixedLabel;
import semulator.logic.label.Label;
import semulator.logic.variable.Variable;

public class AssignmentInstruction extends AbstractInstruction {

    Variable assignedVariable;

    public AssignmentInstruction(Variable variable, Variable assignedVariable) {
        this(variable, assignedVariable, FixedLabel.EMPTY);
    }

    public AssignmentInstruction(Variable variable, Variable assignedVariable, Label label) {
        super(InstructionData.ASSIGNMENT, variable, label);
        this.assignedVariable = assignedVariable;
    }



    @Override
    public Label execute(ExecutionContext context) {
        long assignedValue = context.getVariableValue(assignedVariable);
        context.updateVariable(getVariable(), assignedValue);
        return null;
    }
}

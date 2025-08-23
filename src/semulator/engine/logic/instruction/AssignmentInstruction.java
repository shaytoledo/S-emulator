package semulator.engine.logic.instruction;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

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

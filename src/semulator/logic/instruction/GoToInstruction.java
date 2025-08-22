package semulator.logic.instruction;

import semulator.logic.execution.ExecutionContext;
import semulator.logic.label.FixedLabel;
import semulator.logic.label.Label;
import semulator.logic.variable.Variable;
import semulator.logic.variable.VariableImpl;
import semulator.logic.variable.VariableType;

public class GoToInstruction extends AbstractInstruction {

    private final Label goToLabel;

    public GoToInstruction(Variable variable, Label to) {
        this(variable, to, FixedLabel.EMPTY);
    }

    public GoToInstruction(Variable variable, Label to, Label label) {
        super(InstructionData.GOTO_LABEL, variable, label);
        this.goToLabel = to;
    }


    @Override
    public Label execute(ExecutionContext context) {
        return goToLabel;
    }
}

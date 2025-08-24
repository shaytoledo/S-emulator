package semulator.engine.logic.instruction.synthetic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

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

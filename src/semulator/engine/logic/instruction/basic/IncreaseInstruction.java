package semulator.engine.logic.instruction.basic;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.instruction.AbstractInstruction;
import semulator.engine.logic.instruction.InstructionData;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

public class IncreaseInstruction extends AbstractInstruction {

    public IncreaseInstruction(Variable variable) {
        super(InstructionData.INCREASE, variable);
    }

    public IncreaseInstruction(Variable variable, Label label) {
        super(InstructionData.INCREASE, variable, label);
    }

    @Override
    public Label execute(ExecutionContext context) {

        long variableValue = context.getVariableValue(getVariable());
        variableValue++;
        context.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }
}
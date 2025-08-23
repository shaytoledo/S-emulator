package semulator.engine.logic.instruction;

import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

public abstract class AbstractInstruction implements SInstruction {

    private final InstructionData instructionData;
    private final Label label;
    private final Variable variable;

    public AbstractInstruction(InstructionData instructionData, Variable variable) {
        this(instructionData, variable, FixedLabel.EMPTY);
    }

    public AbstractInstruction(InstructionData instructionData, Variable variable, Label label) {
        this.instructionData = instructionData;
        this.label = label;
        this.variable = variable;
    }

    @Override
    public String getName() {
        return instructionData.getName();
    }

    @Override
    public int cycles() {
        return instructionData.getCycles();
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public Variable getVariable() {
        return variable;
    }
}

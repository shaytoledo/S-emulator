package semulator.engine.logic.program;

import semulator.engine.logic.instruction.Instruction;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.List;

public interface Program {


    boolean validate();

    int calculateMaxDegree();

    int calculateCycles();

    Instruction getNextInstructionLabel(Instruction currentInstruction);

    Instruction getInstructionByLabel(Label nextLabel);


    String getName();

    void addInstruction(Instruction instruction);

    List<Instruction> getInstructions();

    public List<Variable> getVariables();

    public List<Label> getLabels();

    public Label getExitLabel();

    public List<String> getVariablesPeek();

    public List<String> getLabelsPeek();

    public List<String> getInstructionsPeek();
}

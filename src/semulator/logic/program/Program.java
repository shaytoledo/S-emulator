package semulator.logic.program;

import semulator.logic.instruction.SInstruction;
import semulator.logic.label.Label;

import java.util.List;

public interface Program {

    String getName();
    void addInstruction(SInstruction instruction);
    List<SInstruction> getInstructions();

    boolean validate();
    int calculateMaxDegree();
    int calculateCycles();

    SInstruction getNextInstructionLabel(SInstruction currentInstruction) ;
    SInstruction getInstructionByLabel(Label nextLabel);

    }

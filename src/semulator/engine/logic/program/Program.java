package semulator.engine.logic.program;

import semulator.engine.logic.instruction.SInstruction;
import semulator.engine.logic.label.Label;

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

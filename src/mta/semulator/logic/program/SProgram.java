package mta.semulator.logic.program;

import mta.semulator.logic.instruction.SInstruction;
import mta.semulator.logic.label.Label;

import java.util.List;

public interface SProgram {

    String getName();
    void addInstruction(SInstruction instruction);
    List<SInstruction> getInstructions();

    boolean validate();
    int calculateMaxDegree();
    int calculateCycles();

    SInstruction getNextInstructionLabel(SInstruction currentInstruction) ;
    SInstruction getInstructionByLabel(Label nextLabel);

    }

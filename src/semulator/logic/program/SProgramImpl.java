package semulator.logic.program;

import semulator.logic.instruction.SInstruction;
import semulator.logic.label.FixedLabel;
import semulator.logic.label.Label;

import java.util.ArrayList;
import java.util.List;

public class SProgramImpl implements SProgram{

    private final String name;
    private final List<SInstruction> instructions;

    public SProgramImpl(String name) {
        this.name = name;
        instructions = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addInstruction(SInstruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public List<SInstruction> getInstructions() {
        return instructions;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public int calculateMaxDegree() {
        // traverse all commands and find maximum degree
        return 0;
    }

    @Override
    public int calculateCycles() {
        // traverse all commands and calculate cycles
        return 0;
    }

    @Override
    public SInstruction getNextInstructionLabel(SInstruction currentInstruction) {
        int currentIndex = instructions.indexOf(currentInstruction);
        if (currentIndex >= 0 && currentIndex + 1 < instructions.size()) {
            return instructions.get(currentIndex + 1);
        }
        else {
            return null;
        }
    }

    @Override
    public SInstruction getInstructionByLabel(Label nextLabel) {
        for (SInstruction instruction : instructions) {
            if (instruction.getLabel().equals(nextLabel)) {
                return instruction;
            }
        }
        //It allways find the instruction by label because in load progran it check the labels jumps
        return null; // or throw an exception if label not found

    }
}

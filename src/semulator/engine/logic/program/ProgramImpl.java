package semulator.engine.logic.program;

import semulator.engine.logic.instruction.Instruction;
import semulator.engine.logic.label.FixedLabel;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.*;

public class ProgramImpl implements Program {

    private final String name;
    private final List<Instruction> instructions;
    private final List<Variable> variables;
    private final List<Label> labels;
    private final Label exitLabel;


    public static final class Builder {
        private String name;
        private final List<Instruction> instructions = new ArrayList<>();
        private final List<Variable> variables = new ArrayList<>();
        private final List<Label> labels = new ArrayList<>();
        private Label exitLabel;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        //------Instructions------
        public Builder withInstructions(Collection<? extends Instruction> newInstructions) {
            this.instructions.clear();
            if (newInstructions != null) {
                this.instructions.addAll(newInstructions);
            }
            return this;
        }

        public Builder addInstruction(Instruction instruction) {
            if (instruction != null) {
                this.instructions.add(instruction);
            }
            return this;
        }

        public Builder addInstructions(Instruction... instruction) {
            if (instruction != null) {
                this.instructions.addAll(Arrays.asList(instruction));
            }
            return this;
        }

        //------Variables------
        public Builder withVariables(Collection<? extends Variable> newVariables) {
            this.variables.clear();
            if (newVariables != null) {
                this.variables.addAll(newVariables);
            }
            return this;
        }

        public Builder addVariable(Variable variable) {
            if (variable != null) {
                this.variables.add(variable);
            }
            return this;
        }

        public Builder addVariables(Variable... vars) {
            if (vars != null) {
                this.variables.addAll(Arrays.asList(vars));
            }
            return this;
        }

        //-----Labels------
        public Builder withLabels(Collection<? extends Label> newLabels) {
            this.labels.clear();
            if (newLabels != null) {
                this.labels.addAll(newLabels);
            }
            return this;
        }

        public Builder addLabel(Label label) {
            if (label != null) {
                this.labels.add(label);
            }
            return this;
        }

        public Builder addLabels(Label... lbls) {
            if (lbls != null) {
                this.labels.addAll(Arrays.asList(lbls));
            }
            return this;
        }

        public Builder withExitLabel(Label exitLabel) {
            this.exitLabel = FixedLabel.EXIT;
            return this;
        }


        public ProgramImpl build() {
            return new ProgramImpl(this);
        }

    }

    public static Builder from(Program program) {
        Builder b = new Builder();
        if (program == null) return b;
        b.withName(program.getName());
        b.withInstructions(program.getInstructions());
        b.withVariables(program.getVariables());
        b.withLabels(program.getLabels());
        b.withExitLabel(program.getExitLabel());
        return b;
    }

    private ProgramImpl(Builder builder) {
        this.name = (builder.name == null || builder.name.isBlank()) ? "Unnamed Program" : builder.name;

        this.instructions = List.copyOf(builder.instructions);

        //Add all variables to map (from X1 to the real variable)
        Map<String, Variable> variablesByName = new LinkedHashMap<>();
        for (Variable variable : builder.variables) {
            if (variable != null && variable.getRepresentation() != null) {
                variablesByName.put(variable.getRepresentation(), variable);
            }
        }
        this.variables = List.copyOf(variablesByName.values());

        //Add all labels to map (from L1 to the real label)
        Map<String, Label> labelsByName = new LinkedHashMap<>();
        for (Label label : builder.labels) {
            if (label != null && label.getLabelRepresentation() != null) {
                labelsByName.put(label.getLabelRepresentation(), label);
            }
        }
        this.labels = List.copyOf(labelsByName.values());

        this.exitLabel = builder.exitLabel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addInstruction(Instruction instruction) {

    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public List<Label> getLabels() {
        return labels;
    }

    @Override
    public Label getExitLabel() {
        return exitLabel;
    }

    @Override
    public List<Variable> getVariables() {
        return variables;
    }

    public Variable getVariableByName(String name) {
        return variables.stream()
                .filter(v -> v.getRepresentation().equals(name))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Instruction getNextInstructionLabel(Instruction currentInstruction) {
        int currentIndex = instructions.indexOf(currentInstruction);
        if (currentIndex >= 0 && currentIndex + 1 < instructions.size()) {
            return instructions.get(currentIndex + 1);
        }
        else {
            return null;
        }
    }

    @Override
    public Instruction getInstructionByLabel(Label nextLabel) {
        for (Instruction instruction : instructions) {
            if (instruction.getLabel().equals(nextLabel)) {
                return instruction;
            }
        }
        //It always finds the instruction by label because in load program it checks the label jumps
        return null; // or throw an exception if label not found

    }



    // need to implement later
    @Override
    public boolean validate() {
        return false;
    }

    // need to implement later
    @Override
    public int calculateMaxDegree() {
        // traverse all commands and find maximum degree
        return 0;
    }

    @Override
    public int calculateCycles() {
        return instructions.stream()
                .mapToInt(Instruction::cycles)
                .sum();
    }

}
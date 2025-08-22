package semulator.logic.label;

public class LabelImpl implements Label{

    private final String label;

    public LabelImpl(int number) {
        label = "L" + number;
    }

    public String getLabelRepresentation() {
        return label;
    }
}

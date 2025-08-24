package semulator.engine.logic.instruction;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

public interface Instruction {

    String getName();
    Label execute(ExecutionContext context);
    int cycles();
    Label getLabel();
    Variable getVariable();
}

package mta.semulator.logic.instruction;

import mta.semulator.logic.execution.ExecutionContext;
import mta.semulator.logic.label.Label;
import mta.semulator.logic.variable.Variable;

public interface SInstruction {

    String getName();
    Label execute(ExecutionContext context);
    int cycles();
    Label getLabel();
    Variable getVariable();
}

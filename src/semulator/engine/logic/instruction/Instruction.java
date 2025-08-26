package semulator.engine.logic.instruction;

import semulator.engine.logic.execution.ExecutionContext;
import semulator.engine.logic.label.Label;
import semulator.engine.logic.variable.Variable;

import java.util.Map;

public interface Instruction {

    String getName();

    Label execute(ExecutionContext context);

    int cycles();

    Label getLabel();

    Variable getVariable();

    boolean isBasic();

    String toDisplayString();

    Map<String,String> args();
}

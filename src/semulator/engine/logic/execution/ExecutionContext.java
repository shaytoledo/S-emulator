package semulator.engine.logic.execution;

import semulator.engine.logic.variable.Variable;

public interface ExecutionContext {

    long getVariableValue(Variable v);
    void updateVariable(Variable v, long value);
}

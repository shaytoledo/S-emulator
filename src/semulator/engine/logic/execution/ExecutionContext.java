package semulator.engine.logic.execution;

import semulator.engine.logic.variable.Variable;

import java.util.Map;

public interface ExecutionContext {

    long getVariableValue(Variable v);
    void updateVariable(Variable v, long value);
    //Map<Variable, Long> getVariablesState();
    public Map<String, Long> getVariablesState();


    }

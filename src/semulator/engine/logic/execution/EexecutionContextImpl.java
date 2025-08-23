package semulator.engine.logic.execution;

import semulator.engine.logic.variable.Variable;
import semulator.engine.logic.variable.VariableImpl;
import semulator.engine.logic.variable.VariableType;

import java.util.Map;

public class EexecutionContextImpl implements ExecutionContext {
    Map<Variable, Long> variableState;


    public EexecutionContextImpl(Long... inputs) {
        // Initialize the variable state with the input values
        variableState = new java.util.HashMap<>();
        for (int i = 0; i < inputs.length; i++) {
            Variable v = new VariableImpl(VariableType.INPUT, i+1); // Assuming a constructor that takes a name
            variableState.put(v, inputs[i]);
        }
    }

    @Override
    public long getVariableValue(Variable variable) {
        Long value = variableState.get(variable);
        if (value == null) {
            return 0L; // just for now
        }
        return value;
    }


    @Override
    public void updateVariable(Variable v, long value) {
        variableState.put(v, value);

    }
}

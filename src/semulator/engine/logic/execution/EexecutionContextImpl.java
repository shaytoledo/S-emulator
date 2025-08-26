package semulator.engine.logic.execution;

import semulator.engine.logic.variable.Variable;
import semulator.engine.logic.variable.VariableImpl;
import semulator.engine.logic.variable.VariableType;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Collections.emptyList;

public class EexecutionContextImpl implements ExecutionContext {

    Map<String, Long> variableState;
    //Map<Variable, Long> variableState;



    public EexecutionContextImpl(List<Long> inputs) {
        // Initialize the variable state with the input values
        variableState = new java.util.HashMap<>();
        if (inputs == null) {
            inputs = emptyList();
        }
        for (int i = 0; i < inputs.size(); i++) {
            Variable v = new VariableImpl(VariableType.INPUT, i+1); // Assuming a constructor that takes a name
            variableState.put(v.getRepresentation(), inputs.get(i));
        }
    }

    private static String keyOf(Variable v) {
        return v.toString().toLowerCase(Locale.ROOT); // "x1", "y", "z2" — קבוע
    }


    @Override
    public long getVariableValue(Variable v) {
        String k = v.getRepresentation();
        Long val = variableState.get(k);
        if (val == null) {
            variableState.put(k, 0L); // init-on-read
            return 0L;
        }
        return val;
    }

    @Override
    public void updateVariable(Variable v, long value) {
        variableState.put(keyOf(v), value);
    }

//    @Override
//    public long getVariableValue(Variable variable) {
//        Long value = variableState.get(variable);
//        if (value == null) {
//            variableState.put(variable, 0L);
//            return 0L; // just for now
//        }
//        return value;
//    }
//
//
//    @Override
//    public void updateVariable(Variable v, long value) {
//        variableState.put(v, value);
//
//    }
//
//    @Override
//    public Map<Variable, Long> getVariablesState() {
//        return variableState;
//    }


    @Override
    public Map<String, Long> getVariablesState() {
        return variableState;
    }
}




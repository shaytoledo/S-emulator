package semulator.engine.logic.execution;

import semulator.engine.logic.variable.Variable;

import java.util.List;
import java.util.Map;

public interface ProgramExecutor {

    long run(List<Long> inputs);
    Map<String, Long> variablesState();
    long getTotalCycles();

}

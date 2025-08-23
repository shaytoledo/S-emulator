package semulator.engine.logic.execution;

import semulator.engine.logic.variable.Variable;

import java.util.Map;

public interface ProgramExecutor {

    long run(Long... input);
    Map<Variable, Long> variableState();
}

package semulator.engine.adapter.translate;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;


// The engine API, determines which methods the engine has

public interface Engine {

    LoadReport loadProgram(Path xmlPath);
    ProgramSummary getProgramSummary();
    void expandToLevel(int level);
    RunResult run(int level, List<Long> inputs);
    List<RunSummary> getHistory();

    //The reports about how the loading went and the errors if there were any
    record LoadReport(boolean ok, List<String> errors) {}

    record ProgramSummary(
            String name,
            List<String> inputs,
            List<String> labels,
            List<InstructionView> instructions
    ) {}

    record InstructionView(
            int number,
            String typeBS,
            String label,
            String command,
            int cycles
    ) {}

    record RunResult(
            long y,
            Map<String, Long> variables,
            long totalCycles
    ) {}

    record RunSummary(
            int runNumber,
            int level,
            List<Long> inputs,
            long y,
            long cycles
    ) {}
}


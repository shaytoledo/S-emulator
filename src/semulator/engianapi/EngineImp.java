package semulator.engianapi;

import semulator.engianapi.xml.Engine;

import java.nio.file.Path;
import java.util.List;

public class EngineImp implements Engine {

    private Program cuurentProgram;
    private int expandLevel = 0;
    private final List<Engine.RunResult> history = new ArrayList<>();
    private int runCounter = 0;

    @Override
    public LoadReport loadProgram(Path xmlPath) {
        try {
            var sprogram = EngineJaxbLoader.load(xmlPath);

            var tr = ProgramTranslator.translate(sprogram);
            if (!tr.errors.isEmpty()) {
                return new LoadReport(false, tr.errors);
            }

            this.cuurentProgram = tr.program;
            return new LoadReport(true, List.of());

        } catch (Exception e) {
            return new LoadReport(false, List.of("Failed to load: " + e.getMessage()));
        }
    }

    @Override
    public ProgramSummary getProgramSummary() {
        return null;
    }

    @Override
    public void expandToLevel(int level) {

    }

    @Override
    public RunResult run(int level, List<Long> inputs) {
        return null;
    }

    @Override
    public List<RunSummary> getHistory() {
        return List.of();
    }
}

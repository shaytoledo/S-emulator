package semulator.engine.adapter.translate;

import semulator.engine.logic.program.Program;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// implementation of the Engine interface (methods of the engine)

public class EngineImpl implements Engine {

    private Program cuurentProgram;
    private int expandLevel = 0;
    private final List<Engine.RunResult> history = new ArrayList<>();
    private int runCounter = 0;

    // Use EngineJaxbLoader to load the XML file and ProgramTranslator to translate it to internal representation
    @Override
    public LoadReport loadProgram(Path xmlPath) {
        try {
            // Load the XML file into SProgram representation
            var sprogram = EngineJaxbLoader.load(xmlPath);

            //Translate the loaded SProgram to internal Program representation
            var tr = ProgramTranslator.translate(sprogram);

            // If there are errors during translation, return them in the LoadReport (errors that define in the specification document)
            if (!tr.errors.isEmpty()) {
                // return the errors
                return new LoadReport(false, tr.errors);
            }

            this.cuurentProgram = tr.program;
            return new LoadReport(true, List.of());

        } catch (Exception e) {
            return new LoadReport(false, List.of("Failed to load: " + e.getMessage()));
        }
    }





    // need to implement
    @Override
    public ProgramSummary getProgramSummary() {
        return null;
    }

    @Override
    public void expandToLevel(int level) { }

    @Override
    public RunResult run(int level, List<Long> inputs) {
        return null;
    }

    @Override
    public List<RunSummary> getHistory() {
        return List.of();
    }
}

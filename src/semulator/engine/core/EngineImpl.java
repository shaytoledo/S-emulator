package semulator.engine.core;

import semulator.engine.adapter.translate.JaxbLoader;
import semulator.engine.adapter.translate.ProgramTranslator;
import semulator.engine.logic.exception.NoProgramLoadedException;
import semulator.engine.logic.program.Program;
import semulator.engine.logic.execution.ProgramExecutorImpl;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// implementation of the Engine interface (methods of the engine)

public class EngineImpl implements Engine {

    private static Program cuurentProgram = null;
    private int expandLevel = 0;
    private final List<Engine.RunResult> history = new ArrayList<>();
    private int runCounter = 0;

    // Use EngineJaxbLoader to load the XML file and ProgramTranslator to translate it to internal representation
    @Override
    public LoadReport loadProgram(Path xmlPath) {
        List<String> errors = new ArrayList<>();
        try {
            String fileName = xmlPath.getFileName().toString().toLowerCase();
            if (!fileName.endsWith(".xml")) {
                errors.add("Invalid extension (must be .xml): " + fileName);
            }

            if (!Files.exists(xmlPath) || !Files.isRegularFile(xmlPath)) {
                errors.add("File does not exist: " + xmlPath);
            }

            if (!errors.isEmpty()) {
                return new LoadReport(false, errors);
            }

            // Load the XML file into SProgram representation
            var sprogram = JaxbLoader.load(xmlPath);

            //Translate the loaded SProgram to internal Program representation
            var cuurentProgram = ProgramTranslator.translate(sprogram);

            // If there are errors during translation, return them in the LoadReport (errors that define in the specification document)
            if (!cuurentProgram.errors.isEmpty()) {
                // return the errors
                return new LoadReport(false, cuurentProgram.errors);
            }

            this.cuurentProgram = cuurentProgram.program;
            return new LoadReport(true, List.of());

        }
        catch (Exception e) {
            return new LoadReport(false, List.of("Failed to load: " + e.getMessage()));
        }
    }

    @Override
    public ProgramSummary getProgramSummary() {

        // there isn't valid load program
        if (cuurentProgram == null) {
            throw new NoProgramLoadedException("No program is loaded. Please load a file to display a program.");
        }

        if (cuurentProgram != null) {
            return new ProgramSummary(
                    cuurentProgram.getName(),
                    cuurentProgram.getVariablesPeek(),
                    cuurentProgram.getLabelsPeek(),
                    cuurentProgram.getInstructionsPeek()
            );
        }
        return null;
    }



    @Override
    public RunResult run(int level, List<Long> inputs) {
        ProgramExecutorImpl exe = new ProgramExecutorImpl(cuurentProgram); //, level, inputs);
        long y = exe.run(inputs);

        if (exe != null) {
            var res = new RunResult(
                    y,
                    exe.variablesState(),
                    exe.getTotalCycles()
            );
            history.add(res);
            return res;
        }
        return null;
    }






    // need to implement
    @Override
    public void expandToLevel(int level) { }

    @Override
    public List<RunSummary> getHistory() {
        return List.of();
    }
}

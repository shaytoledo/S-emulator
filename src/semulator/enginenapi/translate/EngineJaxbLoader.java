package semulator.enginenapi.translate;

import jakarta.xml.bind.JAXBContext;
import semulator.enginenapi.xml.gen.SProgram;

import java.nio.file.Path;

public class EngineJaxbLoader {

    public static SProgram load(Path xmlPath) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(SProgram.class);
        return (SProgram) jaxbContext.createUnmarshaller().unmarshal(xmlPath.toFile());
    }
}
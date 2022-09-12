package parser;

import data.Crew;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CrewReader {
    public static Crew read(final String file) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(file))) {
            Yaml yaml = new Yaml(new Constructor(Crew.class));
            return yaml.load(inputStream);
        }
    }
}

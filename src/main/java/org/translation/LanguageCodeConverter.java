package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting language codes to their names.
 */
public class LanguageCodeConverter {

    // map to store language code to language name
    private final Map<String, String> codeToNameMap = new HashMap<>();
    // map to store language name to language code
    private final Map<String, String> nameToCodeMap = new HashMap<>();

    /**
     * Default constructor which will load the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the language code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            // Use lines to populate the maps
            for (String line : lines) {
                // assuming tab-separated values: "en\tEnglish"
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    codeToNameMap.put(code, name);
                    nameToCodeMap.put(name, code);
                }
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the name of the language for the given language code.
     * @param code the language code
     * @return the name of the language corresponding to the code
     */
    public String fromLanguageCode(String code) {
        return codeToNameMap.getOrDefault(code, "unknown code");
    }

    /**
     * Returns the code of the language for the given language name.
     * @param language the name of the language
     * @return the 2-letter code of the language
     */
    public String fromLanguage(String language) {
        return nameToCodeMap.getOrDefault(language, "unknown language");
    }

    /**
     * Returns how many languages are included in this code converter.
     * @return how many languages are included in this code converter.
     */
    public int getNumLanguages() {
        return codeToNameMap.size();
    }
}

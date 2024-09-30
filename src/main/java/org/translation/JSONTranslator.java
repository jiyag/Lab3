package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, JSONObject> countryDataMap = new HashMap<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {
            // Read the file to get the data to populate the country data
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);

            // Populate the instance variables with data from jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                String countryCode = countryObject.getString("code");

                // Store the country data (translations) in the map
                countryDataMap.put(countryCode, countryObject);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // Return the list of language codes for the given country (no aliasing to a mutable object)
        if (countryDataMap.containsKey(country)) {
            JSONObject countryObject = countryDataMap.get(country);
            List<String> languages = new ArrayList<>(countryObject.keySet());
            languages.remove("code");
            return languages;
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countryDataMap.keySet());
    }
    
    /**
     * Returns the translation of a country based on the specified country and language codes.
     * If the country or language is not found, appropriate messages are returned.
     *
     * @param country the country code
     * @param language the language code
     * @return the translation of the country or a message indicating the country/language was not found
     */
    @Override
    public String translate(String country, String language) {
        String translation = "Country not found";

        if (countryDataMap.containsKey(country)) {
            JSONObject countryObject = countryDataMap.get(country);
            if (countryObject.has(language)) {
                translation = countryObject.getString(language);
            }
            else {
                translation = "Translation not available for this language";
            }
        }

        return translation;
    }
}

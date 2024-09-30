package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    private static final int EXPECTED_PARTS = 4;
    private Map<String, String> countryCodeMap;

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        countryCodeMap = new HashMap<>();
        try {
            // Read all lines from the specified file
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            // Populate the map with country codes and names from the file
            for (String line : lines) {
                // Split the line by tabs
                String[] parts = line.split("\t");

                if (parts.length == EXPECTED_PARTS) {
                    String countryName = parts[0].trim();
                    String alpha2Code = parts[1].trim();
                    String alpha3Code = parts[2].trim();
                    // We're ignoring the numeric code for now, but you could use it if necessary

                    // Store both 2-letter and 3-letter codes with the country name in the map
                    countryCodeMap.put(alpha2Code, countryName);
                    countryCodeMap.put(alpha3Code, countryName);
                }
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 2-letter or 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        return countryCodeMap.getOrDefault(code.trim().toUpperCase(), "unknown country code");
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        for (Map.Entry<String, String> entry : countryCodeMap.entrySet()) {
            if (Objects.equals(entry.getValue(), country)) {
                return entry.getKey();
            }
        }
        return "unknown country";
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return (int) countryCodeMap.values().stream().distinct().count() - 1;
    }
}

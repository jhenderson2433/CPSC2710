/*
 * Project: Airport Mapview App (or your assignment name)
 * Author: Your Name
 * Auburn Email: yourname@auburn.edu
 * Date: 2026-01-27
 * Description: Represents one airport record loaded from airport-codes.csv and
 *              provides a method to read all airports from resources.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Airport {

    // ===== Instance variables (one per column/value in the CSV rows) =====
    private String ident;
    private String type;
    private String name;
    private Integer elevationFt;     // nullable because some are missing
    private String continent;
    private String isoCountry;
    private String isoRegion;
    private String municipality;
    private String gpsCode;
    private String iataCode;
    private String localCode;

    // Your CSV rows include TWO coordinate values at the end (lon, lat)
    private Double longitude;
    private Double latitude;

    // Optional: empty constructor (useful for JavaFX / frameworks)
    public Airport() { }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getElevationFt() {
        return elevationFt;
    }

    public void setElevationFt(Integer elevationFt) {
        this.elevationFt = elevationFt;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getIsoCountry() {
        return isoCountry;
    }

    public void setIsoCountry(String isoCountry) {
        this.isoCountry = isoCountry;
    }

    public String getIsoRegion() {
        return isoRegion;
    }

    public void setIsoRegion(String isoRegion) {
        this.isoRegion = isoRegion;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getGpsCode() {
        return gpsCode;
    }

    public void setGpsCode(String gpsCode) {
        this.gpsCode = gpsCode;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    // Optional: constructor you can use internally
    public Airport(String ident, String type, String name, Integer elevationFt,
                   String continent, String isoCountry, String isoRegion,
                   String municipality, String gpsCode, String iataCode,
                   String localCode, Double longitude, Double latitude) {
        this.ident = ident;
        this.type = type;
        this.name = name;
        this.elevationFt = elevationFt;
        this.continent = continent;
        this.isoCountry = isoCountry;
        this.isoRegion = isoRegion;
        this.municipality = municipality;
        this.gpsCode = gpsCode;
        this.iataCode = iataCode;
        this.localCode = localCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // ===== Getters/Setters =====
    // Use IntelliJ: Right-click -> Generate -> Getter and Setter -> select all fields.
    // (Don’t hand-type them unless you have to.)

    /**
     * Reads airport-codes.csv from the resources folder and returns all airports.
     *
     * Put the file here:
     *   src/main/resources/airport-codes.csv
     *
     * Then this code will load it by resource name (no hard-coded path).
     */
    public static List<Airport> readAll() throws IOException {
        String resourceName = "airport-codes.csv";

        InputStream in = Airport.class.getClassLoader().getResourceAsStream(resourceName);
        if (in == null) {
            throw new IOException("Resource not found: " + resourceName
                    + " (make sure it is in src/main/resources)");
        }

        List<Airport> airports = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            // Read header line (we don’t trust it fully because coordinates are split into 2 values)
            String line = br.readLine();
            if (line == null) return airports;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                // Simple CSV split. Your file has no quoted commas in the sample,
                // so this works for this dataset.
                String[] parts = line.split(",", -1);

                // Expected:
                // Header says 12, but real rows are 13 because longitude + latitude at end.
                // Handle both just in case.
                if (parts.length == 13) {
                    Airport a = new Airport(
                            emptyToNull(parts[0]),
                            emptyToNull(parts[1]),
                            emptyToNull(parts[2]),
                            parseInteger(parts[3]),
                            emptyToNull(parts[4]),
                            emptyToNull(parts[5]),
                            emptyToNull(parts[6]),
                            emptyToNull(parts[7]),
                            emptyToNull(parts[8]),
                            emptyToNull(parts[9]),
                            emptyToNull(parts[10]),
                            parseDouble(parts[11]),  // longitude
                            parseDouble(parts[12])   // latitude
                    );
                    airports.add(a);
                } else if (parts.length == 12) {
                    // If coordinates were truly one field (like "lon,lat" quoted),
                    // you’d parse it here. But your current file rows aren’t that.
                    // We’ll still support a “best-effort” fallback:
                    Airport a = new Airport(
                            emptyToNull(parts[0]),
                            emptyToNull(parts[1]),
                            emptyToNull(parts[2]),
                            parseInteger(parts[3]),
                            emptyToNull(parts[4]),
                            emptyToNull(parts[5]),
                            emptyToNull(parts[6]),
                            emptyToNull(parts[7]),
                            emptyToNull(parts[8]),
                            emptyToNull(parts[9]),
                            emptyToNull(parts[10]),
                            null,
                            null
                    );
                    airports.add(a);
                } else {
                    // Skip weird lines instead of crashing the whole load
                    // (you can also throw if your instructor wants strict parsing)
                }
            }
        }

        return airports;
    }

    // ===== Helpers =====

    private static String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static Integer parseInteger(String s) {
        s = emptyToNull(s);
        if (s == null) return null;
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double parseDouble(String s) {
        s = emptyToNull(s);
        if (s == null) return null;
        try {
            return Double.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

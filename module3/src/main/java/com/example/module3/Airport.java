package com.example.module3;

/*
 * Project: Airport Mapview App (or your assignment name)
 * Author: Your Name
 * Auburn Email: yourname@auburn.edu
 * Date: 2026-01-27
 * Description: Represents one airport record loaded from airport-codes.csv and
 *              provides a method to read all airports from resources.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Airport() {
    }

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
     * <p>
     * Put the file here:
     * src/main/resources/airport-codes.csv
     * <p>
     * Then this code will load it by resource name (no hard-coded path).
     */
    public static List<Airport> readAll() throws IOException {
        String resourceName = "airport-codes.csv";

        InputStream in = Airport.class.getClassLoader().getResourceAsStream(resourceName);
        if (in == null) {
            throw new FileNotFoundException("Missing resource: " + resourceName
                    + " (Put it in src/main/resources)");
        }

        List<Airport> airports = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            String headerLine = br.readLine();
            if (headerLine == null) return airports;

            // Use quote-safe CSV splitting
            List<String> headers = splitCsvLine(headerLine);

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                List<String> values = splitCsvLine(line);

                // Build header -> value map (safe even if columns move)
                Map<String, String> row = new HashMap<>();
                int n = Math.min(headers.size(), values.size());
                for (int i = 0; i < n; i++) {
                    row.put(headers.get(i), values.get(i));
                }

                Airport a = new Airport();

                // ---- Map the columns you showed in your class ----
                // (These header names MUST match your CSV headers exactly.)
                a.setIdent(emptyToNull(row.get("ident")));
                a.setType(emptyToNull(row.get("type")));
                a.setName(emptyToNull(row.get("name")));
                a.setElevationFt(parseInteger(row.get("elevation_ft")));
                a.setContinent(emptyToNull(row.get("continent")));
                a.setIsoCountry(emptyToNull(row.get("iso_country")));
                a.setIsoRegion(emptyToNull(row.get("iso_region")));
                a.setMunicipality(emptyToNull(row.get("municipality")));
                a.setGpsCode(emptyToNull(row.get("gps_code")));
                a.setIataCode(emptyToNull(row.get("iata_code")));
                a.setLocalCode(emptyToNull(row.get("local_code")));

                // ---- Coordinates handling (handles both common formats) ----

                // Case 1: CSV has a single "coordinates" column like:  "-80.2329,40.4915"
                String coords = row.get("coordinates");
                if (coords != null && !coords.isBlank()) {
                    String[] parts = coords.split(",");
                    if (parts.length == 2) {
                        Double lon = parseDouble(parts[0].trim());
                        Double lat = parseDouble(parts[1].trim());
                        a.setLongitude(lon);
                        a.setLatitude(lat);
                    }
                } else {
                    // Case 2: CSV has separate longitude/latitude columns
                    // (Some files use "longitude"/"latitude", others use "lng"/"lat".)
                    Double lon = parseDouble(firstNonNull(row.get("longitude"), row.get("lng")));
                    Double lat = parseDouble(firstNonNull(row.get("latitude"), row.get("lat")));

                    // Case 3 (your screenshots suggest this): header count doesn’t include the last two,
                    // but the row ends with lon,lat anyway. Fallback to "last two values".
                    if (lon == null || lat == null) {
                        if (values.size() >= 2) {
                            Double lastLon = parseDouble(values.get(values.size() - 2));
                            Double lastLat = parseDouble(values.get(values.size() - 1));
                            // Only use fallback if they actually look like numbers
                            if (lon == null) lon = lastLon;
                            if (lat == null) lat = lastLat;
                        }
                    }

                    a.setLongitude(lon);
                    a.setLatitude(lat);
                }

                airports.add(a);
            }
        }

        return airports;
    }

// ===== Helpers (paste below readAll, still inside Airport class) =====

    private static String firstNonNull(String a, String b) {
        return (a != null && !a.isBlank()) ? a : b;
    }

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

    // Quote-safe CSV splitter (commas inside quotes won't break columns)
    private static List<String> splitCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // handle escaped quotes ("")
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out;
    }
}
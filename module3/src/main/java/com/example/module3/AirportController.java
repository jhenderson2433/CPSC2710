import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.List;

public class AirportController {

    @FXML private TextField identField;
    @FXML private TextField iataField;
    @FXML private TextField localField;

    @FXML private TextField typeField;
    @FXML private TextField nameField;
    @FXML private TextField elevationField;
    @FXML private TextField countryField;
    @FXML private TextField regionField;
    @FXML private TextField municipalityField;

    @FXML private Button searchButton;
    @FXML private WebView webView;

    private List<Airport> airports;

    @FXML
    private void initialize() {
        try {
            airports = Airport.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load airports CSV", e);
        }

        // Enter key behavior
        identField.setOnAction(e -> searchByIdent());
        iataField.setOnAction(e -> searchByIata());
        localField.setOnAction(e -> searchByLocal());

        searchButton.setOnAction(e -> searchByFirstNonBlank());
    }

    private void searchByFirstNonBlank() {
        if (!identField.getText().isBlank()) searchByIdent();
        else if (!iataField.getText().isBlank()) searchByIata();
        else if (!localField.getText().isBlank()) searchByLocal();
        else clearOutput();
    }

    private void searchByIdent() {
        String key = identField.getText().trim();
        Airport a = airports.stream()
                .filter(x -> key.equalsIgnoreCase(nullToEmpty(x.getIdent())))
                .findFirst().orElse(null);
        showAirport(a);
    }

    private void searchByIata() {
        String key = iataField.getText().trim();
        Airport a = airports.stream()
                .filter(x -> key.equalsIgnoreCase(nullToEmpty(x.getIataCode())))
                .findFirst().orElse(null);
        showAirport(a);
    }

    private void searchByLocal() {
        String key = localField.getText().trim();
        Airport a = airports.stream()
                .filter(x -> key.equalsIgnoreCase(nullToEmpty(x.getLocalCode())))
                .findFirst().orElse(null);
        showAirport(a);
    }

    private void showAirport(Airport a) {
        if (a == null) {
            clearOutput();
            return;
        }

        typeField.setText(nullToEmpty(a.getType()));
        nameField.setText(nullToEmpty(a.getName()));
        elevationField.setText(a.getElevationFt() == null ? "" : a.getElevationFt().toString());
        countryField.setText(nullToEmpty(a.getIsoCountry()));
        regionField.setText(nullToEmpty(a.getIsoRegion()));
        municipalityField.setText(nullToEmpty(a.getMunicipality()));

        updateMap(a);
    }

    private void updateMap(Airport a) {
        if (a.getLatitude() == null || a.getLongitude() == null) return;

        int zoom = 12; // assignment example uses 12
        String url = "https://www.windy.com/?" + a.getLatitude() + "," + a.getLongitude() + "," + zoom;
        webView.getEngine().load(url);
    }

    private void clearOutput() {
        typeField.clear();
        nameField.clear();
        elevationField.clear();
        countryField.clear();
        regionField.clear();
        municipalityField.clear();
        // optionally blank the webview
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}

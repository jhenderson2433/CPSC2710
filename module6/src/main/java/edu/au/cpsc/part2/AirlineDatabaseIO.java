package edu.au.cpsc.part2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AirlineDatabaseIO {

    public static void save(AirlineDatabase db, OutputStream strm) throws IOException {
        if (db == null || strm == null) {
            throw new IllegalArgumentException("db and strm cannot be null");
        }

        // Save a plain serializable list (NOT JavaFX ObservableList)
        List<ScheduledFlight> data = new ArrayList<>(db.getScheduledFlights());

        ObjectOutputStream out = new ObjectOutputStream(strm);
        out.writeObject(data);
        out.flush(); // do not close caller's stream
    }

    @SuppressWarnings("unchecked")
    public static AirlineDatabase load(InputStream strm) throws IOException {
        if (strm == null) {
            throw new IllegalArgumentException("strm cannot be null");
        }

        try {
            ObjectInputStream in = new ObjectInputStream(strm);
            Object obj = in.readObject();

            List<ScheduledFlight> data = (List<ScheduledFlight>) obj;

            AirlineDatabase db = new AirlineDatabase();
            for (ScheduledFlight sf : data) {
                db.addScheduledFlight(sf);
            }
            return db;

        } catch (ClassNotFoundException e) {
            // Happens if class name changed or missing
            throw new IOException("Failed to load database (class not found)", e);
        } catch (ClassCastException e) {
            throw new IOException("Failed to load database (wrong file format)", e);
        }
    }
}

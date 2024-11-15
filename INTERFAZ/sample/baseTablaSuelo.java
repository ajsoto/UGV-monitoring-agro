package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class baseTablaSuelo {

    SimpleStringProperty diaSColumna, horaSColumna;
    SimpleDoubleProperty HumeSueColumna, latSColumna, longSColumna;

    public baseTablaSuelo(String diaSColumna, String horaSColumna, double HumeSueColumna, double latSColumna, double longSColumna) {
        this.diaSColumna = new SimpleStringProperty(diaSColumna);
        this.horaSColumna = new SimpleStringProperty(horaSColumna);
        this.HumeSueColumna = new SimpleDoubleProperty(HumeSueColumna);
        this.latSColumna = new SimpleDoubleProperty(latSColumna);
        this.longSColumna = new SimpleDoubleProperty(longSColumna);
    }

    public String getDiaSColumna() { return diaSColumna.get(); }

    public SimpleStringProperty diaSColumnaProperty() { return diaSColumna; }

    public String getHoraSColumna() { return horaSColumna.get(); }

    public SimpleStringProperty horaSColumnaProperty() { return horaSColumna; }

    public double getHumeSueColumna() { return HumeSueColumna.get(); }

    public SimpleDoubleProperty humeSueColumnaProperty() { return HumeSueColumna; }

    public double getLatSColumna() { return latSColumna.get(); }

    public SimpleDoubleProperty latSColumnaProperty() { return latSColumna; }

    public double getLongSColumna() { return longSColumna.get(); }

    public SimpleDoubleProperty longSColumnaProperty() { return longSColumna; }
}

package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class baseTabla  {

    SimpleStringProperty diaColumna, horaColumna;
    SimpleDoubleProperty tempColumna,humColumna,coColumna,uvColumna,latColumna,logColumna;

    public baseTabla(String diaColumna, String horaColumna, double tempColumna, double humColumna, double coColumna, double uvColumna, double latColumna, double logColumna) {
    this.diaColumna= new SimpleStringProperty(diaColumna);
    this.horaColumna= new SimpleStringProperty(horaColumna);
    this.tempColumna= new SimpleDoubleProperty(tempColumna);
    this.humColumna= new SimpleDoubleProperty(humColumna);
    this.coColumna= new SimpleDoubleProperty(coColumna);
    this.uvColumna= new SimpleDoubleProperty(uvColumna);
    this.latColumna= new SimpleDoubleProperty(latColumna);
    this.logColumna= new SimpleDoubleProperty(logColumna);
    }

    public String getDiaColumna() { return diaColumna.get(); }

    public String getHoraColumna() { return horaColumna.get(); }

    public double getTempColumna() {
        return tempColumna.get();
    }

    public double getHumColumna() {
        return humColumna.get();
    }

    public double getCoColumna() {
        return coColumna.get();
    }

    public double getUvColumna() {
        return uvColumna.get();
    }

    public double getLatColumna() {
        return latColumna.get();
    }

    public double getLogColumna() {
        return logColumna.get();
    }
}

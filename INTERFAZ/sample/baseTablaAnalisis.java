package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class baseTablaAnalisis {
    SimpleStringProperty diaAnalisis;
    SimpleDoubleProperty mediaAnalisis,medianaAnalisis, modaAnalisis, minAnalisis, maxAnalisis, rangoAnalisis,
            desEstandarAnalisis, varianzaAnalisis;

    public baseTablaAnalisis(String diaAnalisis, double mediaAnalisis, double medianaAnalisis, double modaAnalisis,
                     double minAnalisis, double maxAnalisis, double rangoAnalisis, double desEstandarAnalisis, double varianzaAnalisis) {
        this.diaAnalisis= new SimpleStringProperty(diaAnalisis);
        this.mediaAnalisis= new SimpleDoubleProperty(mediaAnalisis);
        this.medianaAnalisis= new SimpleDoubleProperty(medianaAnalisis);
        this.modaAnalisis= new SimpleDoubleProperty(modaAnalisis);
        this.minAnalisis= new SimpleDoubleProperty(minAnalisis);
        this.maxAnalisis= new SimpleDoubleProperty(maxAnalisis);
        this.rangoAnalisis= new SimpleDoubleProperty(rangoAnalisis);
        this.desEstandarAnalisis= new SimpleDoubleProperty(desEstandarAnalisis);
        this.varianzaAnalisis= new SimpleDoubleProperty(varianzaAnalisis);
    }

    public String getDiaAnalisis() { return diaAnalisis.get(); }

    public double getMediaAnalisis() { return mediaAnalisis.get(); }

    public double getMedianaAnalisis() { return medianaAnalisis.get(); }

    public double getModaAnalisis() { return modaAnalisis.get(); }

    public double getMinAnalisis() { return minAnalisis.get(); }

    public double getMaxAnalisis() { return maxAnalisis.get(); }

    public double getRangoAnalisis() { return rangoAnalisis.get(); }

    public double getDesEstandarAnalisis() { return desEstandarAnalisis.get(); }

    public double getVarianzaAnalisis() { return varianzaAnalisis.get(); }

}

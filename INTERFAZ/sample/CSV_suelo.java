package sample;

public class CSV_suelo {
    private String fecha;
    private String hora;
    private double hum_suelo;
    private double latitud;
    private double longitud;

    public CSV_suelo(String fecha, String hora, double hum_suelo, double latitud, double longitud) {
        this.fecha = fecha;
        this.hora = hora;
        this.hum_suelo = hum_suelo;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public CSV_suelo() {
    }

    public String[] getArray(){
        String[] datos = {fecha, hora, String.valueOf(hum_suelo), String.valueOf(latitud),
                String.valueOf(longitud)};
        return datos;
    }
}

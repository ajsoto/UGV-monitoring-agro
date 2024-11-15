package sample;

public class CSV {
    private double temp;
    private double hum;
    private double co2;
    private double ind_uv;
    private double latitud;
    private double longitud;
    private String hora;
    private String fecha;

    public CSV(String fecha, String hora, double temp, double hum, double co2, double ind_uv, double lat, double lon) {
        this.fecha = fecha;
        this.hora = hora;
        this.temp = temp;
        this.hum = hum;
        this.co2 = co2;
        this.ind_uv = ind_uv;
        this.latitud = lat;
        this.longitud = lon;
    }

    public CSV() {
    }

    public String[] getArray(){
        String[] datos = {fecha, hora, String.valueOf(temp), String.valueOf(hum),
                String.valueOf(co2),  String.valueOf(ind_uv), String.valueOf(latitud),
                String.valueOf(longitud)};
        return datos;
    }
}
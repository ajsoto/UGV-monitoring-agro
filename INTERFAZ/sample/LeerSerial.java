package sample;

import com.csvreader.CsvWriter;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import gnu.io.CommPortIdentifier;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Scanner;

public class LeerSerial implements Runnable {
    Thread hilo;

    boolean stopThread, suspender = false, pausar = false;
    int lect;
    private InputStream inputStream;

    Date fecha = new Date();
    SimpleDateFormat for_fecha = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<Double> temp = new ArrayList<Double>();
    ArrayList<Double> hum = new ArrayList<Double>();
    ArrayList<Double> hum_suelo = new ArrayList<Double>();
    ArrayList<Double> co2 = new ArrayList<Double>();
    ArrayList<Double> ind_uv = new ArrayList<Double>();
    ArrayList<Double> latitud = new ArrayList<Double>();
    ArrayList<Double> longitud = new ArrayList<Double>();
    ArrayList<Double>[] var = new ArrayList[]{temp, hum, co2, ind_uv, latitud, longitud};

    ArrayList<String> hora_med = new ArrayList<>();
    String nom_csv = for_fecha.format(fecha) + ".csv";
    ArrayList<CSV> informacion = new ArrayList<>();
    ArrayList<CSV_suelo> informacion_suelo = new ArrayList<>();
    ArrayList<String> puertos = new ArrayList<>();
    String ruta1 = "";
    String ruta2 = "";
    SerialPort port;
    String readline = "";
    int posicion = 0;
    String port_select;
    boolean leer = false;
    boolean leer_suelo = false;
    boolean error = false;
    boolean puerto_abierto = false;
    boolean enviar_error = true;
    Enumeration puerto = null;
    CommPortIdentifier portId = null;
    //////////////////////////////
    ArrayList<String> baseDatos = new ArrayList<>();
    public void setBaseDatos(ArrayList<String> baseDatos) {
        this.baseDatos = baseDatos;
    }
    ///////////////////////////

    public boolean isError() {
        return error;
    }

    public ArrayList<Double> getTemp() {
        return temp;
    }

    public ArrayList<Double> getHum() {
        return hum;
    }

    public ArrayList<Double> getHum_suelo() {
        return hum_suelo;
    }

    public ArrayList<Double> getCo2() {
        return co2;
    }

    public ArrayList<Double> getInd_uv() {
        return ind_uv;
    }

    public ArrayList<Double> getLatitud() {
        return latitud;
    }

    public ArrayList<Double> getLongitud() {
        return longitud;
    }

    public ArrayList<String> getHora_med() { return hora_med; }

    public void setSp(ArrayList<String> ports) {
        for(int i=0; i < ports.size(); i++){
            port = SerialPort.getCommPort(ports.get(i));
            if (port != null){
                break;
            }
        }
        port.openPort();
        port.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[port.bytesAvailable()];
                int numRead = port.readBytes(newData, newData.length);
                //System.out.println("Read " + numRead + " bytes.");
                if (numRead > 0) {
                    for (int i = 0; i < newData.length; ++i) {
                        readline = readline + (char) newData[i];
                        //System.out.println(readline);
                        if(readline.length() > 0){
                            puerto_abierto = true;
                            enviar_error = false;
                        }
                        readline = "";
                    }
                }
            }
        });
        port.closePort();
    }

    String conc = "";

    public LeerSerial (){

    }

    public void start(String nombre){
        hilo = new Thread(this,nombre);
        stopThread = false;
        hilo.start();
    }
    private ArrayList<String> listSerials() {
        ArrayList<String> portList = new ArrayList<>();

        for (SerialPort port : SerialPort.getCommPorts()) {
            portList.add(port.getSystemPortName());
        }
        return portList;
    }

    @Override
    public void run() {
        String sistema = System.getProperty("os.name");
        if(sistema.startsWith("Windows")){
            System.out.println("Sistema operativo windows");
            ruta1 = "Datos_SEMANT\\Variables_agroambientales\\";
            ruta2 = "Datos_SEMANT\\Variable_suelo\\";

        } else if(sistema.startsWith("Linux")){
            System.out.println("Sistema operativo Linux");
            ruta1 = "Datos_SEMANT/Variables_agroambientales/";
            ruta2 = "Datos_SEMANT/Variable_suelo/";
        } else{

        }
        FileReader csvSimilar = null;
        FileReader csvSimilar_suelo = null;
        String[] valuesCSV;
        String[] valuesCSV_suelo;
        System.out.println("Verificando CSVs...");
        if(baseDatos.size() > 0) {
            for (int i = 0; i < baseDatos.size(); i++) {
                if (for_fecha.format(fecha).equals(baseDatos.get(i))) {
                    System.out.println("Existe un CSV similar");
                    try {
                        csvSimilar = new FileReader(ruta1 + baseDatos.get(i) + ".csv");
                        csvSimilar_suelo = new FileReader(ruta2 + baseDatos.get(i) + ".csv");
                        Scanner inputStream = new Scanner(csvSimilar);
                        Scanner inputStream_suelo = new Scanner(csvSimilar_suelo);
                        while (inputStream.hasNext()) {
                            String data = inputStream.next();
                            String data_suelo = inputStream_suelo.next();
                            valuesCSV = data.split(",");
                            valuesCSV_suelo = data_suelo.split(",");
                            hora_med.add(valuesCSV[1]);
                            hum_suelo.add(Double.parseDouble(valuesCSV_suelo[3]));
                            for (int x = 2; x < valuesCSV.length; x++) {
                                var[x - 2].add(Double.parseDouble(valuesCSV[x]));
                            }
                            informacion.add(new CSV(for_fecha.format(fecha), hora_med.get(hora_med.size() - 1),
                                    temp.get(temp.size() - 1), hum.get(hum.size() - 1), co2.get(co2.size() - 1),
                                    ind_uv.get(ind_uv.size() - 1), latitud.get(latitud.size() - 1),
                                    longitud.get(longitud.size() - 1)));
                            informacion_suelo.add(new CSV_suelo(for_fecha.format(fecha), hora_med.get(hora_med.size() - 1),
                                    hum_suelo.get(hum_suelo.size() - 1), latitud.get(latitud.size() - 1),
                                    longitud.get(longitud.size() - 1)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println(listSerials());
        leerDatos(listSerials());


    }

    public void leerDatos(ArrayList<String> lista){
        for (int i = 0; i < lista.size(); i++) {
            port = SerialPort.getCommPort(lista.get(i));
            System.out.println("Abriendo puerto");
            port.openPort();
            port.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        return;
                    }
                    //.out.println("Evento");
                    byte[] newData = new byte[port.bytesAvailable()];
                    int numRead = port.readBytes(newData, newData.length);
                    //System.out.println("Read " + numRead + " bytes.");
                    if (numRead > 0) {
                        for (int i = 0; i < newData.length; ++i) {
                            readline = readline + (char) newData[i];
                            //System.out.println(readline);
                            if (readline.length() > 0) {
                                formatoTexto(readline);
                            }
                            readline = "";
                        }
                    }
                }
            });
        }
    }
    public void formatoTexto(String lect){
        if (lect.equals("i")) {
            posicion = 0;
            leer = true;
            leer_suelo = false;
            Date hora = new Date();
            SimpleDateFormat for_hora = new SimpleDateFormat("hh:mm:ss");
            hora_med.add(for_hora.format(hora));
            return;
        }
        //Lectura humedad del suelo
        if(lect.equals("s")){
            leer_suelo = true;
            leer = false;
            return;
        }
        if(lect.equals("n") && leer_suelo == true){
            hum_suelo.add(Double.parseDouble(conc));
            conc = "";
            try{
                if (hum_suelo.get(hum_suelo.size() - 1).equals(-4.0)){
                    System.out.println("No almacenar suelo");
                } else{
                    CsvWriter archivo_suelo = new CsvWriter(ruta2 + nom_csv);
                    informacion_suelo.add(new CSV_suelo(for_fecha.format(fecha), hora_med.get(hora_med.size() - 1),
                            hum_suelo.get(hum_suelo.size() - 1), latitud.get(latitud.size() - 1),
                            longitud.get(longitud.size() - 1)));
                    for (CSV_suelo csv : informacion_suelo) {
                        String[] datos = csv.getArray();
                        archivo_suelo.writeRecord(datos);
                    }
                    archivo_suelo.close();
                }
                leer_suelo = false;
                leer = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        else if(leer_suelo){
            conc += lect;
            return;
        }
        //System.out.println("Estoy despues del if");
        if (lect.equals("n") && leer == true) {
            var[posicion].add(Double.parseDouble(conc));
            posicion++;
            conc = "";
            if (posicion == var.length) {
                System.out.println(longitud.get(longitud.size() - 1).equals(-7.0));
                try {
                    if(temp.get(temp.size() - 1).equals(-1.0) || hum.get(hum.size() - 1).equals(-2.0)
                            || co2.get(co2.size() - 1).equals(-3.0) || ind_uv.get(ind_uv.size() - 1).equals(-5.0) ){
                        System.out.println("No almacenar");
                    } else{
                        System.out.println("Almacenando");
                        CsvWriter archivo = new CsvWriter(ruta1 + nom_csv);
                        informacion.add(new CSV(for_fecha.format(fecha), hora_med.get(hora_med.size() - 1),
                                temp.get(temp.size() - 1), hum.get(hum.size() - 1), co2.get(co2.size() - 1),
                                ind_uv.get(ind_uv.size() - 1), latitud.get(latitud.size() - 1),
                                longitud.get(longitud.size() - 1)));
                        System.out.println("Generando csv");
                        for (CSV csv : informacion) {
                            String[] datos = csv.getArray();
                            archivo.writeRecord(datos);
                        }
                        archivo.close();
                    }

                    leer = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        else if(leer){
            conc += lect;
            return;
        }
    }

    public void stop(){
        port.closePort();
    }
}



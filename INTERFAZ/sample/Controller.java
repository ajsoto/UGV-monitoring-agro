package sample;

import com.csvreader.CsvWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import java.util.ArrayList;
import static java.lang.Double.valueOf;

public class Controller implements Initializable, Runnable {

    /**PARA VINCULAR LOS ELEMENTOS DE LA INTERFAZ**/

    /**MENÚ GENERAL**/
    @FXML private ImageView inicioFlecha, basededatosFlecha, CaracteristicasdelSueloFlecha,mapadeintensidadFlecha, graficasFlecha,
                            estadisticaFlecha, ayudaFlecha;
    @FXML private AnchorPane menuPanel, inicioPanel, ErrorPanel, basededatosPanel, CaracteristicasdelSueloPanel,
                            mapadeintensidadPanel, graficasPanel, rangograficasPanel, realgraficasPanel,
                            estadisticaPanel, ayudaPanel;
    /**MENÚ AYUDA**/
    @FXML private AnchorPane GeneralPanel, AyudaSuelo;
    @FXML private ScrollPane  basePanel, mapaPanel, grafPanel, estadPanel, AyudaTemp, AyudaHum,AyudaCO2,AyudaUV,
                            AyudaGPS, AyudaEstadistica, AyudSueloPanel;
    @FXML private ImageView baseFlecha, GeneralFlecha, mapaFlecha, grafFlecha, estadFlecha, SueloFlecha;
    /**MAPAS**/
    final NumberAxis xAxis_mapas = new NumberAxis();
    final NumberAxis yAxis_mapas = new NumberAxis();
    @FXML private NumberAxis mapasYAxis;
    @FXML private NumberAxis mapasXAxis;
    @FXML BubbleChart<Number,Number> Mapas = new BubbleChart<Number, Number>(xAxis_mapas,yAxis_mapas);

    @FXML private AnchorPane MapasTemp, MapasHum, MapasCO2, MapasSuelo, MapasUV, ColoresRangos;
    /**MENÚ MAPA DE INTENSIDAD**/
    @FXML private ImageView temFlecha, humFlecha, co2Flecha, humSueFlecha, indiceFlecha;
    @FXML private AnchorPane temPanel;
    @FXML private JFXComboBox ComboMapas;
    @FXML private VBox variablesMapas;

    /**MENÚ ESTADISTICA**/
    @FXML private JFXRadioButton TempRadioButton, HumRadioButton, CO2RadioButton, SueloRadioButton, UVRadioButton;
    @FXML private AnchorPane DiagramaPanel,EstadisticaPanel;
    @FXML private Label UnidadTempEstad, UnidadHumEstad, UnidadCO2Estad, UnidadSueloEstad, UnidadUVEstad,
                        UnidadTiempoEstad;
    /**MENÚ CARACTERISTICAS**/
    final CategoryAxis XAxis = new CategoryAxis();
    final NumberAxis YAxis = new NumberAxis();
    @FXML  LineChart<String,Number> Suelografica = new LineChart<String,Number>(XAxis,YAxis);
    @FXML private NumberAxis SueloYAxis;
    @FXML Label UnidadHumSuelo, UnidadMuestrasSuelo;
    /**Gráficas**/
    @FXML private JFXRadioButton TempGraficaRadio, HumGraficaRadio, CO2GraficaRadio, UVGraficaRadio;
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    @FXML  LineChart<String,Number> TemGrafica = new LineChart<String,Number>(xAxis,yAxis);
    @FXML  LineChart<String,Number> HumGrafica = new LineChart<String,Number>(xAxis,yAxis);
    @FXML  LineChart<String,Number> CO2Grafica = new LineChart<String,Number>(xAxis,yAxis);
    @FXML  LineChart<String,Number> UVGrafica = new LineChart<String,Number>(xAxis,yAxis);
    @FXML  LineChart<String,Number> rangoGraficas = new LineChart<String,Number>(xAxis,yAxis);
    @FXML private NumberAxis TemYAxis, HumYAxis, CO2YAxis, UVYAxis, graficaYAxis;

    final CategoryAxis xAxisBar = new CategoryAxis();
    final NumberAxis yAxisBar = new NumberAxis();
    @FXML BarChart<String, Number> DiagramaEstadis = new BarChart<>(xAxisBar, yAxisBar);
    @FXML Label UnidadTemp, UnidadHum, UnidadCO2, UnidadUV, UnidadMuestras;


    @FXML private JFXComboBox ComboFechaData,ComboFechaDataDos, ComboFechaGrafica, ComboFechaGraficaDos,
                        ComboFechaAnalisis,ComboFechaAnalisisDos, ComboFechaSuelo, ComboFechaSueloDos;
    @FXML private JFXButton BotonAceptarData, BotonAceptarGraficas,BotonAceptarAnalisis,
                        DiagramaBoton,TablaestadisticaBoton, BotonAceptarSuelo;

    @FXML private TableView basededatosTabla,TablaAnalisisEstadistico, SueloTabla;
    @FXML private TableColumn diaColumna, horaColumna,tempColumna, humColumna, coColumna, uvColumna,
              latColumna, logColumna;
    @FXML private TableColumn diaAnalisis, mediaAnalisis,medianaAnalisis, modaAnalisis, minAnalisis, maxAnalisis,
            rangoAnalisis, desEstandarAnalisis, varianzaAnalisis;
    @FXML private TableColumn diaSColumna, horaSColumna,HumeSueColumna, latSColumna, longSColumna;

    private ArrayList vector = new ArrayList();

    ObservableList<String> comboIDContent2, comboIDContent2_limpiar;

    /**TIEMPO REAL**/
    @FXML private ImageView BotonComenzar, BotonDetener;
    XYChart.Series series1_real = new XYChart.Series();
    XYChart.Series series2_real = new XYChart.Series();
    XYChart.Series series3_real = new XYChart.Series();
    XYChart.Series series4_real = new XYChart.Series();
    XYChart.Series serie_suelo_real = new XYChart.Series();
    boolean detener = true;


    LeerSerial mh1= new LeerSerial();
    ArrayList<String> puertos = new ArrayList<String>();
    ArrayList<CSV> Basededatos = new ArrayList<>();
    ArrayList<CSV_suelo> Basededatos_suelo = new ArrayList<>();
    ArrayList<String> hora_med = new ArrayList<>();
    ArrayList<Double> temp = new ArrayList<Double>();
    ArrayList<Double> hum = new ArrayList<Double>();
    ArrayList<Double> hum_suelo = new ArrayList<Double>();
    ArrayList<Double> co2 = new ArrayList<Double>();
    ArrayList<Double> ind_uv = new ArrayList<Double>();
    ArrayList<Double> latitud = new ArrayList<Double>();
    ArrayList<Double> longitud = new ArrayList<Double>();
    ArrayList<Double>[] variables = new ArrayList[]{temp, hum, co2, hum_suelo, ind_uv, latitud, longitud};
    String[] values;
    String[] valuesCSV;
    String[] valuesCSVSuelo;
    ArrayList<String> ejeGrafica = new ArrayList<>();
    ArrayList<String> ejeGraficaSuelo = new ArrayList<>();
    Thread hilo;
    boolean stopThread;
    ArrayList<Double> limitesTemp = new ArrayList<>();
    ArrayList<Double> limitesHum = new ArrayList<>();
    ArrayList<Double> limitesCO2 = new ArrayList<>();
    ArrayList<Double> limitesSuelo = new ArrayList<>();
    ArrayList<Double> limitesUV = new ArrayList<>();
    double limTempmax, limTempmin, limHummax,limHummin,limCO2max,limCO2min,limSuelomax, limSuelomin,limUVmax,limUVmin;

    Date fecha = new Date();
    SimpleDateFormat for_fecha = new SimpleDateFormat("yyyy-MM-dd");

    String ruta1 = "";
    String ruta2 = "";

    ObservableList basededatosLista = FXCollections.observableArrayList();
    ObservableList basededatosListaSuelo = FXCollections.observableArrayList();
    ObservableList basededatosAnalisis = FXCollections.observableArrayList();

    /**Arrays para analisis estadistico**/
    ArrayList<Double> total = new ArrayList<>();
    ArrayList<Double> media = new ArrayList<>();
    ArrayList<Double> mediana = new ArrayList<>();
    ArrayList<Double> moda = new ArrayList<>();
    ArrayList<Double> min = new ArrayList<>();
    ArrayList<Double> max = new ArrayList<>();
    ArrayList<Double> rango = new ArrayList<>();
    ArrayList<Double> desv = new ArrayList<>();
    ArrayList<Double> coV = new ArrayList<>();
    ArrayList<Double> var = new ArrayList<>();
    ArrayList<Double> estadistica = new ArrayList<>();
    ArrayList<String> fechas_csv = new ArrayList<>();

    ArrayList<Double> modas = new ArrayList<Double>();
    Map<Double, Double> countMap = new HashMap<>();
    ArrayList<ArrayList<Double>> modasTabla = new ArrayList<>();

    XYChart.Series seriesMedia = new XYChart.Series();
    XYChart.Series seriesMediana = new XYChart.Series();
    XYChart.Series seriesModa = new XYChart.Series();
    XYChart.Series seriesMin = new XYChart.Series();
    XYChart.Series seriesMax = new XYChart.Series();

    File[] archivos =  null;
    int i = 0;
    int j = 0;

    /* MAPAS */

    XYChart.Series serie_naranja = new XYChart.Series();
    XYChart.Series serie_amarillo = new XYChart.Series();
    XYChart.Series serie_azul1 = new XYChart.Series();
    XYChart.Series serie_azul2 = new XYChart.Series();
    XYChart.Series serie_verde1 = new XYChart.Series();
    XYChart.Series serie_verde2 = new XYChart.Series();
    XYChart.Series serie_rosa = new XYChart.Series();
    XYChart.Series serie_rojo = new XYChart.Series();

    /**Botones menú general**/
    public void ventanas_principales(AnchorPane panel_select, ImageView flecha_select){
        AnchorPane [] paneles = {inicioPanel, basededatosPanel, CaracteristicasdelSueloPanel, mapadeintensidadPanel, graficasPanel, estadisticaPanel, ayudaPanel};
        ImageView [] flechas = {inicioFlecha, basededatosFlecha, CaracteristicasdelSueloFlecha, mapadeintensidadFlecha, graficasFlecha, estadisticaFlecha, ayudaFlecha};

        for (AnchorPane panel:paneles){
            if(panel.equals(panel_select)) panel.setVisible(true);
            else panel.setVisible(false);
        }
        for (ImageView flecha:flechas){
            if(flecha.equals(flecha_select)) flecha.setVisible(true);
            else flecha.setVisible(false);
        }
    }
    public void BotonSalida(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
    public void BotonInicio(MouseEvent event) { ventanas_principales(inicioPanel, inicioFlecha); }
    public void BotonBasedeDatos(MouseEvent event) { ventanas_principales(basededatosPanel, basededatosFlecha);}
    public void BotonCaracteristicasdelSuelo(MouseEvent event) { ventanas_principales(CaracteristicasdelSueloPanel, CaracteristicasdelSueloFlecha);}
    public void BotonMapadeIntensidad(MouseEvent event) { ventanas_principales(mapadeintensidadPanel, mapadeintensidadFlecha); }
    public void BotonGrafica(MouseEvent event) { ventanas_principales(graficasPanel, graficasFlecha);}
    public void BotonEstadistica(MouseEvent event) { ventanas_principales(estadisticaPanel, estadisticaFlecha);}
    public void BotonAyuda(MouseEvent event) { ventanas_principales(ayudaPanel, ayudaFlecha);}

    /**Botones menú ayuda**/
    public void scrolls_ayuda(ScrollPane panel_select, ImageView flecha_select){
        ScrollPane [] paneles = {basePanel, mapaPanel, grafPanel, estadPanel, AyudSueloPanel};
        ImageView [] flechas = {baseFlecha, mapaFlecha, grafFlecha, estadFlecha, SueloFlecha};

        for(ScrollPane panel:paneles){
            if(panel.equals(panel_select)) panel.setVisible(true);
            else panel.setVisible(false);
        }
        for(ImageView flecha:flechas){
            if(flecha.equals(flecha_select)) flecha.setVisible(true);
            else flecha.setVisible(false);
        }

        GeneralPanel.setVisible(false);
        GeneralFlecha.setVisible(false);
    }
    public void Botonbase(MouseEvent event) { scrolls_ayuda(basePanel, baseFlecha);}
    public void BotonSueloAyud(MouseEvent event) { scrolls_ayuda(AyudSueloPanel, SueloFlecha);}
    public void BotonGeneral(MouseEvent event) {
        GeneralPanel.setVisible(true);
        GeneralFlecha.setVisible(true);

        basePanel.setVisible(false);
        baseFlecha.setVisible(false);
        AyudSueloPanel.setVisible(false);
        SueloFlecha.setVisible(false);
        mapaPanel.setVisible(false);
        mapaFlecha.setVisible(false);
        grafPanel.setVisible(false);
        grafFlecha.setVisible(false);
        estadPanel.setVisible(false);
        estadFlecha.setVisible(false);
    }
    public void Botonmapa(MouseEvent event) { scrolls_ayuda(mapaPanel, mapaFlecha);}
    public void Botongraf(MouseEvent event) { scrolls_ayuda(grafPanel, grafFlecha);}
    public void Botonestad(MouseEvent event) { scrolls_ayuda(estadPanel, estadFlecha);}

    public void verMas(ScrollPane panel_select){
        ScrollPane [] paneles = {AyudaHum, AyudaTemp, AyudaCO2, AyudaUV, AyudaGPS};

        for(ScrollPane panel:paneles){
            if(panel.equals(panel_select)) panel.setVisible(true);
            else panel.setVisible(false);
        }
    }
    public void VermasTemp(javafx.event.ActionEvent event){ verMas(AyudaTemp);}
    public void VermasHum(javafx.event.ActionEvent event){ verMas(AyudaHum);}
    public void VermasCO2(javafx.event.ActionEvent event){ verMas(AyudaCO2);}
    public void VermasUV(javafx.event.ActionEvent event){ verMas(AyudaUV);}
    public void VermasGPS(javafx.event.ActionEvent event){ verMas(AyudaGPS);}
    public void VermasSuelo(javafx.event.ActionEvent event){
        AyudaSuelo.setVisible(true);
        AyudSueloPanel.setVisible(false);
    }
    public void Regresar(javafx.event.ActionEvent event){
        if (AyudaSuelo.isVisible()) AyudSueloPanel.setVisible(true);
        AyudaUV.setVisible(false);
        AyudaCO2.setVisible(false);
        AyudaHum.setVisible(false);
        AyudaTemp.setVisible(false);
        AyudaSuelo.setVisible(false);
        AyudaGPS.setVisible(false);
    }
    public void VermasEstadistica(javafx.event.ActionEvent event) {
        AyudaEstadistica.setVisible(true);
        estadPanel. setVisible(false);
    }
    public void RegresarEstadistica(javafx.event.ActionEvent event){
        AyudaEstadistica.setVisible(false);
        estadPanel.setVisible(true);
    }

    /**Botones menú mapa de intensidad**/
    public void paneles_intensidad(AnchorPane panel_select, ImageView flecha_select){
        AnchorPane [] paneles = {MapasTemp, MapasHum, MapasCO2, MapasSuelo, MapasUV};
        ImageView [] flechas = {temFlecha, humFlecha, co2Flecha, humSueFlecha, indiceFlecha};
        temPanel.setVisible(true);

        for(AnchorPane panel:paneles){
            if(panel.equals(panel_select)) panel.setVisible(true);
            else panel.setVisible(false);
        }
        for(ImageView flecha:flechas){
            if(flecha.equals(flecha_select)) flecha.setVisible(true);
            else flecha.setVisible(false);
        }
    }
    public void Botontem(MouseEvent event) {
        double [] rangos_temp = {17, 20, 23, 26, 29, 32, 35};
        paneles_intensidad(MapasTemp, temFlecha);
        CrearMapa(2, rangos_temp,  false);
    }
    public void Botonhum(MouseEvent event) {
        double [] rangos_hum = {65, 66.67, 68.33, 70, 71.67, 73.33, 75};
        paneles_intensidad(MapasHum, humFlecha);
        CrearMapa(3, rangos_hum,  false);
    }
    public void Botonco2(MouseEvent event) {
        double [] rangos_co2 = {400, 500, 600, 700, 800, 900, 1000};
        paneles_intensidad(MapasCO2, co2Flecha);
        CrearMapa(4, rangos_co2,  false);
    }
    public void BotonhumSuelo(MouseEvent event) {
        double [] rangos_suelo = {25, 27.5, 30, 32.5, 35, 37.5, 40};
        paneles_intensidad(MapasSuelo, humSueFlecha);
        CrearMapa(2, rangos_suelo,  true);
    }
    public void Botonindice(MouseEvent event) {
        double [] rangos_uv = {3, 4, 5, 6, 7, 8, 9, 10};
        paneles_intensidad(MapasUV, indiceFlecha);
        CrearMapa(6, rangos_uv,  false);
    }

    /**botones menú estadística**/
    public void unidades(Label unidad){
        Label [] textos = {UnidadTempEstad, UnidadHumEstad, UnidadSueloEstad, UnidadUVEstad};

        for(Label texto:textos){
            if(texto.equals(unidad)) texto.setVisible(true);
            else texto.setVisible(false);
        }
        UnidadTiempoEstad.setVisible(true);
    }
    public void BotonDiagramaEstadistica  (javafx.event.ActionEvent event) {
        ArrayList<ArrayList<Double>> [] listas = new ArrayList[]{estadistica, media, mediana, moda, max, min};
        XYChart.Series [] series = {seriesMedia, seriesMediana, seriesModa, seriesMin, seriesMax};
        DiagramaPanel.setVisible(true);
        EstadisticaPanel.setVisible(false);
        basededatosAnalisis.clear();
        fechas_csv.clear();
        for(ArrayList lista:listas) lista.clear();

        for(XYChart.Series serie: series) serie.getData().clear();

        if (TempRadioButton.isSelected()) {
            AnalisisEstadistico(2, false);
            unidades(UnidadTempEstad);
        }
        if (HumRadioButton.isSelected()) {
            AnalisisEstadistico(3, false);
            unidades(UnidadHumEstad);
        }
        if (CO2RadioButton.isSelected()) {
            AnalisisEstadistico(4, false);
            unidades(UnidadCO2Estad);
        }
        if (SueloRadioButton.isSelected()) {
            AnalisisEstadistico(2, true);
            unidades(UnidadSueloEstad);
        }
        if (UVRadioButton.isSelected()) {
            AnalisisEstadistico(6, false);
            unidades(UnidadUVEstad);
        }
        seriesMedia.setName("Media");
        for (int j = 0; j < media.size(); j++) seriesMedia.getData().add(new XYChart.Data(fechas_csv.get(j), media.get(j)));

        seriesMediana.setName("Mediana");
        for (int j = 0; j < mediana.size(); j++) seriesMediana.getData().add(new XYChart.Data(fechas_csv.get(j), mediana.get(j)));

        seriesModa.setName("Moda");
        for (int j = 0; j < moda.size(); j++) seriesModa.getData().add(new XYChart.Data(fechas_csv.get(j), moda.get(j)));

        seriesMin.setName("Min");
        for (int j = 0; j < min.size(); j++) seriesMin.getData().add(new XYChart.Data(fechas_csv.get(j), min.get(j)));

        seriesMax.setName("Max");
        for (int j = 0; j < max.size(); j++) seriesMax.getData().add(new XYChart.Data(fechas_csv.get(j), max.get(j)));
        DiagramaEstadis.getData().addAll(seriesMedia, seriesMediana, seriesModa, seriesMin, seriesMax);
    }
    public void BotonTablaEstadistica  (javafx.event.ActionEvent event) {
        ArrayList<ArrayList<Double>> [] listas = new ArrayList[]{estadistica, media, mediana, moda, max, min, rango, desv, var, modasTabla};
        DiagramaPanel.setVisible(false);
        EstadisticaPanel.setVisible(true);
        basededatosAnalisis.clear();
        for(ArrayList lista:listas) lista.clear();
        fechas_csv.clear();

        if (TempRadioButton.isSelected()) AnalisisEstadistico(2, false);
        if (HumRadioButton.isSelected()) AnalisisEstadistico(3, false);
        if (CO2RadioButton.isSelected()) AnalisisEstadistico(4, false);
        if (SueloRadioButton.isSelected()) AnalisisEstadistico(2, true);
        if (UVRadioButton.isSelected()) AnalisisEstadistico(6, false);
        for (int j = 0; j < media.size(); j++) basededatosAnalisis.add(new baseTablaAnalisis(fechas_csv.get(j), media.get(j), mediana.get(j),
                                                                         moda.get(j), min.get(j), max.get(j), rango.get(j), desv.get(j), var.get(j)));
        if(media.size() > 1){
            /*Media*/
            double suma = 0;
            for (int j = 0; j < total.size(); j++) suma = suma + total.get(j);
            media.add(Math.round((suma/total.size()) * 1000) / 1000d);
            /*Mediana*/
            int medianaEs = total.size()/2;
            Collections.sort(total);
            System.out.println(total);
            if(medianaEs%2 == 1) mediana.add(Math.round(total.get(medianaEs) * 10000) / 10000d);
            else mediana.add(Math.round(((total.get(medianaEs-1) + total.get(medianaEs))/2) * 10000) / 10000d);
            /*Moda*/
            double modaEs = 0;
            double maxCount = -1;
            modas.clear();
            countMap.clear();
            for (Double i : total) {
                double conModa = 0;
                if (countMap.containsKey(i)) conModa = countMap.get(i) + 1;
                else conModa = 1;
                countMap.put(i, conModa);
                if(conModa > maxCount) maxCount = conModa;
            }
            for (Map.Entry<Double,Double> tuple : countMap.entrySet()) {
                if (tuple.getValue() == maxCount) modas.add(tuple.getKey());
            }
            double rangoModa = Math.abs(modas.get(0)-mediana.get(mediana.size()-1));
            modaEs = modas.get(0);
            for (double n:modas) {
                if(Math.abs(n-mediana.get(mediana.size()-1)) < rangoModa) modaEs = n;
            }
            moda.add(Math.round(modaEs * 10000) / 10000d);
            /*Maximo*/
            double maxiEs;
            maxiEs = total.get(0);
            for (int j = 0; j < total.size(); j++) {
                if(total.get(j) > maxiEs) maxiEs = total.get(j);
            }
            max.add(Math.round(maxiEs * 10000) / 10000d);
            /*Minimo*/
            double miniEs;
            miniEs = total.get(0);
            for (int k = 0; k < total.size(); k++) {
                if(total.get(k) < miniEs) miniEs = total.get(k);
            }
            min.add(Math.round(miniEs * 10000) / 10000d);
            /*Rango*/
            double rangoEs;
            rangoEs = maxiEs - miniEs;
            rango.add(Math.round(rangoEs * 10000) / 10000d);
            /*Varianza*/
            double varianza = 0;
            for(int i = 0; i < total.size(); i++){
                double rango;
                rango = Math.pow(total.get(i) - media.get(media.size()-1), 2f);
                varianza = varianza + rango;
            }
            varianza = varianza / total.size();
            var.add(Math.round(varianza * 10000) / 10000d);
            /*Desviancion Estandar*/
            double desviacion;
            desviacion = Math.sqrt(varianza);
            desv.add(Math.round(desviacion * 10000) / 10000d);
            basededatosAnalisis.add(new baseTablaAnalisis("Total", media.get(media.size()-1), mediana.get(mediana.size()-1),
                    moda.get(moda.size()-1), min.get(min.size()-1), max.get(max.size()-1), rango.get(rango.size()-1),
                    desv.get(desv.size()-1), var.get(var.size()-1)));
        }
        TablaAnalisisEstadistico.setItems(basededatosAnalisis);

    }
    public void AnalisisEstadistico(int columna, boolean suelo){
        double miniEs = 0;
        double maxiEs = 0;
        double rangoEs = 0;
        String nombre = "";
        int leidos = 0;
        Iterator it = vector.iterator();
        Boolean b= true;
        int d=0;
        int cont = 0;
        double suma = 0.0;
        boolean leer = false;
        FileReader archivo;
        total.clear();
        for(int x = 0; x < archivos.length; x++) {
            suma = 0;
            leidos++;
            estadistica.clear();
            cont = 0;
            if (vector.get(x) == ComboFechaData.getValue().toString())  leer = true;
            if (leer) {
                fechas_csv.add(vector.get(x) + "");
                ejeGrafica.add(vector.get(x)+"");
                if (suelo){
                    try {
                        String fileName = ruta2 + vector.get(x) + ".csv";
                        archivo = new FileReader (fileName);
                        Scanner inputStream = new Scanner(archivo);
                        while (inputStream.hasNext()) {
                            String data = inputStream.next();
                            valuesCSVSuelo = data.split(",");
                            total.add(Double.parseDouble(valuesCSVSuelo[columna]));
                            estadistica.add(Double.parseDouble(valuesCSVSuelo[columna]));
                            cont++;
                        } }  catch (FileNotFoundException e) { e.printStackTrace(); }
                }
                else{
                    try {
                        System.out.println(vector.get(x));
                        String fileName = ruta1 + vector.get(x) + ".csv";
                        archivo = new FileReader (fileName);
                        Scanner inputStream = new Scanner(archivo);
                        while (inputStream.hasNext()) {
                            String data = inputStream.next();
                            valuesCSV = data.split(",");
                            total.add(Double.parseDouble(valuesCSV[columna]));
                            estadistica.add(Double.parseDouble(valuesCSV[columna]));
                            cont++;
                        } }  catch (FileNotFoundException e) { e.printStackTrace(); }
                }

                if (vector.get(x) == ComboFechaDataDos.getValue().toString()) leer = false;
                /*Media*/
                for (int j = 0; j < cont; j++) suma = suma + estadistica.get(j);
                media.add(Math.round((suma/cont) * 1000) / 1000d);
                /*Mediana*/
                int medianaEs = cont/2;
                Collections.sort(estadistica);
                System.out.println("columna = " + estadistica);
                if(medianaEs%2 == 1) mediana.add(Math.round(estadistica.get(medianaEs) * 10000) / 10000d);
                else mediana.add(Math.round(((estadistica.get(medianaEs-1) + estadistica.get(medianaEs))/2) * 10000) / 10000d);

                /*Moda*/
                double modaEs = 0;
                double maxCount = -1;
                modas.clear();
                countMap.clear();
                for (Double i : estadistica) {
                    double conModa = 0;
                    if (countMap.containsKey(i)) conModa = countMap.get(i) + 1;
                    else conModa = 1;
                    countMap.put(i, conModa);
                    if(conModa > maxCount) maxCount = conModa;
                }
                for (Map.Entry<Double,Double> tuple : countMap.entrySet()) {
                    if (tuple.getValue() == maxCount) modas.add(tuple.getKey());
                }
                double rangoModa = Math.abs(modas.get(0)-mediana.get(mediana.size()-1));
                modaEs = modas.get(0);
                for (double n:modas) {
                    if(Math.abs(n-mediana.get(mediana.size()-1)) < rangoModa) modaEs = n;
                }
                moda.add(Math.round(modaEs * 10000) / 10000d);
                /*Maximo*/
                maxiEs = estadistica.get(0);
                for (int j = 0; j < estadistica.size(); j++) {
                    if(estadistica.get(j) > maxiEs) maxiEs = estadistica.get(j);
                }
                max.add(Math.round(maxiEs * 10000) / 10000d);
                /*Minimo*/
                miniEs = estadistica.get(0);;
                for (int k = 0; k < estadistica.size(); k++) {
                    if(estadistica.get(k) < miniEs) miniEs = estadistica.get(k);
                }
                min.add(Math.round(miniEs * 10000) / 10000d);
                /*Rango*/
                rangoEs = maxiEs - miniEs;
                rango.add(Math.round(rangoEs * 10000) / 10000d);
                /*Varianza*/
                double varianza = 0;
                for(int i = 0; i < estadistica.size(); i++){
                    double rango;
                    rango = Math.pow(estadistica.get(i) - media.get(media.size() - 1), 2f);
                    varianza = varianza + rango;
                }
                varianza = varianza / estadistica.size();
                var.add(Math.round(varianza * 10000) / 10000d);
                /*Desviancion Estandar*/
                double desviacion;
                desviacion = Math.sqrt(varianza);
                desv.add(Math.round(desviacion * 10000) / 10000d);
            }
        }
    }
    public void ActivarBotones(javafx.event.ActionEvent event){
        DiagramaBoton.setDisable(false);
        TablaestadisticaBoton.setDisable(false);
    }

    public void Habilitar_combo2(JFXComboBox combo_select){
        JFXComboBox [] combos = {ComboFechaData, ComboFechaGrafica, ComboFechaAnalisis, ComboFechaSuelo};
        JFXComboBox [] combos_dos = {ComboFechaDataDos, ComboFechaAnalisisDos, ComboFechaGraficaDos, ComboFechaSueloDos};

        for (JFXComboBox combo:combos_dos) combo.setDisable(false);
        for(JFXComboBox combo:combos){
            if(!combo.equals(combo_select)) combo.setValue(combo_select.getValue());
        }
        ComboDos();
    }
    public void Habilitar_aceptar(JFXComboBox combo_select){
        JFXButton [] botones = {BotonAceptarData, BotonAceptarAnalisis, BotonAceptarGraficas, BotonAceptarSuelo};
        JFXComboBox [] combos = {ComboFechaDataDos, ComboFechaGraficaDos, ComboFechaAnalisisDos, ComboFechaSueloDos};

        for(JFXButton boton:botones) boton.setDisable(false);
        for(JFXComboBox combo:combos){
            if(!combo.equals(combo_select)) combo.setValue(combo_select.getValue());
        }
    }
    public void BotonFechaBase (javafx.event.ActionEvent event){ Habilitar_combo2(ComboFechaData); }
    public void BotonFechaBaseDos (javafx.event.ActionEvent event){ Habilitar_aceptar(ComboFechaDataDos); }
    public void BotonFechaGrafica (javafx.event.ActionEvent event){ Habilitar_combo2(ComboFechaGrafica); }
    public void BotonFechaGraficaDos (javafx.event.ActionEvent event){ Habilitar_aceptar(ComboFechaGraficaDos); }
    public void BotonFechaAnalisis (javafx.event.ActionEvent event){ Habilitar_combo2(ComboFechaAnalisis); }
    public void BotonFechaAnalisisDos (javafx.event.ActionEvent event){ Habilitar_aceptar(ComboFechaAnalisisDos); }
    public void BotonFechaSuelo (javafx.event.ActionEvent event){ Habilitar_combo2(ComboFechaSuelo); }
    public void BotonFechaSueloDos (javafx.event.ActionEvent event){ Habilitar_aceptar(ComboFechaSueloDos); }
    public void ComboDos(){
        JFXComboBox [] combos = {ComboFechaDataDos, ComboFechaAnalisisDos, ComboFechaGraficaDos, ComboFechaSueloDos};
        for(JFXComboBox combo:combos){
            comboIDContent2_limpiar = FXCollections.observableArrayList();
            combo.setItems(comboIDContent2_limpiar);
        }

        ArrayList vector2 = new ArrayList();
        boolean agregar = false;
        for (int j = 0; j < vector.size(); j++) {
            if (vector.get(j).equals(ComboFechaData.getValue())) agregar = true;
            if (agregar) vector2.add(vector.get(j));
        }
        comboIDContent2 = FXCollections.observableArrayList(vector2);

        for(JFXComboBox combo:combos) combo.setItems(comboIDContent2);
    }

    public void Aceptar(javafx.event.ActionEvent event){
        JFXRadioButton [] botones = {TempRadioButton, HumRadioButton, CO2RadioButton, SueloRadioButton, UVRadioButton,
                TempGraficaRadio, HumGraficaRadio, CO2GraficaRadio, UVGraficaRadio};
        basededatosLista.clear();
        basededatosListaSuelo.clear();
        ejeGrafica.clear();
        ejeGraficaSuelo.clear();

        if(!TempGraficaRadio.isDisable()) leer_csv();
        for(JFXRadioButton boton:botones) boton.setDisable(false);
    }
    public void unidades_csv(Label unidad){
        Label [] textos = {UnidadTemp, UnidadHum, UnidadCO2, UnidadUV};
        for(Label texto:textos){
            if(texto.equals(unidad)) texto.setVisible(true);
            else texto.setVisible(false);
        }
        UnidadMuestras.setVisible(true);
    }
    public void leer_csv(){
        ArrayList [] limites = {limitesCO2, limitesHum, limitesSuelo, limitesTemp, limitesUV};
        for(ArrayList lista: limites) lista.clear();

        rangoGraficas.getData().clear();
        Suelografica.getData().clear();
        boolean leer_csv = false;
        int cont=0;
        int contSuelo=0;
        String[] ejex;
        String[] ejexSuelo;
        FileReader archivo;
        for(int x = 0; x < archivos.length; x++) {
            XYChart.Series series1 = new XYChart.Series();
            XYChart.Series series2 = new XYChart.Series();
            XYChart.Series series3 = new XYChart.Series();
            XYChart.Series series4 = new XYChart.Series();
            XYChart.Series series5 = new XYChart.Series();
            if (vector.get(x) == ComboFechaData.getValue().toString()) leer_csv = true;
            if (leer_csv) {
                System.out.println(vector.get(x));
                String fileName = ruta1 + vector.get(x) + ".csv";
                String fileNameSuelo = ruta2 + vector.get(x) + ".csv";
                //ArrayList<Double> lista = new ArrayList();
                try {
                    archivo = new FileReader (fileName);
                    Scanner inputStream = new Scanner(archivo);
                    while (inputStream.hasNext()) {
                        String data = inputStream.next();
                        valuesCSV = data.split(",");
                        limitesGraficas(valuesCSV, true);
                        ejeGrafica.add(String.valueOf(cont));
                        ejex = ejeGrafica.toArray(new String[0]);
                        System.out.println(ejeGrafica);
                        series1.setName(vector.get(x)+"");
                        series2.setName(vector.get(x)+"");
                        series3.setName(vector.get(x)+"");
                        series4.setName(vector.get(x)+"");
                        incluirDatos(series1, series2, series3, series4, ejex, valuesCSV, cont);
                        cont++;
                        //lista.clear();
                    }
                    graficaYAxis.setAutoRanging(false);
                    if (TempGraficaRadio.isSelected()) {
                        rangoGraficas.getData().add(series1);
                        rangoGraficas.setCreateSymbols(false);
                        graficaYAxis.setTickUnit(((limTempmax)-(limTempmin))/20);
                        graficaYAxis.setUpperBound(limTempmax);
                        graficaYAxis.setLowerBound(limTempmin);
                        unidades_csv(UnidadTemp);
                    }
                    if(HumGraficaRadio.isSelected()) {
                        rangoGraficas.getData().add(series2);
                        rangoGraficas.setCreateSymbols(false);
                        graficaYAxis.setTickUnit(((limHummax)-(limHummin))/20);
                        graficaYAxis.setUpperBound(limHummax);
                        graficaYAxis.setLowerBound(limHummin);
                        unidades_csv(UnidadHum);
                    }
                    if(CO2GraficaRadio.isSelected()) {
                        rangoGraficas.getData().add(series3);
                        rangoGraficas.setCreateSymbols(false);
                        graficaYAxis.setTickUnit(((limCO2max)-(limCO2min))/20);
                        graficaYAxis.setUpperBound(limCO2max);
                        graficaYAxis.setLowerBound(limCO2min);
                        unidades_csv(UnidadCO2);
                    }

                    if (UVGraficaRadio.isSelected()) {
                        rangoGraficas.getData().add(series4);
                        rangoGraficas.setCreateSymbols(false);
                        graficaYAxis.setTickUnit(((14)-(0))/14);
                        graficaYAxis.setUpperBound(14);
                        graficaYAxis.setLowerBound(0);
                        unidades_csv(UnidadUV);
                    }
                    limitesGraficas(valuesCSV, false);
                }   catch (FileNotFoundException e) { e.printStackTrace(); }
                try {
                    archivo = new FileReader (fileNameSuelo);
                    Scanner inputStream = new Scanner(archivo);
                    while (inputStream.hasNext()) {
                        String data = inputStream.next();
                        valuesCSVSuelo = data.split(",");
                        limitesSuelo.add(Double.parseDouble(valuesCSVSuelo[2]));
                        series5.setName(vector.get(x)+"");
                        ejeGraficaSuelo.add(String.valueOf(contSuelo));
                        ejexSuelo = ejeGraficaSuelo.toArray(new String[0]);
                        basededatosListaSuelo.addAll(new baseTablaSuelo((valuesCSVSuelo[0]), (valuesCSVSuelo[1]), Double.parseDouble(valuesCSVSuelo[2]),
                                Double.parseDouble(valuesCSVSuelo[7]), Double.parseDouble(valuesCSVSuelo[8])));
                        series5.getData().add(new XYChart.Data<>(ejexSuelo[contSuelo], Double.parseDouble(valuesCSVSuelo[2])));
                        contSuelo++;
                        //lista.clear();
                    }
                    Suelografica.getData().add(series5);
                    Suelografica.setCreateSymbols(false);
                    SueloYAxis.setAutoRanging(false);
                    limSuelomax = limitesSuelo.stream().mapToDouble(i -> i).max().getAsDouble()+1;
                    limSuelomin = limitesSuelo.stream().mapToDouble(i -> i).min().getAsDouble()-1;
                    SueloYAxis.setTickUnit(((limSuelomax)-(limSuelomin))/20);
                    SueloYAxis.setUpperBound(limSuelomax);
                    SueloYAxis.setLowerBound(limSuelomin);

                }   catch (FileNotFoundException e) { e.printStackTrace(); }}
            if (vector.get(x) == ComboFechaDataDos.getValue().toString()) leer_csv = false;
        }
        basededatosTabla.setItems(basededatosLista);
        SueloTabla.setItems(basededatosListaSuelo);
    }

    public void incluirDatos(XYChart.Series series1, XYChart.Series series2, XYChart.Series series3, XYChart.Series series4,
                             String[] ejeX, String[] ejeY, int posicion){
        basededatosLista.addAll(new baseTabla((ejeY[0]), (ejeY[1]), Double.parseDouble(ejeY[2]), Double.parseDouble(ejeY[3]),
                Double.parseDouble(ejeY[4]), Double.parseDouble(ejeY[5]), Double.parseDouble(ejeY[6]), Double.parseDouble(ejeY[7])));

        series1.getData().add(new XYChart.Data<>(ejeX[posicion], Double.parseDouble(ejeY[2])));
        series2.getData().add(new XYChart.Data<>(ejeX[posicion], Double.parseDouble(ejeY[3])));
        series3.getData().add(new XYChart.Data<>(ejeX[posicion], Double.parseDouble(ejeY[4])));
        series4.getData().add(new XYChart.Data<>(ejeX[posicion], Double.parseDouble(ejeY[5])));
    }

    public void graficarRadioButton(javafx.event.ActionEvent event){
        graficaYAxis.setAutoRanging(false);
        leer_csv();
    }
    public void limitesGraficas(String[] valores, boolean añadir){
        if(añadir){
            limitesTemp.add(Double.parseDouble(valores[2]));
            limitesHum.add(Double.parseDouble(valores[3]));
            limitesCO2.add(Double.parseDouble(valores[4]));
            limitesUV.add(Double.parseDouble(valores[5]));
        }
        else{
            limTempmax = limitesTemp.stream().mapToDouble(i -> i).max().getAsDouble()+5;
            limTempmin = limitesTemp.stream().mapToDouble(i -> i).min().getAsDouble()-1;
            TemYAxis.setTickUnit(((limTempmax)-(limTempmin))/20);
            TemYAxis.setUpperBound(limTempmax);
            TemYAxis.setLowerBound(limTempmin);

            limHummax = limitesHum.stream().mapToDouble(i -> i).max().getAsDouble()+1;
            limHummin = limitesHum.stream().mapToDouble(i -> i).min().getAsDouble()-1;
            HumYAxis.setTickUnit(((limHummax)-(limHummin))/20);
            HumYAxis.setUpperBound(limHummax);
            HumYAxis.setLowerBound(limHummin);

            limCO2max = limitesCO2.stream().mapToDouble(i -> i).max().getAsDouble()+10;
            limCO2min = limitesCO2.stream().mapToDouble(i -> i).min().getAsDouble()-10;
            CO2YAxis.setTickUnit(((limCO2max)-(limCO2min))/20);
            CO2YAxis.setUpperBound(limCO2max);
            CO2YAxis.setLowerBound(limCO2min);

            limUVmax = limitesUV.stream().mapToDouble(i -> i).max().getAsDouble()+1;
            limUVmin = limitesUV.stream().mapToDouble(i -> i).min().getAsDouble()-1;
            UVYAxis.setTickUnit(((14)-(0))/14);
            UVYAxis.setUpperBound(14);
            UVYAxis.setLowerBound(0);
        }
    }

    /**TIEMPO REAL**/
    public void botonInicio(MouseEvent event){
        JFXComboBox [] combos = {ComboFechaData, ComboFechaDataDos, ComboFechaGrafica, ComboFechaGraficaDos, ComboFechaAnalisis, ComboFechaAnalisisDos};
        JFXButton [] botones = {BotonAceptarData, BotonAceptarGraficas, BotonAceptarAnalisis, DiagramaBoton, TablaestadisticaBoton};
        JFXRadioButton [] radios = {TempRadioButton, HumRadioButton, CO2RadioButton, UVRadioButton};
        LineChart [] lineas = {TemGrafica, HumGrafica, CO2Grafica, UVGrafica, Suelografica};
        XYChart.Series [] series_real = {series1_real, series2_real,series3_real, series4_real, serie_suelo_real};
        /**TIEMPO REAL**/
        rangograficasPanel.setVisible(false);
        realgraficasPanel.setVisible(true);
        mh1.start("Prueba Hilo 1");

        for(LineChart linea:lineas) linea.getData().clear();
        for(XYChart.Series serie:series_real) serie.getData().clear();

        for(int i=0;i<lineas.length;i++){
            lineas[i].getData().add(series_real[i]);
            lineas[i].setCreateSymbols(false);
        }
        TemYAxis.setAutoRanging(false);
        HumYAxis.setAutoRanging(false);
        CO2YAxis.setAutoRanging(false);
        UVYAxis.setAutoRanging(false);
        SueloYAxis.setAutoRanging(false);
        /*if(mh1.isError()){
            ErrorPanel.setVisible(true);
            inicioPanel.setDisable(true);
            menuPanel.setDisable(true);
        }*/
        //else{
        BotonDetener.setDisable(false);
        BotonComenzar.setDisable(true);

        for(JFXComboBox combo:combos) combo.setDisable(true);
        for(JFXButton boton:botones) boton.setDisable(true);
        for(JFXRadioButton radio:radios) radio.setDisable(true);

        start("Prueba Hilo 2");
        BotonComenzar.setVisible(false);
        BotonDetener.setVisible(true);

            //longitud = mh1.getLongitud();
        //}
    }
    public void botonFin(MouseEvent event){
        try{
            Thread.sleep(1000);
            mh1.stop();
            Thread.sleep(1000);
            stopThread = true;
        }catch (InterruptedException exception){
            System.out.println("Error Controller: " + exception);
        }
        rangograficasPanel.setVisible(true);
        realgraficasPanel.setVisible(false);

        BotonDetener.setDisable(true);
        BotonComenzar.setDisable(false);
        ComboFechaData.setDisable(false);
        ComboFechaGrafica.setDisable(false);
        ComboFechaAnalisis.setDisable(false);

        BotonDetener.setVisible(false);
        BotonComenzar.setVisible(true);
    }
    public void start(String nombre){
        hilo = new Thread(this,nombre);
        stopThread = false;
        hilo.start();
    }
    public void BotonError(javafx.event.ActionEvent event){
        menuPanel.setDisable(false);
        inicioPanel.setDisable(false);
        ErrorPanel.setVisible(false);
    }

    /**MAPAS**/
    public void BotonFechaMapas(javafx.event.ActionEvent event){
        variablesMapas.setDisable(false);
    }
    public void clasificar_mapa(String fileName, int columna_lat, int columna_lon, int columna, double [] rangos){
        Mercator mercator = new EllipticalMercator();
        ArrayList<Double> datosMapas = new ArrayList<>();
        XYChart.Series [] series = {serie_naranja, serie_amarillo, serie_azul1, serie_azul2, serie_verde1, serie_verde2, serie_rosa, serie_rojo};

        for(XYChart.Series serie:series) serie.getData().clear();
        Mapas.getData().clear();
        FileReader archivo = null;

        try {
            archivo = new FileReader(fileName);
            Scanner inputStream = new Scanner(archivo);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                valuesCSV = data.split(",");
                datosMapas.add(Double.parseDouble(valuesCSV[columna]));
                double resultx=mercator.xAxisProjection(Double.parseDouble(valuesCSV[columna_lon]));
                double resulty=mercator.yAxisProjection(Double.parseDouble(valuesCSV[columna_lat]));
                latitud.add(Double.parseDouble(valuesCSV[columna_lat]));
                longitud.add(Double.parseDouble(valuesCSV[columna_lon]));
                if (datosMapas.get(datosMapas.size() - 1) <= rangos[0]) {
                    serie_naranja.getData().add(new XYChart.Data(resultx, resulty, 5));
                } else if (datosMapas.get(datosMapas.size() - 1) > rangos[0] &&
                        datosMapas.get(datosMapas.size() - 1) <= rangos[1]) {
                    serie_amarillo.getData().add(new XYChart.Data(resultx, resulty, 5));
                } else if (datosMapas.get(datosMapas.size() - 1) > rangos[1] &&
                        datosMapas.get(datosMapas.size() - 1) <= rangos[2]) {
                    serie_azul1.getData().add(new XYChart.Data(resultx, resulty, 5));
                } else if (datosMapas.get(datosMapas.size() - 1) > rangos[2] &&
                        datosMapas.get(datosMapas.size() - 1) <= rangos[3]) {
                    serie_azul2.getData().add(new XYChart.Data(resultx, resulty, 5));
                } else if (datosMapas.get(datosMapas.size() - 1) > rangos[3] &&
                        datosMapas.get(datosMapas.size() - 1) <= rangos[4]) {
                    serie_verde1.getData().add(new XYChart.Data(resultx, resulty, 5));
                } else if (datosMapas.get(datosMapas.size() - 1) > rangos[4] &&
                        datosMapas.get(datosMapas.size() - 1) <= rangos[5]) {
                    serie_verde2.getData().add(new XYChart.Data(resultx, resulty, 5));
                } else if (datosMapas.get(datosMapas.size() - 1) > rangos[5] &&
                        datosMapas.get(datosMapas.size() - 1) <= rangos[6]) {
                    serie_rosa.getData().add(new XYChart.Data(resultx, resulty, 5));
                } else {
                    serie_rojo.getData().add(new XYChart.Data(resultx, resulty, 5));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i=0;i<series.length;i++) System.out.println(series[i].getData());
        System.out.println(datosMapas);
        double max_x = mercator.xAxisProjection(longitud.stream().mapToDouble(i -> i).max().getAsDouble());
        double min_x = mercator.xAxisProjection(longitud.stream().mapToDouble(i -> i).min().getAsDouble());
        double max_y = mercator.yAxisProjection(latitud.stream().mapToDouble(i -> i).max().getAsDouble());
        double min_y = mercator.yAxisProjection(latitud.stream().mapToDouble(i -> i).min().getAsDouble());
        System.out.println(max_x);
        System.out.println(min_x);
        System.out.println(max_y);
        System.out.println(min_y);

        Mapas.getData().addAll(series);
        mapasYAxis.setAutoRanging(false);
        mapasXAxis.setAutoRanging(false);

        mapasXAxis.setTickUnit(((max_x)-(min_x)));
        mapasYAxis.setTickUnit(((max_y)-(min_y)));

        mapasXAxis.setUpperBound(max_x + max_x*0.00005);
        mapasXAxis.setLowerBound(min_x - min_x*0.00005);
        mapasYAxis.setUpperBound(max_y + max_y*0.00005);
        mapasYAxis.setLowerBound(min_y - min_y*0.00005);
    }
    public void CrearMapa(int columna, double [] rangos, boolean suelo){

        for(int x = 0; x < archivos.length; x++) {
            if (vector.get(x) == ComboMapas.getValue().toString()) {
                if(suelo){
                    int columna_lat = 7; //acomodar aqui 3 con la latitud del suelo
                    int columna_lon = 8; //acomodar aqui 4 con la longitud del suelo
                    String fileName = ruta2 + vector.get(x) + ".csv";
                    clasificar_mapa(fileName, columna_lat, columna_lon, columna, rangos);

                } else{
                    int columna_lat = 7;
                    int columna_lon = 8;
                    String fileName = ruta1 + vector.get(x) + ".csv";
                    clasificar_mapa(fileName, columna_lat, columna_lon, columna, rangos);
                }
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LineChart [] graficas = {TemGrafica, HumGrafica, CO2Grafica, UVGrafica, rangoGraficas, Suelografica};
        JFXRadioButton [] botones = {TempRadioButton, HumRadioButton, CO2RadioButton, SueloRadioButton, UVRadioButton};
        JFXRadioButton [] botones_graficas = {TempGraficaRadio, HumGraficaRadio, CO2GraficaRadio, UVGraficaRadio};
        XYChart.Series [] series_mapa = {serie_naranja, serie_amarillo, serie_azul1, serie_azul2, serie_verde1, serie_verde2, serie_rosa, serie_rojo};

        String sistema = System.getProperty("os.name");

        DiagramaEstadis.setAnimated(false);
        Mapas.setAnimated(false);
        Mapas.getData().addAll(series_mapa);

        for(LineChart grafica:graficas) grafica.setAnimated(false);

        /**CREAR GRUPO PARA RadioButton*/
        ToggleGroup group = new ToggleGroup();
        for(JFXRadioButton boton:botones) boton.setToggleGroup(group);

        ToggleGroup groupGraficas = new ToggleGroup();
        for(JFXRadioButton boton:botones_graficas) boton.setToggleGroup(groupGraficas);

        File carpeta1 = null;
        File carpeta2 = null;

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
        carpeta1 = new File(ruta1);
        if(!carpeta1.exists()) new File(ruta1).mkdirs();

        carpeta2 = new File(ruta2);
        if(!carpeta2.exists()) new File(ruta2).mkdir();

        CsvWriter csvInicial = new CsvWriter(ruta1 + for_fecha.format(fecha) + ".csv") ;
        String[] datoInicial = {};
        try{
            csvInicial.writeRecord(datoInicial);
        } catch (IOException e) {
            e.printStackTrace();
        }
        csvInicial.close();

        CsvWriter csvInicial2 = new CsvWriter(ruta2 + for_fecha.format(fecha) + ".csv") ;
        try{
            csvInicial.writeRecord(datoInicial);
        } catch (IOException e) {
            e.printStackTrace();
        }
        csvInicial2.close();

        archivos = carpeta1.listFiles();
        String fileCombo = "";
        if (archivos == null || archivos.length == 0) return;
        else {
            for (int i = 0; i < archivos.length; i++) {
                //System.out.println("Hay CSV's en la carpeta");
                File archivo = archivos[i];
                String nombre = archivo.getName();
                if (nombre.charAt(nombre.length() - 1) == 'v' && nombre.charAt(nombre.length() - 2) == 's'
                        && nombre.charAt(nombre.length() - 3) == 'c') {
                    for (int j = 0; j < nombre.length()-4; j++) fileCombo = fileCombo + nombre.charAt(j);
                    vector.add(fileCombo);
                    //System.out.println(nombre);
                    fileCombo = "";
                }
            }
        }
		mh1.setBaseDatos(vector);

        ObservableList<String> comboIDContent = FXCollections.observableArrayList(vector);
        JFXComboBox [] combos = {ComboFechaData, ComboFechaAnalisis, ComboFechaGrafica, ComboMapas, ComboFechaSuelo};

        for(JFXComboBox combo:combos) combo.setItems(comboIDContent);

        TableColumn [] columnas = {tempColumna, humColumna, coColumna, uvColumna, latColumna, logColumna};
        String [] nombre_columnas = {"tempColumna", "humColumna", "coColumna", "uvColumna", "latColumna", "logColumna"};

        diaColumna.setCellValueFactory(new PropertyValueFactory<baseTabla, String>("diaColumna"));
        horaColumna.setCellValueFactory(new PropertyValueFactory<baseTabla, String>("horaColumna"));
        for(int i=0; i<columnas.length; i++) columnas[i].setCellValueFactory(new PropertyValueFactory<baseTabla,Double>(nombre_columnas[i]));

        TableColumn [] columnas_est = {mediaAnalisis, medianaAnalisis, modaAnalisis, minAnalisis, maxAnalisis, rangoAnalisis, desEstandarAnalisis, varianzaAnalisis};
        String [] nombre_columnas_est = {"mediaAnalisis", "medianaAnalisis", "modaAnalisis", "minAnalisis", "maxAnalisis", "rangoAnalisis", "desEstandarAnalisis", "varianzaAnalisis"};

        diaAnalisis.setCellValueFactory(new PropertyValueFactory<baseTablaAnalisis, String>("diaAnalisis"));
        for(int i=0; i<columnas_est.length; i++) columnas_est[i].setCellValueFactory(new PropertyValueFactory<baseTablaAnalisis,Double>(nombre_columnas_est[i]));

        diaSColumna.setCellValueFactory(new PropertyValueFactory<baseTablaSuelo, String>("diaSColumna"));
        horaSColumna.setCellValueFactory(new PropertyValueFactory<baseTablaSuelo, String>("horaSColumna"));
        HumeSueColumna.setCellValueFactory(new PropertyValueFactory<baseTablaSuelo, Double>("HumeSueColumna"));
        latSColumna.setCellValueFactory(new PropertyValueFactory<baseTablaSuelo, Double>("latSColumna"));
        longSColumna.setCellValueFactory(new PropertyValueFactory<baseTablaSuelo, Double>("longSColumna"));

    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] ejex;
        String[] ejex_suelo;
        int dato = 0;
        int dato_suelo = 0;
        longitud = mh1.getLongitud();
        hum_suelo = mh1.getHum_suelo();
        j = hum_suelo.size();
        i = longitud.size();
        i++;
        j++;
        //mh1.setSp(puertos);
        while (!stopThread) {
            try {
                Thread.sleep(600);
                //System.out.println("While de la tabla");
                System.out.println(i);
                System.out.println(longitud.size());
                if(hum_suelo.size() >= j){
                    System.out.println("Suelo");
                    j++;
                    hora_med = mh1.getHora_med();
                    Basededatos_suelo.add(new CSV_suelo(for_fecha.format(fecha), hora_med.get(hora_med.size() - 1), hum_suelo.get(hum_suelo.size() - 1),
                            latitud.get(latitud.size() - 1), longitud.get(longitud.size() - 1)));
                    basededatosListaSuelo.clear();
                    for (CSV_suelo csv : Basededatos_suelo){
                        ejeGraficaSuelo.add(String.valueOf(dato_suelo));
                        ejex_suelo = ejeGraficaSuelo.toArray(new String[0]);
                        values = csv.getArray();
                        limitesSuelo.add(Double.parseDouble(values[2]));
                        basededatosListaSuelo.addAll(new baseTablaSuelo(values[0], values[1], Double.parseDouble(values[2]),
                                Double.parseDouble(values[3]), Double.parseDouble(values[4])));
                        serie_suelo_real.getData().add(new XYChart.Data<>(ejex_suelo[dato_suelo], Double.parseDouble(values[2])));
                        dato_suelo++;
                    }
                    dato_suelo = 0;
                    ejeGraficaSuelo.clear();
                    SueloTabla.setItems(basededatosListaSuelo);
                    limSuelomax = limitesSuelo.stream().mapToDouble(i -> i).max().getAsDouble()+1;
                    limSuelomin = limitesSuelo.stream().mapToDouble(i -> i).min().getAsDouble()-1;
                    SueloYAxis.setTickUnit(((limSuelomax)-(limSuelomin))/20);
                    SueloYAxis.setUpperBound(limSuelomax);
                    SueloYAxis.setLowerBound(limSuelomin);
                }
                System.out.println("Previo mediciones");
                while (longitud.size() < i) {
                    temp = mh1.getTemp();
                    hum = mh1.getHum();
                    co2 = mh1.getCo2();
                    ind_uv = mh1.getInd_uv();
                    latitud = mh1.getLatitud();
                    longitud = mh1.getLongitud();
                    Thread.sleep(500);
                }
                System.out.println("Todas mediciones ok");
                i++;
                hora_med = mh1.getHora_med();
                Basededatos.add(new CSV(for_fecha.format(fecha), hora_med.get(hora_med.size() - 1), temp.get(temp.size() - 1),
                        hum.get(hum.size() - 1), co2.get(co2.size() - 1), ind_uv.get(ind_uv.size() - 1), latitud.get(latitud.size() - 1),
                        longitud.get(longitud.size() - 1)));
                System.out.println("Añadir datos ok");
                basededatosLista.clear();
                for (CSV csv : Basededatos) {
                    ejeGrafica.add(String.valueOf(dato));
                    ejex = ejeGrafica.toArray(new String[0]);
                    values = csv.getArray();
                    limitesGraficas(values, true);
                    incluirDatos(series1_real, series2_real, series3_real, series4_real, ejex, values, dato);
                    dato++;
                }
                System.out.println("Añadir series ok");
                dato = 0;
                ejeGrafica.clear();
                Thread.sleep(500);
                //System.out.println(basededatosLista);
                basededatosTabla.setItems(basededatosLista);
                limitesGraficas(values, false);
                System.out.println("Imprimir tabla ok");
            } catch (Exception e) {
                //System.out.println("Error Hilo tabla: " + e);
                Thread.currentThread().interrupt();
            }
        }
    }
    }


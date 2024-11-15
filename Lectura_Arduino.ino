#include <SoftwareSerial.h>
#include <DHT.h>
#include <TinyGPS.h>

#define RX_CO2 10
#define TX_CO2 9
#define HUM_SUELO_PIN A0
#define DHT_PIN 11     
#define DHT_TYPE DHT22
#define UV_PIN A1
#define RX_GPS 4
#define TX_GPS 6

#define ESPERA_ENTRE_LECTURAS 100 // Leer cada 100 ms
#define ESPERA_ENTRE_PRESENTACIONES 1000 // Mostrar el índice cada 30 s
#define CANTIDAD_INDICES_UV 11

TinyGPS gps;
SoftwareSerial gpsSerial(RX_GPS, TX_GPS); // Creación de comunicación con GPS
DHT dht(DHT_PIN, DHT_TYPE); // Creación de comunicación con sensor DHT22
SoftwareSerial co2Serial(RX_CO2, TX_CO2); // Creación de comunicación serial con sensor MH-Z19B

/* Variables sensor MH-Z19B: */
const long tiempo_precalentamiento = 100;
int co2Inicial = 400;

/* Variables sensor Humedad del Suelo Capacitivo */
int hum_min = 516, hum_max = 256, lect_hum_suelo = 0;
float hum_suelo;

/* Variables sensor DHT22 */
//float hum, temp;

/* Variables sensor UV */
unsigned int lectura_sensor,contador_lecturas=1;
float media_lecturas, total_lecturas=0.0;
int valor_indice_uv[CANTIDAD_INDICES_UV]={210,295,378,467,563,646,738,818,907,1003,1022}; // De 1 a 11
int indice_uv;
byte indice;
boolean buscando_indice_uv;
long cronometro_lecturas,cronometro_presentaciones, tiempo_transcurrido;

/* Variables GPS */
float flat, flon;

void setup() {
  Serial.begin(9600);
  co2Serial.begin(9600);
  gpsSerial.begin(9600);
  dht.begin(); // Inicialización comunicación con sensor DHT22

  lectura_sensor = analogRead(UV_PIN); // La primera lectura es incorrecta (normalmente cero)
  cronometro_lecturas = millis(); // Esperar un ciclo de lectura para estabilizar el sensor y la entrada analógica
  cronometro_presentaciones = millis();
  
  if (precalentando()) {
    //Serial.print("Preheating");
    while (precalentando()) {
      //Serial.print(".");
      delay(5000);
    }
    //Serial.println();
  }
}

void loop() {
  Serial.println("Inicio");

  /* GPS*/
  gpsSerial.begin(9600);
  gpsSerial.listen();
  if (Validar_GPS()){
      gps.f_get_position(&flat, &flon);
      Serial.println("l");
      Serial.println(flat == TinyGPS::GPS_INVALID_F_ANGLE ? 0.0 : flat, 6);
      delay(1000);
      Serial.println("g");
      Serial.println(flon == TinyGPS::GPS_INVALID_F_ANGLE ? 0.0 : flon, 6); 
   } 
   else {
    Serial.println("l");
    Serial.println(-6);
    delay(1000);
    Serial.println("g");
    Serial.println(-7);
    }
    gpsSerial.end();
    delay(1000);
    
  /* SENSOR DHT22 */
  float hum = dht.readHumidity();
  float temp = dht.readTemperature();

  Serial.println("t");
  if (isnan(temp)) {
    Serial.println(-1);
  }
  else {
    Serial.println(temp);
  }
  delay(1000);

  Serial.println("h");
  if (isnan(hum)) {
    Serial.println(-2);
  }
  else {
    Serial.println(hum);
  }
  delay(1000);
  
  /*SENSOR MH-Z19B*/
  int lect_CO2;
  co2Serial.listen();
  lect_CO2 = leerco2UART();
  Serial.println("c");
  if(lect_CO2 > 0){
    Serial.println(lect_CO2);
  } else {
    Serial.println(-3);
  }
  delay(1000);
  
  /* SENSOR HUMEDAD DEL SUELO CAPACITIVO */
  lect_hum_suelo = analogRead(HUM_SUELO_PIN); // Lectura analógica del sensor
  hum_suelo = 6000000*(pow(lect_hum_suelo,-2.149)); // Conversión lectura a %Humedad
  Serial.println("s");
  if (isnan(hum_suelo)){
    Serial.println(-4);
  } else if(hum_suelo < 0) {
    hum_suelo = 0.00;
    Serial.println(hum_suelo);
  } else if(hum_suelo > 100){
    hum_suelo = 100;
    Serial.println(hum_suelo);
  } else{
    Serial.println(hum_suelo);
  }
  
  delay(1000);
  
  /* SENSOR UV */
  indice_uv = lectura_uv();
  Serial.println("u");
  if(isnan(indice_uv)){
    Serial.println(-5);
  }
  else{
    Serial.println(indice_uv);
  }
  delay(1000);
  
}
/* BLOQUES DE FUNCIONES PARA SENSOR MH-Z19B*/
boolean precalentando(){
  return millis() < (tiempo_precalentamiento);
}

int leerco2UART(){
  byte cmd[9] = {0xFF, 0x01, 0x86, 0x00, 0x00, 0x00, 0x00, 0x00, 0x79};
  byte response[9];
  co2Serial.write(cmd,9);
  memset(response, 0, 9);
  int waited = 0;
  while (co2Serial.available() == 0) {
    //if (debug) Serial.print(".");
    delay(100);  // wait a short moment to avoid false reading
    if (waited++ > 10) {
      //if (debug) Serial.println(F("No response after 10 seconds"));
      co2Serial.flush();
      return -1;
    }
  }
  boolean skip = false;
  while (co2Serial.available() > 0 && (unsigned char)co2Serial.peek() != 0xFF) {
    if (!skip) {
     // Serial.print(F("MHZ: - skipping unexpected readings:"));
      skip = true;
    }
    //Serial.print(" ");
    //Serial.print(co2Serial.peek(), HEX);
    co2Serial.read();
  }
  if (skip) Serial.println();
  if (co2Serial.available() > 0) {
    int count = co2Serial.readBytes(response, 9);
    if (count < 9) {
      co2Serial.flush();
      //Serial.println("Lectura incompleta");
      return -2;
    }
  } else {
    co2Serial.flush();
    return -2;
  }
  
  //Serial.print(F("  << "));
  for (int i = 0; i < 9; i++) {
    //Serial.print(response[i], HEX);
    //Serial.print(F("  "));
  }
  //Serial.println(F(""));

  byte check = getCheckSum(response);
  if (response[8] != check) {
    //Serial.println(F("MHZ: Checksum not OK!"));
    //Serial.print(F("MHZ: Received: "));
    //Serial.println(response[8], HEX);
    //Serial.print(F("MHZ: Should be: "));
    //Serial.println(check, HEX);
    //temperature = STATUS_CHECKSUM_MISMATCH;
    co2Serial.flush();
    return -3;
  }
  int ppm_uart = 256 * (int)response[2] + response[3];
  byte status = response[5];
  if (status != 0) {
    //Serial.print(F(" ! Status maybe not OK ! "));
    //Serial.println(status, HEX);
  } else {
    //Serial.print(F(" Status  OK: "));
    //Serial.println(status, HEX);
  }
  co2Serial.flush();
  return ppm_uart;
}

byte getCheckSum(byte* packet) {
//  if (!SerialConfigured) {
//    if (debug) Serial.println(F("-- serial is not configured"));
//    return STATUS_SERIAL_NOT_CONFIGURED;
//  }
//  if (debug) Serial.println(F("  getCheckSum()"));
  byte i;
  unsigned char checksum = 0;
  for (i = 1; i < 8; i++) {
    checksum += packet[i];
  }
  checksum = 0xff - checksum;
  checksum += 1;
  return checksum;
}

/* BLOQUES DE FUNCIONES SENSOR HUMEDAD DEL SUELO*/
float mapfloat(long x, long in_min, long in_max, long out_min, long out_max){
 return (float)(x - in_min) * (out_max - out_min) / (float)(in_max - in_min) + out_min;
}
/* BLOQUES DE FUNCIONES SENSOR UV*/
int lectura_uv(){
  tiempo_transcurrido=millis()-cronometro_lecturas;
  if(tiempo_transcurrido>ESPERA_ENTRE_LECTURAS)
  {
    cronometro_lecturas=millis();
    lectura_sensor=analogRead(UV_PIN);
    total_lecturas+=lectura_sensor;
    media_lecturas=total_lecturas/contador_lecturas++;
  }
  tiempo_transcurrido=millis()-cronometro_presentaciones;
  if(tiempo_transcurrido>ESPERA_ENTRE_PRESENTACIONES)
  {
    cronometro_presentaciones=millis();
    buscando_indice_uv=true;
    indice=CANTIDAD_INDICES_UV;
    while(buscando_indice_uv&&indice>0)
    {
      indice--;
      if(media_lecturas>valor_indice_uv[indice])
      {
        buscando_indice_uv=false;
      }
    }
    contador_lecturas=1;
    total_lecturas=0.0;
  }
  return indice;
}

/* BLOQUES DE FUNCIONES GPS */
bool Validar_GPS(){
 bool newData = false;
   
   // Intentar recibir secuencia durante un segundo
   for (unsigned long start = millis(); millis() - start < 1000;)
   {
      while (gpsSerial.available())
      {
         char c = gpsSerial.read();
         if (gps.encode(c)) {// Nueva secuencia recibida
            newData = true;
            return newData;
            }
      }  
   }
   return newData;
}

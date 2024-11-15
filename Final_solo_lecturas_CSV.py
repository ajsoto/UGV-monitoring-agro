#Importar librerias
import RPi.GPIO as GPIO
import time as time
import Adafruit_DHT
import CO2
import Sensor_UV as uv
from ADS1x15 import ADS1015
import pandas as pd

GPIO.setmode(GPIO.BCM) #Pines de raspberry en modo BCM

ADC = ADS1015() #Declarar ADS1015, para el caso de ADS1115 "ADS1115()"
GAIN = 1 #Ganancia del ADS1015, para medir voltajes de hasta 4,096V con resolucion de 0,1250mV

DHT_SENSOR = Adafruit_DHT.DHT22 #Utilizar DHT22, para el caso de DHT11 "Adafruit_DHT.DHT11"

#Pines de lectura
DHT_PIN = 17
HUM_SUELO_PIN = 0
INDICE_UV_PIN = 1

#Variables iniciales para sensor humedad de suelo
hum_min = 1270
hum_max = 645

medidas = [0, 0]
suma = 0

#Listas de lecturas
datos_temp = []
datos_hum = []
datos_CO2 = []
datos_temp_CO2 = []
datos_hum_suelo = []
datos_uv = []
datos_hora = []
n_medicion = []
n = 1

while True:
    #Hora de las mediciones
    datos_hora.append(time.strftime("%H:%M:%S"))
    
    #Lectura variables
    hum, temp = Adafruit_DHT.read_retry(DHT_SENSOR, DHT_PIN)
    lect_CO2 = CO2.leer_CO2()
    lect_temp = CO2.leer_temp()
    
    #Lectura de variables que requieren ADC
    for i in range(2):
        if i == HUM_SUELO_PIN:
            medidas[0] = ADC.read_adc(HUM_SUELO_PIN, GAIN)
        elif i == INDICE_UV_PIN:
            for j in range(0,2048):
                sensorValue = ADC.read_adc(INDICE_UV_PIN, gain=GAIN)
                suma = sensorValue + suma
            medidas[1] = suma/2048
    
    suma = 0
    lect_indice_uv = uv.leer_indice_uv(medidas[1])
    lect_hum_suelo = medidas[0]
    
    #Calculo humedad del suelo
    m = (0-100)/(hum_min-hum_max)
    hum_suelo = m*(lect_hum_suelo-hum_min)
    
    #Impresion de variables medidas
    if hum is not None and temp is not None:
        print("Temperatura: " + str(round(temp,3)) + str('°C'))
        print("Humedad: " + str(round(hum,3)) + str('%'))
    else:
        print("Error en lectura de Humedad y Temperatura")
    
    if lect_hum_suelo is not None:
        print("Humedad del suelo: " + str(hum_suelo) + str('%'))
    else:
        print("Error en lectura de humedad del suelo")
    
    if lect_indice_uv is not None:
        print("Indice UV: " + str(lect_indice_uv))
    else:
        print("Error en lectura del Indice Ultravioleta")
    
    print("CO2: " + str(lect_CO2))
    print("Temperatura (CO2): " + str(lect_temp))
    print()
    
    datos_temp.append(temp)
    datos_hum.append(hum)
    datos_CO2.append(lect_CO2)
    datos_temp_CO2.append(lect_temp)
    datos_hum_suelo.append(hum_suelo)
    datos_uv.append(lect_indice_uv)
    
    datos = {'Hora': datos_hora,
             'Temperatura (°C)': datos_temp,
             'Humedad relativa (%)': datos_hum,
             'CO2 (ppm)': datos_CO2,
             'Temperatura_CO2 (°C)': datos_temp_CO2,
             'Humedad del suelo (%)': datos_hum_suelo,
             'Indice_UV': datos_uv}
    
    n_medicion.append(n)
    
    datos_CSV = pd.DataFrame(datos, index = n_medicion)
    archivo = str(time.strftime("%d-%m-%y"))+str('.csv')
    
    datos_CSV.to_csv(str(archivo))
    
    print(datos_CSV)
    
    n = n + 1
    time.sleep(1)
import serial
import time as time
import pandas as pd

port1='/dev/ttyUSB0'
port2='/dev/serial0'

ser = serial.Serial(port1, 9600, timeout=2.0)
ser2 = serial.Serial(port2, 9600, timeout=2.0)

datos_hora = []
datos_temp = []
datos_hum = []
datos_CO2 = []
datos_hum_suelo = []
datos_uv = []
datos_lat = []
datos_lon = []
n_medicion = []
n = 1

leer = False

while True:
    x = ser.readline()
    print(x)
    if x == b'Inicio\r\n':
        leer = True
    while leer:   
        a = str(ser.readline())
        if a > 0:
            if a[2]=='1':
                datos_hora.append(time.strftime("%H:%M:%S"))
                temp = ser.readline()
                ser2.write(temp)
                datos_temp.append(str(temp)[2:-5])
                
            elif a[2]=='2':
                hum = ser.readline()
                ser2.write(hum)
                datos_hum.append(str(hum)[2:-5])
                
            elif a[2]=='3':
                co2 = ser.readline()
                ser2.write(co2)
                datos_CO2.append(str(co2)[2:-5])
                
            elif a[2]=='4':
                hum_suelo = ser.readline()
                ser2.write(hum_suelo)
                datos_hum_suelo.append(str(hum_suelo)[2:-5])
                
            elif a[2]=='5':
                ind_uv = ser.readline()
                ser2.write(ind_uv)
                datos_uv.append(str(ind_uv)[2:-5])
                
            elif a[2]=='6':                
                lat = ser.readline()
                ser2.write(lat)
                datos_lat.append(str(lat)[2:-5])
                
            elif a[2]=='7':                
                lon = ser.readline()
                ser2.write(lon)
                datos_lon.append(str(lon)[2:-5])
                      
                datos = {'Hora': datos_hora,
                 'Temperatura (°C)': datos_temp,
                 'Humedad relativa (%)': datos_hum,
                 'CO2 (ppm)': datos_CO2,
                 'Humedad del suelo (%)': datos_hum_suelo,
                 'Indice_UV': datos_uv,
                 'Latitud': datos_lat,
                 'Longitud': datos_lon }
            
                n_medicion.append(n)
                datos_CSV = pd.DataFrame(datos, index = n_medicion)
                
                archivo = str(time.strftime("%d-%m-%y"))+str('.csv')
                datos_CSV.to_csv(str(archivo))
            
                print(datos_CSV)
            
                n = n + 1
            else:
                print()
            print('Ok')
        
        

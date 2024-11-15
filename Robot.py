import RPi.GPIO as GPIO
import time as time
import pandas as pd
import os
import serial

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.cleanup()

port1='/dev/ttyUSB0'
port2='/dev/serial0'
corregir =False
trans = 3 #receptor 26
recep = 26
# ECNO A gpio 27 y 17 AQUI DICE ENCODER A NO ENABLE A
# Enco B gipo 10 y 22
#Motor a derecha
en_d = 18 
in3 = 24
in4 = 23        
enc_d = 10
pasos_d = 0
pasos_d_tot = 0

#Motor b
en_i = 12
in1 = 8
in2 = 25
enc_i = 27
pasos_i = 0
pasos_i_tot = 0

#Derecha
GPIO_TRIGGER_D = 11
GPIO_ECHO_D = 9
TARGET_D = 0

#Izquierda
GPIO_TRIGGER_I = 20
GPIO_ECHO_I = 21
TARGET_I = 0

#Frente
GPIO_TRIGGER_F = 6
GPIO_ECHO_F = 5
TARGET_F = 60
TARGET_F_GIRO = 0

giro_derecha = True
corrigiendo_izquierda = False
corrigiendo_derecha = False
#Base de datos
datos_fecha = []
datos_hora = []
datos_hum_suelo = []
datos_lon = []
datos_lat = []
datos = [datos_fecha, datos_hora, datos_hum_suelo, datos_lat, datos_lon]

contenido = os.listdir('/home/pi/Desktop/Datos/Suelo')
archivo = str('/home/pi/Desktop/Datos/Suelo') + str(time.strftime("%y-%m-%d"))+str('.csv')
nombre_archivo = str(time.strftime("%y-%m-%d"))+str('.csv')
nuevo = True
for folder in contenido:
    if folder == nombre_archivo:
        nuevo = False
        break

puente_h = [en_d, in1, in2, en_i, in3, in4]
for i in range(0,6,1):
    GPIO.setup(puente_h[i],GPIO.OUT)

GPIO.setup(trans,GPIO.OUT)
#GPIO.setup(19,GPIO.OUT)
GPIO.setup(recep,GPIO.IN)

GPIO.output(trans, False)
#GPIO.output(19, True)
#SERVOMOTOR Y SENSOR
servoPin = 13
GPIO.setup(servoPin,GPIO.OUT)
pwm_s = GPIO.PWM(servoPin,50)

pwm_D = GPIO.PWM(en_d,500)
pwm_I = GPIO.PWM(en_i,500)

pwm = [pwm_D, pwm_I, pwm_s]
for i in range(0,3,1):
    pwm[i].start(0)

GPIO.setup(GPIO_TRIGGER_D, GPIO.OUT)
GPIO.setup(GPIO_ECHO_D, GPIO.IN)
GPIO.setup(GPIO_TRIGGER_I, GPIO.OUT)
GPIO.setup(GPIO_ECHO_I, GPIO.IN)
GPIO.setup(GPIO_TRIGGER_F, GPIO.OUT)
GPIO.setup(GPIO_ECHO_F, GPIO.IN)

GPIO.setup(enc_d, GPIO.IN)
GPIO.setup(enc_i, GPIO.IN)

def my_callback_one(enc_d):
    global pasos_d, pasos_d_tot
    pasos_d += 1
    pasos_d_tot += 1
    #print('Pasos a: ' + str(pasos_d))

def my_callback_two(enc_i):
    global pasos_i, pasos_i_tot
    pasos_i += 1
    pasos_i_tot += 1
    
def Giro_Contra_Reloj_MotorB():
    GPIO.output(in3,False)
    GPIO.output(in4,True)
    
def Giro_Contra_Reloj_MotorA():
    GPIO.output(in1,False)
    GPIO.output(in2,True)
    
def Giro_Favor_Reloj_MotorB():
    GPIO.output(in3,True)
    GPIO.output(in4,False)
    
def Giro_Favor_Reloj_MotorA():
    GPIO.output(in1,True)
    GPIO.output(in2,False)

t_ult = 0.1
def distancia(GPIO_TRIGGER, GPIO_ECHO):
    # set Trigger to HIGH
    GPIO.output(GPIO_TRIGGER, True)

    # set Trigger after 0.01ms to LOW
    time.sleep(0.00001)
    GPIO.output(GPIO_TRIGGER, False)

    StartTime = time.time()
    StopTime = time.time()

    # save StartTime
    inicio = time.time()
    while GPIO.input(GPIO_ECHO) == 0:
        StartTime = time.time()
        if(StartTime - inicio > 0.025):
            return 400

    # save time of arrival
    while GPIO.input(GPIO_ECHO) == 1:
        StopTime = time.time()

    # time difference between start and arrival
    TimeElapsed = StopTime - StartTime
    # multiply with the sonic speed (34300 cm/s)
    # and divide by 2, because there and back
    distance = (TimeElapsed * 34300) / 2
    return distance

def leer_almacenar(datos, lista):
    global ser, ser2
    lect = ser.readline()
    if lista == 2:
        ser2.write(b's')
        ser2.write(lect[0:-2])
        ser2.write(b'n')
        datos[lista].append(str(lect)[2:-5])
    else:
        datos[lista].append(str(lect)[2:-5])
    return datos

n_giro = 1
def giro_90(TRIGGER_OBS, ECHO_OBS, TRIGGER_2, ECHO_2):
    global TARGET_F_GIRO, TARGET_F
    TARGET_2_GIRO = 0
    pwm_D.ChangeDutyCycle(0) 
    pwm_I.ChangeDutyCycle(0)
    for i in range(0,5,1):
        TARGET_F_GIRO += distancia(GPIO_TRIGGER_F, GPIO_ECHO_F) 
        #print(TARGET_F_GIRO)
        time.sleep(0.15)
    TARGET_F = TARGET_F_GIRO/5
    TARGET_F_GIRO = 0
    #print('TARGET_F', TARGET_F + 5)
    
    if giro_derecha:
        for i in range(0,5,1):
            TARGET_2_GIRO += distancia(GPIO_TRIGGER_D, GPIO_ECHO_D) 
            #print(TARGET_2_GIRO)
            time.sleep(0.15)
        pwm_D.ChangeDutyCycle(0) 
        pwm_I.ChangeDutyCycle(40)#60)
        tiempo_giro = 0.23
        
    else:
        for i in range(0,5,1):
            TARGET_2_GIRO += distancia(GPIO_TRIGGER_I, GPIO_ECHO_I) 
            #print(TARGET_2_GIRO)
            time.sleep(0.15)
        pwm_D.ChangeDutyCycle(40)#60) 
        pwm_I.ChangeDutyCycle(0)
        tiempo_giro = 0.23
        
    TARGET_2 = TARGET_2_GIRO/5
    
    #print('TARGET_2', TARGET_2 + 5)
    time.sleep(1.5)
    
    dist_obs = distancia(TRIGGER_OBS, ECHO_OBS)
    dist_2 = distancia(TRIGGER_OBS, ECHO_OBS)
    dist_f = distancia(GPIO_TRIGGER_F, GPIO_ECHO_F)
    time.sleep(t_ult)
    #print('Obstaculo', dist_obs)
    #print('No obstaculo', dist_2)
    #print('Frente', dist_f)
    
    if n_giro == 1:
        while dist_obs>TARGET_F+5 or dist_f > TARGET_2:
            dist_obs = distancia(TRIGGER_OBS, ECHO_OBS)
            dist_2 = distancia(TRIGGER_2, ECHO_2)
            dist_f = distancia(GPIO_TRIGGER_F, GPIO_ECHO_F)
            #print('Obstaculo', dist_obs)
            #print('No obstaculo', dist_2)
            #print('Frente', dist_f)
            #print()
            if(dist_obs < TARGET_F+5  and dist_f < TARGET_2):
                break
            time.sleep(t_ult)
        time.sleep(tiempo_giro)
    elif n_giro == 2:
        while dist_obs>TARGET_F:
            dist_obs = distancia(TRIGGER_OBS, ECHO_OBS)
            dist_2 = distancia(TRIGGER_2, ECHO_2)
            dist_f = distancia(GPIO_TRIGGER_F, GPIO_ECHO_F)
            #print('Obstaculo', dist_obs)
            #print('No obstaculo', dist_2)
            #print('Frente', dist_f)
            #print()
            if(dist_obs < TARGET_F):
                break
            time.sleep(t_ult)
        time.sleep(0.14)
    
    pasos_d = 0
    pasos_i = 0
    pasos_i_tot = 0
    pasos_d_tot = 0
    pwm_D.ChangeDutyCycle(0) 
    pwm_I.ChangeDutyCycle(0)
    time.sleep(1)
    pwm_D.ChangeDutyCycle(30) #45) 
    pwm_I.ChangeDutyCycle(40) #54)
    
    #time.sleep(1.5)

GPIO.add_event_detect(enc_d, GPIO.RISING)
GPIO.add_event_callback(enc_d, my_callback_one)
GPIO.add_event_detect(enc_i, GPIO.RISING)
GPIO.add_event_callback(enc_i, my_callback_two)

print('Iniciando desplazamiento')
# for i in range(0,30,1):
#     TARGET_I += distancia(GPIO_TRIGGER_I, GPIO_ECHO_I)
#     #print(TARGET_I)cv
#     TARGET_D += distancia(GPIO_TRIGGER_D, GPIO_ECHO_D)
#     time.sleep(0.15)

#TARGET_I = TARGET_I/30
#TARGET_D = TARGET_D/30
TARGET_I = 16
TARGET_D = 16
#print('Target izquierda:', TARGET_I)
#print('Target derecha:', TARGET_D)
time.sleep(0.5)
error_d = 0
error_i = 0

#duty_D = 45
#duty_I = 58
duty_D = 30#45
duty_I = 35#54

Giro_Contra_Reloj_MotorA()
Giro_Contra_Reloj_MotorB()

pwm_D.ChangeDutyCycle(duty_D) 
pwm_I.ChangeDutyCycle(duty_I)

try:
    while True:
        dist_i = distancia(GPIO_TRIGGER_I, GPIO_ECHO_I)
        dist_d = distancia(GPIO_TRIGGER_D, GPIO_ECHO_D)
        dist_f = distancia(GPIO_TRIGGER_F, GPIO_ECHO_F)
        if(dist_i == -1):
            dist_i = dist_prev_i
        else:
            dist_prev_i = dist_i
        if(dist_i == -1):
            dist_d = dist_prev_d
        else:
            dist_prev_d = dist_d
        if(dist_f == -1):
            dist_f = dist_prev_f
        else:
            dist_prev_f = dist_f
                
        print('Dist med izq:', dist_i, 'Dist med dcha:', dist_d, 'Dist med frente:', dist_f)
        if(dist_i > TARGET_I - error_i and dist_d > TARGET_D - error_d and dist_f > TARGET_F):
            duty_D = 40#75
            duty_I = 50#80
            print('Estado derecho')
            if corrigiendo_izquierda and dist_d > 20 and dist_d < 40:
                pwm_D.ChangeDutyCycle(0) 
                pwm_I.ChangeDutyCycle(0)
                time.sleep(0.5)
                pwm_D.ChangeDutyCycle(50) 
                pwm_I.ChangeDutyCycle(0)
                corrigiendo_izquierda = False
                time.sleep(0.8)
            else:
                corrigiendo_izquierda = False
            if corrigiendo_derecha and dist_i > 20 and dist_i < 40:
                pwm_D.ChangeDutyCycle(0) 
                pwm_I.ChangeDutyCycle(0)
                time.sleep(0.5)
                pwm_D.ChangeDutyCycle(0) 
                pwm_I.ChangeDutyCycle(50)
                corrigiendo_derecha = False
                time.sleep(0.8)
            else:
                corrigiendo_derecha = False
          
        elif(dist_i < TARGET_I - error_i and dist_d > TARGET_D - error_d and dist_f > TARGET_F):
            duty_D = 23
            duty_I = 55
            print('Estado corrigiendo izquierda')
            corrigiendo_izquierda = True
        
        elif(dist_i > TARGET_I - error_i and dist_d < TARGET_D - error_d and dist_f > TARGET_F):
            duty_D = 55
            duty_I = 28
            print('Estado corrigiendo derecha')
            corrigiendo_derecha = True
        
        elif(dist_i > TARGET_I - error_i and dist_d > TARGET_D - error_d and dist_f < TARGET_F):
            print('Estado Giro 90')
            print('..........................GIRO 1.............................')
            #print('Pasos tot izq:', pasos_i_tot, 'Pasos tot der', pasos_d_tot)
            
            if giro_derecha:
                TRIGGER_OBS = GPIO_TRIGGER_I
                ECHO_OBS = GPIO_ECHO_I
                TRIGGER_2 = GPIO_TRIGGER_D
                ECHO_2 = GPIO_ECHO_D
            else:
                TRIGGER_OBS = GPIO_TRIGGER_D
                ECHO_OBS = GPIO_ECHO_D
                TRIGGER_2 = GPIO_TRIGGER_I
                ECHO_2 = GPIO_ECHO_I
            giro_90(TRIGGER_OBS, ECHO_OBS, TRIGGER_2, ECHO_2)
            n_giro = 2
            dist = distancia(GPIO_TRIGGER_F, GPIO_ECHO_F)
            time.sleep(t_ult)
            
            #print(dist)
            while(dist>22):
                dist = distancia(GPIO_TRIGGER_F, GPIO_ECHO_F)
                #print('prueba 2', dist)
                if(dist<=36):
                    break
                time.sleep(t_ult)
            #print(dist)
            #print('Pasos tot izq:', pasos_i_tot, 'Pasos tot der', pasos_d_tot)
            print('..........................GIRO 2.............................')
            giro_90(TRIGGER_OBS, ECHO_OBS, TRIGGER_2, ECHO_2)  
            time.sleep(1.5)
            pwm_D.ChangeDutyCycle(36) 
            pwm_I.ChangeDutyCycle(32)
            TARGET_F = 70
            n_giro = 1   
            if giro_derecha:
                giro_derecha = False
            else:
                giro_derecha = True
            
            pasos_d = 0
            pasos_i = 0
                
        else:
            duty_D = 0
            duty_I = 0
            print('Estado quieto')
        
        if pasos_d > 80:
            print('Tomando suelo')
            corregir = False
            D_IZQ = 0
            D_IZQ_PROM = 0
            
            pwm_D.ChangeDutyCycle(0.0)
            pwm_I.ChangeDutyCycle(0.0)
            time.sleep(0.5)
            
            for i in range(0,5,1):
                D_IZQ += distancia(GPIO_TRIGGER_D, GPIO_ECHO_D) 
                time.sleep(0.15)
            D_IZQ_PROM = D_IZQ/5
            
#             if(D_IZQ_PROM > 10):
#                 pwm_D.ChangeDutyCycle(55)
#                 time.sleep(0.7)
#                 pwm_D.ChangeDutyCycle(0)
#                 pwm_I.ChangeDutyCycle(46)
#                 time.sleep(0.5)
#                 pwm_D.ChangeDutyCycle(0.0)
#                 pwm_I.ChangeDutyCycle(0.0)
#                 time.sleep(0.5)
#                 corregir = True
            
            pwm_s.ChangeDutyCycle(8.5)
            time.sleep(0.25)
            pwm_s.ChangeDutyCycle(0)
            
            GPIO.output(trans,True)
            while GPIO.input(recep) == GPIO.LOW:
                time.sleep(0.01)
            
            ser = serial.Serial(port1, 9600, timeout=2.0)
            ser2 = serial.Serial(port2, 9600, timeout=2.0)
            
            while str(ser.readline())[2] != 's':
                time.sleep(0.01)
            datos[1].append(time.strftime("%H:%M:%S"))
            datos = leer_almacenar(datos, 2)       
            print('Suelo: ' + str(datos_hum_suelo))
            
            while str(ser.readline())[2] != 'l':
                time.sleep(0.01)
            datos = leer_almacenar(datos, 3)
            print('Latitud: ' + str(datos_lat))
            
            while str(ser.readline())[2] != 'g':
                time.sleep(0.01)
            datos = leer_almacenar(datos, 4)      
            print('Longitud: ' + str(datos_lon))
            
            pwm_s.ChangeDutyCycle(2)
            time.sleep(0.245)
            
            pwm_s.ChangeDutyCycle(0)
            time.sleep(1)
            ser.close()
            ser2.close()        
            GPIO.output(trans, False)
            
            datos_fecha.append(str(time.strftime("%y-%m-%d")))
            
            datos_frame = {'Fecha': datos_fecha,
                 'Hora': datos_hora,
                 'Humedad del suelo': datos_hum_suelo,
                 'Latitud': datos_lat,
                 'Longitud': datos_lon }
            
            datos_CSV = pd.DataFrame(datos_frame)
            if nuevo:
                datos_CSV.to_csv(str(archivo), index=False)
            else:
                datos_CSV.to_csv(str(archivo), mode = 'a', header=False, index=False)
                for i in range(0,5,1):
                    datos[i] = []
            
            pasos_d = 0
            pasos_i = 0
            if (corregir):
                pwm_I.ChangeDutyCycle(45)
                time.sleep(0.6)
                pwm_I.ChangeDutyCycle(0)
                pwm_D.ChangeDutyCycle(53)
                time.sleep(0.7)
                pwm_D.ChangeDutyCycle(0.0)
                pwm_I.ChangeDutyCycle(0.0)
                time.sleep(0.5)
        pwm_D.ChangeDutyCycle(duty_D) 
        pwm_I.ChangeDutyCycle(duty_I)
        t = time.time()
        #print('Pasos derecha;', pasos_d, 'Pasos izquierda;', pasos_i)
        print()
        time.sleep(t_ult)
        # Reset by pressing CTRL + C
except KeyboardInterrupt:
    print("Medicion detenida")
    GPIO.cleanup()


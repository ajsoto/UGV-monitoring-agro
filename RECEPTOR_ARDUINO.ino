#include <SoftwareSerial.h>

#define TX_Lora 10
#define RX_Lora 11
#define LED 3

SoftwareSerial serialLora(RX_Lora, TX_Lora);
void setup() {
  Serial.begin(9600);
  serialLora.begin(9600);
  pinMode(LED, OUTPUT);
  digitalWrite(LED, LOW);

}

void loop() {
  if(serialLora.available()){
    digitalWrite(LED, HIGH);
    delay(100);
    digitalWrite(LED, LOW);
    char lect = serialLora.read();
    Serial.print(lect);
    
  }

}

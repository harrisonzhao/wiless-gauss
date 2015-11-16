#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
// https://github.com/gcopeland/rf24

RF24 radio(7, 8);

void setup()
{
  while (!Serial);
  Serial.begin(9600);
  
  radio.begin();
  radio.openReadingPipe(0, 00001);
  
  radio.startListening();
}

void loop()
{
  if (radio.available())
  {
    char text[32] = {0};
    radio.read(&text, sizeof(text));
    
    Serial.println(text);
  }
}

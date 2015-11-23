#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

// https://github.com/maniacbug/RF24

RF24 radio(7, 8);

void setup()
{
  radio.begin();
  radio.setRetries(15, 15);
  radio.openWritingPipe(00001);
  Serial.begin(9600);
  radio.stopListening();
}

void loop()
{
  const char text[] = "Hello World";
  int m = millis();
  char out[20];
  sprintf(out,"%s:%d",text,m);
  
  radio.write(&out, sizeof(out));
  Serial.println(out);
  delay(0);
}


const int ap1 = A5;
const int ap2 = A4;
const int ap3 = A3;
const int fs1 = A2;
const int fs2 = A1;

int sv1 = 0;          
int sv2 = 0;  
int sv3 = 0;
int fp1 = 0;
int fp2 = 0;

void setup() {
  // initialize serial communications at 9600 bps:
  Serial.begin(9600);

 
}

void loop() {
  analogReference(EXTERNAL);    //connect 3.3v to AREF

  sv1 = analogRead(ap1);           
  delay(2);                    
 
  sv2 = analogRead(ap2);           
  delay(2);                
 
  sv3 = analogRead(ap3);           
  delay(2);
 
  fp1 = analogRead(fs1); 
  delay(2);
 
  fp2 = analogRead(fs2); 
 
  // print the results to the serial monitor:
  Serial.print(" X = " ); 
    Serial.print(sv1);  
  Serial.print(" Y = " );
    Serial.print(sv2);
  Serial.print(" Z = " );
    Serial.print(sv3);
  Serial.print(" \t thumb = ");
    Serial.print(fp1);
  Serial.print(" finger = ");
    Serial.print(fp2);   
  Serial.println(""); 
  
 
 
 
  delay(250);                    
 
} 

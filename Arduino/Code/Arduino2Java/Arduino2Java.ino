//working version 1.0

#include "I2Cdev.h"
#include "MPU6050_6Axis_MotionApps20.h"
#if I2CDEV_IMPLEMENTATION == I2CDEV_ARDUINO_WIRE
    #include "Wire.h"
#endif

MPU6050 mpu;

#define OUTPUT_READABLE_YAWPITCHROLL

const int ap1 = A1;
const int ap2 = A2;
const int ap3 = A3;

bool dmpReady = false;  // set true if DMP init was successful
uint8_t mpuIntStatus;   // holds actual interrupt status byte from MPU
uint8_t devStatus;      // return status after each device operation (0 = success, !0 = error)
uint16_t packetSize;    // expected DMP packet size (default is 42 bytes)
uint16_t fifoCount;     // count of all bytes currently in FIFO
uint8_t fifoBuffer[64]; // FIFO storage buffer

Quaternion q;           // [w, x, y, z]         quaternion container
VectorInt16 aa;         // [x, y, z]            accel sensor measurements
VectorInt16 aaReal;     // [x, y, z]            gravity-free accel sensor measurements
VectorInt16 aaWorld;    // [x, y, z]            world-frame accel sensor measurements
VectorFloat gravity;    // [x, y, z]            gravity vector
float euler[3];         // [psi, theta, phi]    Euler angle container
float ypr[3];           // [yaw, pitch, roll]   yaw/pitch/roll container and gravity vector

// packet structure for InvenSense teapot demo
uint8_t teapotPacket[14] = { '$', 0x02, 0,0, 0,0, 0,0, 0,0, 0x00, 0x00, '\r', '\n' };


volatile bool mpuInterrupt = false;     // indicates whether MPU interrupt pin has gone high
void dmpDataReady() {
    mpuInterrupt = true;
}

int16_t ax, ay, az, gx, gy, gz;

int sv1 = 0;          
int sv2 = 0;  
int sv3 = 0;

void setup() {
    // join I2C bus (I2Cdev library doesn't do this automatically)
    #if I2CDEV_IMPLEMENTATION == I2CDEV_ARDUINO_WIRE
        Wire.begin();
        TWBR = 24; // 400kHz I2C clock (200kHz if CPU is 8MHz). Comment this line if having compilation difficulties with TWBR.
    #elif I2CDEV_IMPLEMENTATION == I2CDEV_BUILTIN_FASTWIRE
        Fastwire::setup(400, true);
    #endif

    Serial.begin(230400);
    Serial1.begin(230400); //If connected Bluetooth module properly to Arduino Micro
    
    //Set internal pull up resistor
    // pinMode(ap1, INPUT_PULLUP);
    // pinMode(ap2, INPUT_PULLUP);
    // pinMode(ap3, INPUT_PULLUP);
    
    
//    Serial.println(F("Initializing I2C devices..."));
    mpu.initialize();

//    Serial.println(F("Testing device connections..."));
//    Serial.println(mpu.testConnection() ? F("MPU6050 connection successful") : F("MPU6050 connection failed"));
//
//    Serial.println(F("\nSend any character to begin DMP programming and demo: "));
//    while (Serial.available() && Serial.read()); // empty buffer
//    while (!Serial.available());                 // wait for data
//    while (Serial.available() && Serial.read()); // empty buffer again

    // load and configure the DMP
//    Serial.println(F("Initializing DMP..."));
    devStatus = mpu.dmpInitialize();

    // supply your own gyro offsets here, scaled for min sensitivity
    mpu.setXGyroOffset(220);
    mpu.setYGyroOffset(76);
    mpu.setZGyroOffset(-85);
    mpu.setZAccelOffset(1788); // 1688 factory default for my test chip

    // make sure it worked (returns 0 if so)
    if (devStatus == 0) {
        // turn on the DMP, now that it's ready
//        Serial.println(F("Enabling DMP..."));
        mpu.setDMPEnabled(true);

        // enable Arduino interrupt detection
//        Serial.println(F("Enabling interrupt detection (Arduino external interrupt 0)..."));
        attachInterrupt(0, dmpDataReady, RISING);
        mpuIntStatus = mpu.getIntStatus();

        // set our DMP Ready flag so the main loop() function knows it's okay to use it
//        Serial.println(F("DMP ready! Waiting for first interrupt..."));
        dmpReady = true;

        // get expected DMP packet size for later comparison
        packetSize = mpu.dmpGetFIFOPacketSize();
    } else {
        // ERROR!
        // 1 = initial memory load failed
        // 2 = DMP configuration updates failed
        // (if it's going to break, usually the code will be 1)
//        Serial.print(F("DMP Initialization failed (code "));
//        Serial.print(devStatus);
//        Serial.println(F(")"));
    }
}

void loop() {

    mpu.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);
    int vx = map(ax, -16000, 16000, 90, -90);
    int vy = map(ay, -16000, 16000, 90, -90);


    sv1 = analogRead(ap1);           
    delay(2);                    
   
    sv2 = analogRead(ap2);           
    delay(2);                
   
    sv3 = analogRead(ap3);           
    delay(2);
   
    if (!dmpReady) {
      return;
    }
    
    mpuInterrupt = false;
    mpuIntStatus = mpu.getIntStatus();
    fifoCount = mpu.getFIFOCount();

    if ((mpuIntStatus & 0x10) || fifoCount == 1024) {
        mpu.resetFIFO();
    }
    else if (mpuIntStatus & 0x02) {
        while (fifoCount < packetSize) {
          fifoCount = mpu.getFIFOCount();
        }
        mpu.getFIFOBytes(fifoBuffer, packetSize);
        
        fifoCount -= packetSize;
        char out[64] = {0};
        char floatBuffer1[8]={0};
        char floatBuffer2[8]={0};
        char floatBuffer3[8]={0};
        mpu.dmpGetQuaternion(&q, fifoBuffer);
        mpu.dmpGetGravity(&gravity, &q);
        mpu.dmpGetYawPitchRoll(ypr, &q, &gravity);

        dtostrf(ypr[0] * 180/M_PI, 7, 3, floatBuffer1);
        dtostrf(ypr[1] * 180/M_PI, 7, 3, floatBuffer2);
        dtostrf(ypr[2] * 180/M_PI, 7, 3, floatBuffer3);
        sprintf(out,"s%s %s %s %d %d %d %d %df",floatBuffer1,floatBuffer2,floatBuffer3,sv1,sv2,sv3,-vx,-vy);
        Serial.print(out);
        Serial1.write(out,64);
    }
    delay(20);
}



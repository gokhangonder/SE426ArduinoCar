// Object Detection
#include <NewPing.h>

//L298N motor driver pins
//3 and 9 enA and enB that has PWM
//others in1, in2, in3, in4

#define enA 12
#define in1 10
#define in2 11

//Motor B Connections
#define enB 9
#define in3 8
#define in4 5

int strength, angle;
String msg, cmd;

/*
  This code is to Remove ambience noise from sensor data.
  IR LED connected to Digital pin: 6
  IR diode connected to analog input:A3

  by-Abhilash Patel
*/

//TCRT5000 InfraRed Sensor Connections

// SensorLeft

#define analogInfrLeft A0
#define digitalInfrLeft 7

// SensorRight

#define analogInfrRight A3
#define digitalInfrRight 6

int a, b, c, d, e, f;

//Ultrasoninc Sensor HC-SR04 Connections
#define echoPin 2 // attach pin D2 Arduino to pin Echo of HC-SR04
#define trigPin 4 //attach pin D4 Arduino to pin Trig of HC-SR04

//defines variables
int distance = 100; // variable for the distance measurement

#define MAX_DISTANCE 200

NewPing sonar(trigPin, echoPin, MAX_DISTANCE);

void setup() {
  // put your setup code here, to run once:

  Serial.begin(9600); // // Serial Communication is starting with 9600 of baudrate speed

  // Set all the motor control pins to outputs
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  //pinMode(13, OUTPUT);

  // Set all infrared sensor pins to outputs
  //pinMode(digitalInfrLeft, OUTPUT);
  //pinMode(digitalInfrRight, OUTPUT);

  // Turn off motors - Initial state
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);

  analogWrite(enA, 255);
  analogWrite(enB, 255);

  distance = readPing();
  msg = "";
}

void loop() {
  // put your main code here, to run repeatedly:
  //moveForward();

  if (Serial.available() > 0) {
    delay(40);
    // Check if there is data coming
    msg = Serial.readString(); // Read the message as String
    Serial.println("Android Command: " + msg);
  }

  if (distance >= 30)
  {
    //moveStop();
    if (msg == "D")
      moveForward();
    else if ( msg == "S")
      moveStop();
    else if (msg == "L")
      turnLeft();
    else if (msg == "R")
      turnRight();
  } else
    moveStop();
    //moveForward();
  distance = readPing();
}

void getIRSensorData() {

  digitalWrite(digitalInfrLeft, HIGH);     // Turning ON LED
  digitalWrite(digitalInfrRight, HIGH);    // Turning ON LED

  delayMicroseconds(500);   //wait

  a = analogRead(analogInfrLeft);       //take reading from photodiode(pin A3) :noise+signal
  d = analogRead(analogInfrRight);

  digitalWrite(digitalInfrLeft, LOW);   //turn Off LED
  digitalWrite(digitalInfrRight, LOW);   //turn Off LED

  delayMicroseconds(500);               //wait
  b = analogRead(analogInfrLeft);       // again take reading from photodiode :noise
  e = analogRead(analogInfrRight);

  c = a - b;                            //taking differnce:[ (noise+signal)-(noise)] just signal
  f = d - e;

  //Serial.print(a);         //noise+signal
  //Serial.print("\t");
  //Serial.print(b);         //noise
  //Serial.print("\t");
  Serial.println(c);         // denoised signal
  Serial.println(f);         // denoised signal

}

int readPing() {
  delay(70);
  int cm = sonar.ping_cm();
  if (cm == 0)
  {
    cm = 250;
  }
  Serial.println(cm);
  return cm;
}

void moveStop() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
}

void moveForward() {

  analogWrite(enA, 255);
  analogWrite(enB, 255);

  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void moveBackward() {

  analogWrite(enA, 255);
  analogWrite(enB, 255);

  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void turnRight() {

  analogWrite(enA, 255);
  analogWrite(enB, 255);

  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
  delay(500);
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void turnLeft() {

  analogWrite(enA, 255);
  analogWrite(enB, 255);

  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
  delay(500);
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

// This function lets you control spinning direction of motors
/*void directionControl() {
  // Set motors to maximum speed
  // For PWM maximum possible values are 0 to 255
  // analogWrite(enA, 255);
  // analogWrite(enB, 255);

  // Turn on motor A & B
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
  delay(2000);

  // Now change motor directions
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
  delay(2000);

  // Turn off motors
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
  }*/

// This function lets you control speed of the motors
/*void speedControl() {
  // Turn on motors
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
*/
// Accelerate from zero to maximum speed
/*for (int i = 0; i < 256; i++) {
  analogWrite(enA, i);
  analogWrite(enB, i);
  delay(20);
  }*/

// Decelerate from maximum speed to zero
/*for (int i = 255; i >= 0; --i) {
  analogWrite(enA, i);
  analogWrite(enB, i);
  delay(20);
  }*/
/*
  // Now turn off motors
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
  }*/

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

String msg, cmd;

//TCRT5000 InfraRed Sensor Connections

// SensorLeft
#define analogInfrLeft A0
//#define digitalInfrLeft 7

// SensorRight
#define analogInfrRight A1
//#define digitalInfrRight 6

//Ultrasoninc Sensor HC-SR04 Connections
#define echoPin 2 // attach pin D2 Arduino to pin Echo of HC-SR04
#define trigPin 4 //attach pin D4 Arduino to pin Trig of HC-SR04

//defines variables
int distance = 100; // variable for the distance measurement
boolean obstacleDetection;
boolean lineTracking;

#define MAX_DISTANCE 200

NewPing sonar(trigPin, echoPin, MAX_DISTANCE);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600); // // Serial Communication is starting with 9600 of baudrate speed
  Serial.setTimeout(100);

  // Set all the motor control pins to outputs
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  //pinMode(13, OUTPUT);

  // Set all infrared sensor pins to outputs
  pinMode(analogInfrLeft, INPUT);
  pinMode(analogInfrRight, INPUT);

  // Turn off motors - Initial state
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);

  analogWrite(enA, 200);
  analogWrite(enB, 200);

  distance = readPing();
  msg = "S";
  obstacleDetection = true;
}

void loop() {
  // put your main code here, to run repeatedly:
  //moveForward();

  if (Serial.available() > 0) {
    delay(30);
    // Check if there is data coming
    msg = Serial.readString(); // Read the message as String
    Serial.println("Android Command: " + msg);
  }

  if (msg == "O") {
    obstacleDetection = true;
  } else if (msg == "F") {
    obstacleDetection = false;
  }

  if (obstacleDetection) {
    if (distance < 20) {
      moveBackward();
      delay(100);
      moveStop();
    }
  }

  if (distance >= 20) {
    if (msg == "D")
      moveForward();
    else if (msg == "L")
      turnLeft();
    else if (msg == "R")
      turnRight();
    else if (msg == "S")
      moveStop();
    else if (msg == "B")
      moveBackward();
    else if (msg == "T")
      trackLine();
  }
  else {
    if (msg == "S")
      moveStop();
    else if (msg == "B")
      moveBackward();
    else
      moveStop();
  }

  distance = readPing();
}

void trackLine() {
  //Serial.println(digitalRead(analogInfrLeft));
  //Serial.println(digitalRead(analogInfrRight));

  if (digitalRead(analogInfrLeft) == 0 && digitalRead(analogInfrRight) == 0) {
    //Forward
    moveForward();
  }
  //line detected by left sensor
  else if (digitalRead(analogInfrLeft) == 0 && !analogRead(analogInfrRight) == 0) {
    //turn left
    turnLeft();
  }
  //line detected by right sensor
  else if (!digitalRead(analogInfrLeft) == 0 && digitalRead(analogInfrRight) == 0) {
    //turn right
    turnRight();
  }
  //line detected by none
  else if (!digitalRead(analogInfrLeft) == 0 && !digitalRead(analogInfrRight) == 0) {
    //stop
    moveStop();
  }
}

int readPing() {
  //delay(50);
  int cm = sonar.ping_cm();
  if (cm == 0)
  {
    cm = 250;
  }
  //Serial.println(cm);
  return cm;
}

void moveStop() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
}

void moveForward() {
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void moveBackward() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
}

void turnRight() {
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
  delay(40);
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

void turnLeft() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
  delay(40);
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
}

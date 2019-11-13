#include<Servo.h>
Servo servo_one; 
Servo servo_two; 
int servo_pin_one = 9;
int servo_pin_two = 10;
//bulbs


//ultrasonic sensor
int trigger_pin_ultrasonic = 2;
int echo_pin_ultrasonic = 3;
int bulb_pin_ultrasonic = 4; 
int time;
int distance; 

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

//servo
servo_one.attach(servo_pin_one); 
servo_two.attach(servo_pin_two);
  //ultrasonic
  pinMode (trigger_pin_ultrasonic, OUTPUT); 
  pinMode (echo_pin_ultrasonic, INPUT);
  pinMode (bulb_pin_ultrasonic, OUTPUT);

}

void loop() {

    //ultrasonic
    digitalWrite (trigger_pin_ultrasonic, HIGH);
    delayMicroseconds (10);
    digitalWrite (trigger_pin_ultrasonic, LOW);
    time = pulseIn (echo_pin_ultrasonic, HIGH);
    distance = (time * 0.034) / 2;
    //Serial.print (" Distance= ");   
    //Serial.println (distance); 
  if (distance <= 15) 
        {
        Serial.println ("door_open");
        digitalWrite (bulb_pin_ultrasonic, HIGH);

        //isi
        servo_one.write(90); //right

        
        servo_two.write(90);  //left
        delay(1000);
        
        }
  else {
        Serial.println ("door_close");  
        
        digitalWrite (bulb_pin_ultrasonic, LOW);

        //isi
        servo_one.write(0);

        
        servo_two.write(200);       
        delay (3000);
  } 

  //end ultrasonic
}

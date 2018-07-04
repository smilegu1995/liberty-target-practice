/*************************************************** 
  This is an example for our Adafruit 16-channel PWM & Servo driver
  Servo test - this will drive 8 servos, one after the other on the
  first 8 pins of the PCA9685

  Pick one up today in the adafruit shop!
  ------> http://www.adafruit.com/products/815
  
  These drivers use I2C to communicate, 2 pins are required to  
  interface.

  Adafruit invests time and resources providing this open source code, 
  please support Adafruit and open-source hardware by purchasing 
  products from Adafruit!

  Written by Limor Fried/Ladyada for Adafruit Industries.  
  BSD license, all text above must be included in any redistribution
 ****************************************************/

#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <SoftwareSerial.h>

//define total servos  
#define TOT_TARGETS 5
#define RX 10
#define TX 11
SoftwareSerial esp8266(RX,TX); 
// called this way, it uses the default address 0x40
Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();
// you can also call it with a different address you want
//Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(0x41);
// you can also call it with a different address and I2C interface
//Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(&Wire, 0x40);

// Depending on your servo make, the pulse width min and max may vary, you 
// want these to be as small/large as possible without hitting the hard stop
// for max range. You'll have to tweak them as necessary to match the servos you
// have!
//#define SERVOMIN  160 // this is the 'minimum' pulse length count (out of 4096)
//#define SERVOMAX  375 // this is the 'maximum' pulse length count (out of 4096)
#define SERVOMIN  375 // this is the 'minimum' pulse length count (out of 4096)
#define SERVOMAX  160  // this is the 'maximum' pulse length count (out of 4096)
#define LIT_LIMIT 230  // laser usually hits 40-50. Ambient room lighting around 6-7
#define LIT_LIMIT0 200  // laser usually hits 40-50. Ambient room lighting around 6-7

const int knockSensor = A1; // where we connect z
const int threshold = 150;  // thresle used to store the last LED status, to toggle the light
const int threshold0 = 30;  // thresle used to store the last LED status, to toggle the light
const int selectKnockPins[3] = {2, 3, 4}; // S0~2, S1~3, S2~4
const int servMin[5] = {400, 425, 330, 330, 395};
const int servMax[5] = {160, 180, 160, 190, 180};

// these variables will change:
int touchValue = 0;      // variable to store the value read from the sensor pin
int litValue = 0;

//enable analog reading from photoresistors
const int TargetSensor = A0; //where we connect z
const int selectPRPins[3] = {5, 6, 7}; // S0~5, S1~6, S2~7
int r0 = 0;      //value of select pin at the 4051 (s0)

int r1 = 0;      //value of select pin at the 4051 (s1)

int r2 = 0;      //value of select pin at the 4051 (s2)
// our servo # counter
uint8_t servonum = 0;

int count = 0;

bool visitedTarget[5] = {true, false, false, false, false};
bool game = false;

String AP = "ol_demo_wifi";       // CHANGE ME
String PASS = "was4ever"; // CHANGE ME
char reply[500]; // you wouldn't normally do this
char ipAddress [20];
const bool printReply = true;
const char line[] = "-----\n\r";

int countTrueCommand;
int countTimeCommand; 
boolean found = false;
bool DEBUG = true;   //show more logs

void setup() {
  Serial.begin(9600);
  //esp8266.begin(115200);
  //sendCommand("AT+UART_DEF=9600,8,1,0,0",5,"OK");
  //delay(1000);
  //esp8266.end();
  // Start the software serial for communication with the ESP8266
  esp8266.begin(9600);
  //sendCommand("AT",5,"OK");
  //esp8266.println(command);
  //esp8266.println("AT");

  /*
  sendCommand("AT",5,"OK");
  sendCommand("AT+CWMODE=1",5,"OK");
  sendCommand("AT+CWJAP=\""+ AP +"\",\""+ PASS +"\"",20,"OK");
  sendToWifi("AT+CIFSR", 10, true);
  sendToWifi("AT+CIPMUX=1",10,DEBUG); // configure for multiple connections
  sendToWifi("AT+CIPSERVER=1,80",10,DEBUG); // turn on server on port 80
  sendToUno("Wifi connection is running!",10,DEBUG);*/
  
  randomSeed(analogRead(3));
  pwm.begin();
  
  pwm.setPWMFreq(60);  // Analog servos run at ~60 Hz updates
  for (int i = 1; i < TOT_TARGETS; i++){
      pwm.setPWM(i, 0, servMax[i]);
  }
  pwm.setPWM(servonum, 0, SERVOMIN);
  pinMode(selectPRPins[0], OUTPUT);
  pinMode(selectPRPins[1], OUTPUT);
  pinMode(selectPRPins[2], OUTPUT);

  pinMode(selectKnockPins[0], OUTPUT);
  pinMode(selectKnockPins[1], OUTPUT);
  pinMode(selectKnockPins[2], OUTPUT);
  delay(1000);
}

// you can use this function if you'd like to set the pulse length in seconds
// e.g. setServoPulse(0, 0.001) is a ~1 millisecond pulse width. its not precise!
void setServoPulse(uint8_t n, double pulse) {
  double pulselength;
  
  pulselength = 1000000;   // 1,000,000 us per second
  pulselength /= 60;   // 60 Hz
  Serial.print(pulselength); Serial.println(" us per period"); 
  pulselength /= 4096;  // 12 bits of resolution
  Serial.print(pulselength); Serial.println(" us per bit"); 
  pulse *= 1000000;  // convert to us
  pulse /= pulselength;
  Serial.println(pulse);
  pwm.setPWM(n, 0, pulse);
}

void loop() {  
  if(esp8266.available()>0){
    String message = readWifiSerialMessage();
    Serial.println("received message from TCP Client: "+ message);
    if(find(message,"ping")){  //receives ping from wifi
        esp8266.print("ok");   
    } else if (find(message, "AU")){
      sendToWifi("ok", 10, false);
      allTargetsUp();
    } else if (find(message, "AD")){
      sendToWifi("ok", 10, false);
      allTargetsDown();
    } else if (find(message, "GSTR")){
      sendToWifi("ok", 10, false);
      gameCycleStart();
    } else if (find(message, "T_C")){
      sendToWifi("ok", 10, false);
      cycleTargets();
    } else if (find(message, "GG")){
       sendToWifi("ok", 10, false);
    } else if (find(message, "T_X")){
       sendToWifi("ok", 10, false);
       dataStreamTxTest();
    } else{
      sendToWifi("NC", 10, false);
      Serial.println("\n"+ message + " is not a valid input");
    }
  }
  delay(100);
}

void allTargetsDown() {
  for (int i = 0; i < 5; i++){
    visitedTarget[i] = false;
    pwm.setPWM(i, 0, servMax[servonum]);
  }
}

void allTargetsUp(){
    for (int i = 0; i < 5; i++){
      visitedTarget[i] = false;
      pwm.setPWM(i, 0, servMin[servonum]);
    }
}

void cycleTargets(){
  allTargetsDown();
  int count = 0;
  while (count <5){
    while (visitedTarget[servonum]) {
      servonum = random(0,5);
    }
    count++;
    pwm.setPWM(servonum, 0, servMin[servonum]);
    visitedTarget[servonum] = true;
    delay(500);   
    pwm.setPWM(servonum, 0, servMax[servonum]); 
  }
  if (count == 5) {
    for (int i = 0; i < 5; i++) {
      visitedTarget[i] = false;
      count--;
    }
  }
}

void dataStreamTxTest(){
  int count = 0;
  delay(10000);
  while (count < 5){
    sendToWifi("validhit", 10, false);
    count++;
    delay(3000);
  }
  sendToWifi("validend", 10, false);
  delay(1000);
  sendToWifi("validend", 10, false);
}

void gameCycleStart(){
  //cycleTargets();
  Serial.println("Game Cycle Start!");
  allTargetsDown();
  game = true;
  int count = 0;
  boolean servochanged = true;
  unsigned long startTime = millis();
  
  while (game) {

      if(esp8266.available()>0){
        String message = readWifiSerialMessage();
          if (find(message,"GG")){
            sendToWifi("resethit", 10, false);
            game = false;
          }
        }
      // Drive each servo one at a time
      //Serial.println(servonum);
      // read the sensor and store it in the variable sensorReading:
      visitedTarget[servonum] = true;
      selectMuxKnockPin(servonum);
      selectMuxPRPin(servonum);
      touchValue = analogRead(knockSensor);
    //  convert sensor output value
    
    
      litValue = map(analogRead(TargetSensor), 0, 1023, 0, 255);
      //Serial.println(litValue);
      //Serial.println(touchValue);
      if (litValue > LIT_LIMIT || touchValue > threshold) {      
          pwm.setPWM(servonum, 0, servMax[servonum]);
          sendToWifi("validhit", 10, false);
          if (count == 0){
            delay(500);
            sendToWifi("\nvalidhit", 10, false);
          }
          count++;
          if (millis() - startTime > 62000){
            game = false;
            Serial.println("Exiting Game Cycle");
            break;
          }
          if (count > 0 && count % 5 == 0) {
            for (int i = 0; i < 5; i++) {
              visitedTarget[i] = false;
            }
          }
          servonum = random(0,5);
          while (visitedTarget[servonum]) {
            servonum = random(0,5);
          }
          servochanged = true;
          Serial.println("visitng servo");
          Serial.println(servonum);
          visitedTarget[servonum] = true;
          delay(2000);
      } else {
          if (millis() - startTime > 62000){
            game = false;
            Serial.println("Exiting Game Cycle");
            break;
          }
          pwm.setPWM(servonum, 0, servMin[servonum]);
          if (servochanged){
            servochanged = false;
            delay(2000);
          }
      }
  }
  allTargetsDown();
  sendToWifi("validend", 10, false);
  delay(1000);
  sendToWifi("validend", 10, false);
}

void selectMuxPRPin(byte pin)
{
  if (pin > 5) return; // Exit if pin is out of scope  
  for (int i=0; i<3; i++)
  {
    if (pin & (1<<i))
      digitalWrite(selectPRPins[i], HIGH);
    else
      digitalWrite(selectPRPins[i], LOW);
  }
}

void selectMuxKnockPin(byte pin)
{
  if (pin > 5) return; // Exit if pin is out of scope
  
  for (int i=0; i<3; i++)
  {
    if (pin & (1<<i))
      digitalWrite(selectKnockPins[i], HIGH);
    else
      digitalWrite(selectKnockPins[i], LOW);
  }
}

void sendCommand(String command, int maxTime, char readReplay[]) {
  Serial.print(countTrueCommand);
  Serial.print(". at command => ");
  Serial.print(command);
  Serial.print(" ");
  while(countTimeCommand < (maxTime*1))
  {
    esp8266.println(command);//at+cipsend
    if(esp8266.find(readReplay))//ok
    {
      found = true;
      break;
    }
  
    countTimeCommand++;
  }
  
  if(found == true)
  {
    Serial.println("OYI");
    countTrueCommand++;
    countTimeCommand = 0;
  }
  
  if(found == false)
  {
    Serial.println("Fail");
    countTrueCommand = 0;
    countTimeCommand = 0;
  }
  
  found = false;
 }

/*
* Name: find
* Description: Function used to match two string
* Params: 
* Returns: true if match else false
*/
boolean find(String string, String value){
  if(string.indexOf(value)>=0)
    return true;
  return false;
}

/*
* Name: sendToWifi
* Description: Function used to send data to ESP8266.
* Params: command - the data/command to send; timeout - the time to wait for a response; debug - print to Serial window?(true = yes, false = no)
* Returns: The response from the esp8266 (if there is a reponse)
*/
String sendToWifi(String command, const int timeout, boolean debug){
  String response = "";
  esp8266.print(command); // send the read character to the esp8266
  /*
  long int time = millis();
  while( (time+timeout) > millis())
  {
    while(esp8266.available())
    {
    // The esp has data so display its output to the serial window 
    char c = esp8266.read(); // read the next character.
    response+=c;
    }  
  }
  if(debug)
  {
    Serial.println(response);
  }*/
  return response;
}


/*
* Name: sendToUno
* Description: Function used to send data to Uno.
* Params: command - the data/command to send; timeout - the time to wait for a response; debug - print to Serial window?(true = yes, false = no)
* Returns: The response from the uno (if there is a reponse)
*/
String sendToUno(String command, const int timeout, boolean debug){
  String response = "";
  Serial.println(command); // send the read character to the esp8266
  long int time = millis();
  while( (time+timeout) > millis())
  {
    while(Serial.available())
    {
      // The esp has data so display its output to the serial window 
      char c = Serial.read(); // read the next character.
      response+=c;
    }  
  }
  if(debug)
  {
    Serial.println(response);
  }
  return response;
}

/*
* Name: sendData
* Description: Function used to send string to tcp client using cipsend
* Params: 
* Returns: void
*/
void sendData(String str){
  String len="";
  len+=str.length();
  sendToWifi("AT+CIPSEND=0,"+len,10,DEBUG);
  delay(100);
  sendToWifi(str,10,DEBUG);
  delay(100);
  Serial.println("\nSent message on TCP: " + str);
  //sendToWifi("AT+CIPCLOSE=5",10,DEBUG);
}

void getReply(int wait)
{
    int tempPos = 0;
    long int time = millis();
    while( (time + wait) > millis())
    {
        if (esp8266.available() > 0)
        {
            String message = readWifiSerialMessage();
            Serial.println(message);  
        }
        reply[tempPos] = 0;
    } 
 
    //if (printReply) { Serial.println( reply );}
}

/*
* Name: readSerialMessage
* Description: Function used to read data from Arduino Serial.
* Params: 
* Returns: The response from the Arduino (if there is a reponse)
*/
String  readSerialMessage(){
  char value[100]; 
  int index_count =0;
  while(Serial.available()>0){
    value[index_count]=Serial.read();
    index_count++;
    value[index_count] = '\0'; // Null terminate the string
  }
  String str(value);
  str.trim();
  return str;
}

/*
* Name: readWifiSerialMessage
* Description: Function used to read data from ESP8266 Serial.
* Params: 
* Returns: The response from the esp8266 (if there is a reponse)
*/
String  readWifiSerialMessage(){
  char value[100]; 
  int index_count =0;
  while(esp8266.available()>0){
    value[index_count]=esp8266.read();
    index_count++;
    value[index_count] = '\0'; // Null terminate the string
  }
  String str(value);
  str.trim();
  return str;
}


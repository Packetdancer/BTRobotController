int cmd_mode = 0;

void setup() {
  // put your setup code here, to run once:
  pinMode(13,OUTPUT);
  digitalWrite(13,HIGH);
  Serial.begin(115200);
}

void loop() {
  // put your main code here, to run repeatedly: 
  int bytesToRead = Serial.available();
  if ((bytesToRead == 0) && (cmd_mode != 0)) { 
    delay(200);
    return;
  }

  char readBuffer[256];
  String inputLine;
  if (cmd_mode != 0) {
    int bytesRead = Serial.readBytes((char *)readBuffer,bytesToRead);
    if (bytesRead > 0) {
    }
    readBuffer[bytesRead] = 0;
    inputLine = String(readBuffer);
  }
  
  
  switch(cmd_mode) {
    case 0:
      Serial.print("$$$");
      cmd_mode = 1;
      break;
     
    case 1:
      if (inputLine.startsWith("CMD")) {
        cmd_mode = 2;
        digitalWrite(13,LOW);
        Serial.println("SF,1");
        Serial.println("---");
        Serial.flush();
      }
      break;
  }
}

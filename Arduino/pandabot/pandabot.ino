#include <HUBeeBMDWheel.h>
 
HUBeeBMDWheel lWheel(10,9,8);
HUBeeBMDWheel rWheel(7,6,5);
 
int cmdBuf[2];
 
void setup()
{
  pinMode(13,OUTPUT);
  lWheel.stopMotor();
  rWheel.stopMotor();
  lWheel.setDirectionMode(1);
  Serial.begin(151200);
}
 
void blink(int count)
{
  for (int loop = 0; loop < count; loop++) {
    digitalWrite(13,HIGH);
    delay(200);
    digitalWrite(13,LOW);
    delay(200);
  } 
}
 
void loop()
{
  int availableBytes = Serial.available();
  if (availableBytes == 0) return;

  // blink(availableBytes);
  
  Serial.readBytes((char *)cmdBuf, 4);

  lWheel.setMotorPower(cmdBuf[0]);
  rWheel.setMotorPower(cmdBuf[1]);
}

import pt.citar.diablu.processing.nxt.*;



LegoNXT lego;
int powerA;

void setup() {
  size(400, 400);
  lego = new LegoNXT(this, "COM15");
  frameRate(10);
  powerA = 40;
}


void draw() {

  //lego.playTone((int)random(200, 7000), (int)random(200, 800));
   
   if( lego.getButtonState(0)) {
     lego.motorForward(LegoNXT.MOTOR_A, powerA);


     fill(255, 0, 0);
   } else{
     fill(255);
 
     lego.motorHandBrake(LegoNXT.MOTOR_A);
     }
   rect(0, 0, 100, 100);
   fill(255);
   if(lego.getButtonState(3)) {
     fill(0, 255, 0);
   }
   rect(200, 0, 100, 100);
}


void keyPressed() {
  println(key);
  
  if (key == '1') {
    println(lego.motorForward(LegoNXT.MOTOR_A, 30));
  } 
  else if(key =='2') {
    lego.motorForward(LegoNXT.MOTOR_B, 50);
  } 
  else if (key == '3') {
    lego.motorForward(LegoNXT.MOTOR_C, 100);
  } 
  else if (key =='q') {
    lego.motorStop(LegoNXT.MOTOR_A);
  } 
  else if(key == 'w') {
    lego.motorHandBrake(LegoNXT.MOTOR_B);
  } 
  else if (key == 'e') {
    lego.motorHandBrake(LegoNXT.MOTOR_C);
  }
}

void mousePressed() {
 println("Botao 1: " + lego.getButtonState(0));
  println("Botao 4: " + lego.getButtonState(3));
}

void stop() {

}

import communication.SerialRead;
import mousemovement.MouseCtrl;

import java.util.ArrayList;

public class Main {

    public static void main(String [] args) throws InterruptedException {

        MouseCtrl mouseCtrl = new MouseCtrl();
        mouseCtrl.setInitialPos(900, 1000);

        SerialRead serialRead = new SerialRead();
//        serialRead.openAndSetSerialPort("/dev/ttyACM0");
        serialRead.openAndSetSerialPort("/dev/rfcomm0");


        int f1,f2,f3,leftCount=0,rightCount=0, vx,vy;
        double x,y,z;
        String n;
        String [] vals = null;
        ArrayList<String> newVals = new ArrayList<>();
        //x should not move when 0
        //y should not move when +-90
        //y should decrease when 0
        //45,90 goes top left
        //-45,0 go left
        while(true) {
            try {
                vals = serialRead.readFromSerialPortGyro();
                for(String v: vals){
                    if(v.length()>0){
                        newVals.add(v);
                        System.out.print(v);
                    }
                }
                System.out.println();
//                System.out.println("val: "+newVals.get(0)+" "+newVals.get(1)+" "+newVals.get(2)+" "+newVals.get(3)+" "+newVals.get(4)+" "+newVals.get(5)
//                +" "+newVals.get(6)+" "+newVals.get(7));
                f1 = Integer.parseInt(newVals.get(4));
                f2 = Integer.parseInt(newVals.get(3));
                f3 = Integer.parseInt(newVals.get(5));
                x = Float.parseFloat(newVals.get(0));
                y = Float.parseFloat(newVals.get(1));
                z = Float.parseFloat(newVals.get(2));
                vx = Integer.parseInt(newVals.get(6));
                vy = Integer.parseInt(newVals.get(7));

                newVals.clear();
//                if(f1>750){
//                    mouseCtrl.moveAtAngleWithVel(x, y, -z);
                    mouseCtrl.moveMousePosition(vx,vy);
                    //3rd to decide axis of mouse movement
                    //2nd to be more important when gyro angles are 0 or +-180
                    //1st to be more important when gyro angles are 90 ot -90
                    //control mouse
//                }
                if(f2<50){
                    if(leftCount>10){
                        mouseCtrl.leftClick();
                        leftCount=0;
                    }
                    else{
                        leftCount++;
                    }
                }
                if(f3<50){
                    if(rightCount>10){
                        mouseCtrl.rightClick();
                        rightCount=0;
                    }
                    else{
                        rightCount++;
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
                newVals.clear();
            }
        }
    }
}


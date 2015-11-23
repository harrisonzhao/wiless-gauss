import communication.radio.SerialRead;
import mousemovement.MouseCtrl;

import java.awt.*;

public class Main {

    public static void main(String [] args) throws InterruptedException {

        MouseCtrl mouseCtrl = new MouseCtrl();
        mouseCtrl.setInitialPos(1000,500);

        SerialRead serialRead = new SerialRead();
        serialRead.openAndSetSerialPort("/dev/ttyACM0");
        while(true) {
            String[] vals = serialRead.readFromSerialPort();
            if(Integer.parseInt(vals[3])>70){
                mouseCtrl.moveUp(1);
            }
            if(Integer.parseInt(vals[4])>70){
                mouseCtrl.moveDown(1);
            }
            if(Integer.parseInt(vals[5])>70){
                mouseCtrl.moveLeft(1);
            }
        }
    }
}

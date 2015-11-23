package communication.radio;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.nio.charset.StandardCharsets;

public class SerialRead {

    private SerialPort serialPort;

    public void openAndSetSerialPort(String port){
        serialPort = new SerialPort("/dev/ttyACM0");
        try {
            System.out.println("Port opened: " + serialPort.openPort());
            System.out.println("Params setted: " + serialPort.setParams(9600,64, 1, 0));
        }
        catch (SerialPortException e){
            e.printStackTrace();
        }
    }

    public String [] readFromSerialPort(){
        byte[] data = new byte[0];
        try {
            data = serialPort.readBytes(64);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        String output = new String(data, StandardCharsets.UTF_8);
        String [] vals = output.split(" ");
        vals[0] = vals[0].substring(findIndexOfFirstInt(vals[0]));
        vals[vals.length-1] = vals[vals.length-1].substring(0,findIndexOfLastInt(vals[vals.length-1])+1);
        return vals;

    }

    private static int findIndexOfLastInt(String s) {
        for(int i=s.length()-1;i>0;i--){
            if(((s.charAt(i))> 47) && ((s.charAt(i))<58)) {
                return i;
            }
        }
        return 0;
    }

    private static int findIndexOfFirstInt(String s){
        for(int i=0;i<s.length();i++){
            if(((s.charAt(i))> 47) && ((s.charAt(i))<58)) {
                return i;
            }
        }
        return 0;
    }
}
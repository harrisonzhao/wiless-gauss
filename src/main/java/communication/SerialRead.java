package communication;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.nio.charset.StandardCharsets;

public class SerialRead {

    private SerialPort serialPort;

    public void openAndSetSerialPort(String port){
        serialPort = new SerialPort(port);
        try {
            System.out.println("Port opened: " + serialPort.openPort());
            System.out.println("Params setted: " + serialPort.setParams(230400, 64, 1, 0));
        }
        catch (SerialPortException e){
            e.printStackTrace();
        }
    }

    public String [] readFromSerialPortGyro(){
        byte[] data = new byte[0];
        String parsedData=null;
        try {
            data = serialPort.readBytes(64);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        String output = new String(data, StandardCharsets.UTF_8);
        int start = output.indexOf("s");
        int finish = output.indexOf("f");
        if(start<finish){
            parsedData = output.substring(start,finish);
        }
        else{
            parsedData = output.substring(start) + output.substring(0, finish);
        }
        parsedData = parsedData.substring(1);
        //System.out.println("parsed: "+parsedData);
        String [] vals = parsedData.split(" ");
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

    public void restart() {
        try {
            serialPort.closePort();
            serialPort.openPort();
            serialPort.setParams(9600,64, 1, 0);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
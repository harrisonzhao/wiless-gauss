package communication.bluetooth;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;

public class BlueToothConnector {
    private static Object lock = new Object();

    public void run() {
        UUID[] uuidSet = new UUID[1];
        uuidSet[0]=new UUID(0x1105); //OBEX Object Push service

        int[] attrIDs =  new int[] {
                0x0100 // Service name
        };
        try {
            // 1
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            // 2
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            // 3
            agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());


            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Device Inquiry Completed. ");

        } catch( Exception e ){
            e.printStackTrace();
        }
    }
}

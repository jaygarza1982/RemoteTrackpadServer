//Purpose of class is to be a UDP packet publisher, MulticastReceiver receives the packet

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastPublisher {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public void multicast(String multicastMessage) throws Exception {
        socket = new DatagramSocket();
        group = InetAddress.getByName("224.0.2.60");
        buf = multicastMessage.getBytes();

        Thread t = new Thread(new Runnable() {
        	public void run() {
        		while (true) {
        			try {
        				DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        				socket.send(packet);

        				Thread.sleep(1500);
        			} catch (Exception e) {
        				e.printStackTrace();
        			}

        		}
        	}
        });
        t.start();

    }
}
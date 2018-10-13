import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			OutputStream out = socket.getOutputStream();
			
			String fromRemoteClient = in.readLine();
			
			RemoteWindow.printInfo(fromRemoteClient);
			
			Robot robot = new Robot();
			
			//If we receive a mouse move command, move the mouse.
			if (fromRemoteClient.startsWith("MOUSE: ")) {
				fromRemoteClient = fromRemoteClient.replace("MOUSE: ", "");
				
				int x = Integer.parseInt(fromRemoteClient.split(", ")[0]);
				int y = Integer.parseInt(fromRemoteClient.split(", ")[1]);
				
				robot.mouseMove(x, y);
			}
			//Click the left mouse button.
			else if (fromRemoteClient.equals("CLICK LMB")) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			else if (fromRemoteClient.equals("CLICK RMB")) {
				robot.mousePress(InputEvent.BUTTON3_MASK);
				robot.mouseRelease(InputEvent.BUTTON3_MASK);
			}
			else if (fromRemoteClient.equals("HOLD LMB")) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
			}
			else if (fromRemoteClient.equals("RELEASE LMB")) {
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			//Type the keys the user sent.
			else if (fromRemoteClient.startsWith("KEYS:")) {
				fromRemoteClient = fromRemoteClient.replace("KEYS:", "");
				
				for (int i = 0; i < fromRemoteClient.length(); i++) {
					int key = KeyEvent.getExtendedKeyCodeForChar(fromRemoteClient.charAt(i));
					robot.keyPress(key);
					robot.keyRelease(key);
				}
			}
			//Presses the up, down or backspace button
			else if (fromRemoteClient.equals("UP")) {
				robot.keyPress(KeyEvent.VK_UP);
				robot.keyRelease(KeyEvent.VK_UP);
			}
			else if (fromRemoteClient.equals("DOWN")) {
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyRelease(KeyEvent.VK_DOWN);
			}
			else if (fromRemoteClient.equals("BACKSPACE")) {
				robot.keyPress(KeyEvent.VK_BACK_SPACE);
				robot.keyRelease(KeyEvent.VK_BACK_SPACE);
			}
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
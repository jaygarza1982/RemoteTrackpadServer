import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class RemoteWindow extends JFrame {

	public static JTextArea txtServerInfo;
	public static BufferedImage cursorImage;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				RemoteWindow frame = new RemoteWindow();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RemoteWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 596, 527);
		
		//Absolute Layout
		getContentPane().setLayout(null);
		
		setTitle("Jake's Remote Server");
		
		txtServerInfo = new JTextArea();
		txtServerInfo.setEditable(false);
		txtServerInfo.setBounds(10, 11, 560, 466);
		JScrollPane serverInfoScrollPane = new JScrollPane(txtServerInfo);
		serverInfoScrollPane.setBounds(10, 11, 560, 466);
		
		getContentPane().add(serverInfoScrollPane);
		
		try {
			Robot robot = new Robot();
			robot.mouseMove(0, 0);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		
		startServer();
	}
	
	public static void printInfo(String info) {
		txtServerInfo.setText(txtServerInfo.getText() + info + "\n");
		if (txtServerInfo.getText().length() >= 5000)
			txtServerInfo.setText("Log exceeded max size and was cleared.\n");
	}

	public void startServer() {
		final ServerListener server = new ServerListener(25566);
		Thread serverThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						server.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		serverThread.start();
		
		//Broadcast message to all clients on the network that there is a server available
		MulticastPublisher multicastPublisher = new MulticastPublisher();
		try {
			multicastPublisher.multicast("Hello");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

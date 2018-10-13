import java.net.ServerSocket;

public class ServerListener {
	private int port = 0;
	
	public ServerListener(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		
		ServerSocket serverSocket = new ServerSocket(port);
		RemoteWindow.printInfo("Server running on port " + port + ".");
		
		while (true) {
			new ServerThread(serverSocket.accept()).start();
		}
		
	}
}
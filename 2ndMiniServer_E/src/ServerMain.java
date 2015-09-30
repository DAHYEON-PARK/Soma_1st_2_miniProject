import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {
	static final int PORT = 9999;
	private ServerSocket serverSocket;
	public static Map<String, String> userSessions;

	static{
		userSessions = new HashMap<String, String>();
		
		// instead of AcceptUser
		for(int i=1; i<1000; i++){
			userSessions.put(String.valueOf(i), String.valueOf(i*10));
		}
	}

	public void startServer() {

		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("server created.");

//			while(true) {
//				synchronized(userSessions){
//					if(userSessions.size() == 11){
//						System.out.println("user session finished.");
//						break;
//					}
//				}
//
//				Socket socket = serverSocket.accept();
//				System.out.println(socket.getRemoteSocketAddress() + " accept.");
//
//				AcceptUser accept = new AcceptUser(socket);
//				accept.start();
//	1. run으로 호출하면 thread 생성이 안됨.
//	2. start뒤에 thread가 종료되면, 자원이 할당되는데 이를 다시 start시키면 error남!!		
//			}

			while(true){
				Socket socket = serverSocket.accept();
				System.out.println("user score test.");
				ResponseUser response = new ResponseUser(socket);
				response.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new ServerMain().startServer();
	}

}
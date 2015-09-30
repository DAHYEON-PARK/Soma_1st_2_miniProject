import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MyRank extends Thread{

	private Socket socket;
	private PrintWriter pw;
	private String user;

	public MyRank(Socket socket, String user) throws IOException {
		this.socket = socket;
		this.user = user;

		OutputStream os = socket.getOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		pw = new PrintWriter(bw);
	}

	@Override
	public void run() {
		try {

			System.out.println("my rank thread");

			// tree map으로 현재 위치의 앞,뒤를 출력하기로.
			synchronized(ServerMain.userSessions){
				pw.println("my rank : " + ServerMain.userSessions.get(user));
				pw.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//close();
		}
	}

	private void close() {
//		pw.close();
//
//		try {
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}

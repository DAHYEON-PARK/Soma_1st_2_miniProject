import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

public class TopRank extends Thread{

	private Socket socket;
	private PrintWriter pw;
	private int num;

	public TopRank(Socket socket, String num) throws IOException {
		this.socket = socket;
		this.num = Integer.valueOf(num);

		OutputStream os = socket.getOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		pw = new PrintWriter(bw);
	}

	@Override
	public void run() {
		try {

			System.out.println("top rank thread");
			
			// get하는 상황은 thread에서 lock을 걸면 안되고 lock안 걸게끔 코딩하기. 
			
			// multi thread의 의미가 사라짐. - 병목현상.
			// update시에는 get못하게 lock을 걸어야 함.
			synchronized(ServerMain.userSessions){

				Set key = ServerMain.userSessions.keySet();
				// Collections : valueSet();
				int i=1;

				for (Iterator iterator = key.iterator(); iterator.hasNext(); i++) {
					String keyName = (String) iterator.next();
					String valueName = (String) ServerMain.userSessions.get(keyName);

					pw.println(i+". "+keyName + "," + valueName);
					pw.flush();

					if(i == num) break;
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//close();
		}
	}

	private void close() {

// pw와 socket을 모두 close하면 client와의 연결이 끊기므로 하면 안됨!!
		
//		pw.close();
//
//		try {
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}

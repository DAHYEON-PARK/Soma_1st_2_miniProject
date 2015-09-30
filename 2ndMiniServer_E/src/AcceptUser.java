import java.io.*;
import java.net.Socket;

public class AcceptUser extends Thread {

	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;

	public AcceptUser(Socket socket) throws IOException {
		this.socket = socket;

		InputStream is = socket.getInputStream();
		br = new BufferedReader(new InputStreamReader(is));

		OutputStream os = socket.getOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		pw = new PrintWriter(bw);
	}

	@Override
	public void run() {
		try {
			while (true) {
				String str = br.readLine();
				String[] arr;
				arr = str.split(",");

				synchronized(ServerMain.userSessions){
					ServerMain.userSessions.put(arr[0], arr[1]);
				}
				pw.println("Server : " + arr[0] + ", "+ arr[1]);
				pw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			synchronized(ServerMain.userSessions){

				for(String key : ServerMain.userSessions.keySet()){
					System.out.println(key + ", "+ ServerMain.userSessions.get(key));
				}
			}

			close();
		}
	}

	private void close() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pw.close();

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

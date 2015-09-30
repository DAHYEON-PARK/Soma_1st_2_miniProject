import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ResponseUser extends Thread {

	private Socket socket;
	private BufferedReader br;
	//private PrintWriter pw;

	public ResponseUser(Socket socket) throws IOException {
		this.socket = socket;

		InputStream is = socket.getInputStream();
		br = new BufferedReader(new InputStreamReader(is));

		//        OutputStream os = socket.getOutputStream();
		//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		//        pw = new PrintWriter(bw);
	}

	@Override
	public void run() {
		try {
			while (true) {
				
				String str = br.readLine();
				String[] arr;
				arr = str.split(",");

				//System.out.println(arr[0]+"/"+arr[1]+"/"+arr[2]);

				if("update".equalsIgnoreCase(arr[0])){
					// update score
					UpdateScore update = new UpdateScore(socket, arr[1], arr[2]);
					update.start();
				}else if("my".equalsIgnoreCase(arr[0])){
					// get my rank
					MyRank myRank = new MyRank(socket, arr[1]);
					myRank.start();
				}else if("top".equalsIgnoreCase(arr[0])){
					// get top rank
					TopRank topRank = new TopRank(socket, arr[1]);
					topRank.start();
				}

				//pw.println("Server : " + arr[0] + ", "+ arr[1]);
				//pw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private void close() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//pw.close();

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

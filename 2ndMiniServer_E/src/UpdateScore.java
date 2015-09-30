import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UpdateScore extends Thread{

	private Socket socket;
	private PrintWriter pw;
	private String user;
	private String score;

	// not thread -> make util
	public UpdateScore(Socket socket, String user, String score) throws IOException {
		this.socket = socket;
		this.user = user;
		this.score = score;

		// io resoure => reduce, 일일히 다 있을 필요가 없음.
		OutputStream os = socket.getOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		pw = new PrintWriter(bw);
	}

	@Override
	public void run() {
		try {

			System.out.println("update thread");

			synchronized(ServerMain.userSessions){
				String bScore = ServerMain.userSessions.get(user);
				ServerMain.userSessions.put(user, score);
				ServerMain.userSessions = (HashMap<String, String>) sortByValue(ServerMain.userSessions);

				pw.println(user+" : "+bScore+"->"+score);
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

	private Map sortByValue(Map unsortedMap){
		Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}

	private class ValueComparator implements Comparator {
		
		Map map;	// not need : ServerMain.userSessions
		
		public ValueComparator(Map map){
			this.map = map;
		}

		public int compare(Object keyA, Object keyB){
			Comparable valueA = (Comparable) map.get(keyA);
			Comparable valueB = (Comparable) map.get(keyB);
			return valueA.compareTo(valueB);
		}
	}

}

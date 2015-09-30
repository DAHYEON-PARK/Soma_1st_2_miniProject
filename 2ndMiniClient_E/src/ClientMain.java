import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain
{
	public static void main(String[] args) throws UnknownHostException, IOException {

		Socket socket = new Socket("localhost", 9999);

		//Scanner s = new Scanner(System.in);
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		OutputStream os = socket.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		PrintWriter pw = new PrintWriter(osw);

		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		boolean isCommandLineInputWaiting = true;
		String str;

		while (true) {

			if (isCommandLineInputWaiting) {
				//str = s.nextLine();
				str = keyboard.readLine();
				//osw.write(str);
				pw.println(str);
				pw.flush();
				isCommandLineInputWaiting = false;
				continue;
			}

			if (isCommandLineInputWaiting == false) {
				str = br.readLine();
				System.out.println(str);
				isCommandLineInputWaiting = true;
				continue;
			}
		}
	}
}

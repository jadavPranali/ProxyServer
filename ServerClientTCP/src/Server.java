import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args1) {
		try (ServerSocket ss = new ServerSocket(5525)) {
			Socket s = ss.accept();

			DataInputStream dataIn = new DataInputStream(s.getInputStream());
			DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

			BufferedReader conIn = new BufferedReader(new InputStreamReader(System.in));

			String MsgIn = "", MsgOut = "";

			while (!MsgIn.equals("end")) {
				MsgIn = dataIn.readUTF();
				System.out.println(MsgIn);
				MsgOut = conIn.readLine();
				dataOut.writeUTF(MsgOut);
				dataOut.flush();
			}
			s.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);

		}
	}
}

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Proxy {
	public static void main(String[] args) {
		try(ServerSocket proxyServerSocket = new ServerSocket(5525); Socket proxyToServerSocket = new Socket("localhost", 5525)) {
			DataInputStream servertToProxy = new DataInputStream(new BufferedInputStream(proxyToServerSocket.getInputStream()));
			DataOutputStream proxyToServer = new DataOutputStream(new BufferedOutputStream(proxyToServerSocket.getOutputStream()));
			
			Socket clientToProxySocket = proxyServerSocket.accept();
			
			DataInputStream clientToProxy = new DataInputStream(new BufferedInputStream(clientToProxySocket.getInputStream()));
			DataOutputStream proxyToClient = new DataOutputStream(new BufferedOutputStream(clientToProxySocket.getOutputStream()));
			
			while(true) {
				
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.kiley.proxy;

//reader and stream imports
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.kiley.proxy.cmds.CmdConnect;
import com.kiley.proxy.cmds.CmdConnected;
import com.kiley.proxy.cmds.CmdCrash;
import com.kiley.proxy.cmds.CmdMessage;

public class Client {
	private static final String MAIN_MENU_TITLE = "Main Menu";
	private static final String MAIN_MENU_OPTION_DIRECT_REGEX = "1";
	private static final String MAIN_MENU_OPTION_PROXY_REGEX = "2";
	private static final String MAIN_MENU_OPTION_DIRECT = "Connect to Server Directly";
	private static final String MAIN_MENU_OPTION_PROXY = "Connect To Server Through Proxy";
	private static final String[] MAIN_MENU_OPTIONS = new String[] {
			MAIN_MENU_OPTION_DIRECT_REGEX + ") " + MAIN_MENU_OPTION_DIRECT,
			MAIN_MENU_OPTION_PROXY_REGEX + ") " + MAIN_MENU_OPTION_PROXY };

	private static final String DIRECT_CONNECT_MENU_TITLE = "Direct Connect%nEnter the IP Address";

	private static final String PROXY_CONNECT_MENU_TITLE = "Proxy Connect%nEnter the IP Address of the Proxy Server";

	private static final String PROXY_READY_MENU_TITLE = "Connected to Proxy%nEnter the IP Address of the Server";

	private static final String READY_MENU_TITLE = "Connected to Server";
	private static final String READY_MENU_OPTION_CHAT_REGEX = "1";
	private static final String READY_MENU_OPTION_EXIT_REGEX = "2";
	private static final String READY_MENU_OPTION_CRASH_REGEX = "3";
	private static final String READY_MENU_OPTION_CHAT = "Send Message to the Server";
	private static final String READY_MENU_OPTION_EXIT = "Disconnect from Server";
	private static final String READY_MENU_OPTION_CRASH = "Crash the Server";
	private static final String[] READY_MENU_OPTIONS = new String[] {
			READY_MENU_OPTION_CHAT_REGEX + ") " + READY_MENU_OPTION_CHAT,
			READY_MENU_OPTION_EXIT_REGEX + ") " + READY_MENU_OPTION_EXIT,
			READY_MENU_OPTION_CRASH_REGEX + ") " + READY_MENU_OPTION_CRASH };

	private static final String READY_MENU_CHAT = "Enter your Message";

	private Scanner scan = new Scanner(System.in);

	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;

	public Client() {
		this.startMainMenu();
	}

	private void close() {
		this.scan.close();
	}

	private void startMainMenu() {
		System.out.print(this.formatMainMenu());
		String input = this.scan.next();
		try {
			if (input.contains(MAIN_MENU_OPTION_DIRECT_REGEX)) {
				this.startDirectConnectMenu();
			} else if (input.contains(MAIN_MENU_OPTION_PROXY_REGEX)) {
				this.startProxyConnectMenu();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void startDirectConnectMenu() throws UnknownHostException, IOException, ClassNotFoundException {
		System.out.print(this.formatDirectConnectMenu());
		String input = this.scan.next();
		try (Socket socket = new Socket(input, Ports.MAIN_PORT)) {
			this.objIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			this.objOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

			this.startReadyMenu();
		}
	}

	private void startProxyConnectMenu() throws UnknownHostException, IOException, ClassNotFoundException {
		System.out.print(this.formatProxyConnectMenu());
		String input = this.scan.next();
		try (Socket socket = new Socket(input, Ports.MAIN_PORT)) {
			this.objIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			this.objOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

			this.startProxyReadyMenu();
		}
	}

	private void startProxyReadyMenu() throws IOException, ClassNotFoundException {
		System.out.print(this.formatProxyReadyMenu());
		String inputFromScanner = this.scan.next();
		this.objOut.writeObject(new CmdConnect(inputFromScanner));
		Object inputFromProxy = this.objIn.readObject();
		if (inputFromProxy.getClass().equals(CmdConnected.class)) {
			CmdConnected cmd = (CmdConnected) inputFromProxy;

			if (cmd.didSucceed()) {
				this.startReadyMenu();
			}
		}
	}

	private void startReadyMenu() throws IOException, ClassNotFoundException {
		boolean readyLoop = true;
		while (readyLoop) {
			System.out.print(this.formatReadyMenu());
			String inputFromScanner = this.scan.next();
			if (inputFromScanner.contains(READY_MENU_OPTION_CHAT_REGEX)) {
				System.out.print(this.formatReadyMenuChat());
				inputFromScanner = this.scan.nextLine();
				this.objOut.writeObject(new CmdMessage(inputFromScanner));
				Object inputFromServer = this.objIn.readObject();
				if (inputFromServer.getClass().equals(CmdMessage.class)) {
					CmdMessage cmd = (CmdMessage) inputFromServer;
					System.out.print(this.formatMessage(cmd.getMsg()));
				}
			} else if (inputFromScanner.contains(READY_MENU_OPTION_EXIT_REGEX)) {
				readyLoop = false;
			} else if (inputFromScanner.contains(READY_MENU_OPTION_CRASH_REGEX)) {
				this.objOut.writeObject(new CmdCrash());
				readyLoop = false;
			}
		}
	}

	private String formatMainMenu() {
		return String.format(this.formatTitle(MAIN_MENU_TITLE) + this.formatOptions(MAIN_MENU_OPTIONS));
	}

	private String formatDirectConnectMenu() {
		return String.format(this.formatTitle(DIRECT_CONNECT_MENU_TITLE));
	}

	private String formatProxyConnectMenu() {
		return String.format(this.formatTitle(PROXY_CONNECT_MENU_TITLE));
	}

	private String formatProxyReadyMenu() {
		return String.format(this.formatTitle(PROXY_READY_MENU_TITLE));
	}

	private String formatReadyMenu() {
		return String.format(this.formatTitle(READY_MENU_TITLE) + this.formatOptions(READY_MENU_OPTIONS));
	}

	private String formatReadyMenuChat() {
		return String.format(this.formatTitle(READY_MENU_CHAT));
	}

	private String formatMessage(String msg) {
		return String.format(this.formatTitle(msg));
	}

	private String formatTitle(String title) {
		return "%n" + title + "%n";
	}

	private String formatOptions(String[] options) {
		String s = "";
		for (String option : options) {
			s += option + "%n";
		}
		return s;
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.close();
		// try (Socket s = new Socket("localhost", 5525)) {
		// ObjectInputStream dataIn = new ObjectInputStream(new
		// BufferedInputStream(s.getInputStream()));
		// ObjectOutputStream dataOut = new ObjectOutputStream(new
		// BufferedOutputStream(s.getOutputStream()));
		//
		// String MsgIn = "", MsgOut = "";
		//
		// System.out.println("IP to connect to: ");
		//
		// String ip = conIn.next();
		//
		// dataOut.writeObject(new CmdConnect(ip));
		//
		// while (!MsgIn.equals("end")) {
		// MsgOut = conIn.nextLine();
		// dataOut.writeUTF(MsgOut);
		// MsgIn = dataIn.readUTF();
		// System.out.println(MsgIn);
		// }
		// } catch (Exception e) {
		// System.out.println(e.getMessage());
		// System.exit(1);
		//
		// }
	}
}

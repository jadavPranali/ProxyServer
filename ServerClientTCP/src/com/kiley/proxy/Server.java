package com.kiley.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.kiley.proxy.cmds.CmdMessage;

public class Server {
	public Server() {
		try (Scanner scan = new Scanner(System.in)) {
			boolean listening = true;

			while (listening) {
				try (ServerSocket serverSocket = new ServerSocket(Ports.MAIN_PORT)) {
					try (Socket socket = serverSocket.accept()) {
						ObjectOutputStream objOut = new ObjectOutputStream(
								new BufferedOutputStream(socket.getOutputStream()));
						objOut.flush();
						ObjectInputStream objIn = new ObjectInputStream(
								new BufferedInputStream(socket.getInputStream()));

						boolean serverLoop = true;

						while (serverLoop) {
							if (socket.isClosed()) {
								break;
							}
							Object inputFromClient = objIn.readObject();
							if (inputFromClient.getClass().equals(CmdMessage.class)) {
								CmdMessage cmd = (CmdMessage) inputFromClient;

								System.out.println(cmd.getMsg());

								System.out.println("Response?");

								String inputFromScanner = scan.nextLine();

								objOut.writeObject(new CmdMessage(inputFromScanner));
								objOut.flush();
							} else {
								System.out.println("Unknown Type");
							}
						}
					}
				} catch (EOFException e) {
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args1) {
		new Server();
	}
}

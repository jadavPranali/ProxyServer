package com.kiley.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.kiley.proxy.cmds.CmdConnect;
import com.kiley.proxy.cmds.CmdConnected;

public class Proxy {
	public Proxy() {
		boolean listening = true;

		while (listening) {
			try (ServerSocket serverSocket = new ServerSocket(Ports.PROXY_PORT)) {
				Socket clientSocket = serverSocket.accept();

				ObjectOutputStream proxyToClient = new ObjectOutputStream(
						new BufferedOutputStream(clientSocket.getOutputStream()));
				proxyToClient.flush();
				ObjectInputStream clientToProxy = new ObjectInputStream(
						new BufferedInputStream(clientSocket.getInputStream()));

				Object inputFromClient = clientToProxy.readObject();
				if (inputFromClient.getClass().equals(CmdConnect.class)) {
					CmdConnect cmd = (CmdConnect) inputFromClient;

					boolean setup = false;

					try (Socket socketToServer = new Socket(cmd.getTargetIp(), Ports.MAIN_PORT)) {
						ObjectOutputStream proxyToServer = new ObjectOutputStream(
								new BufferedOutputStream(socketToServer.getOutputStream()));
						proxyToServer.flush();
						ObjectInputStream serverToProxy = new ObjectInputStream(
								new BufferedInputStream(socketToServer.getInputStream()));

						proxyToClient.writeObject(new CmdConnected(true));
						proxyToClient.flush();
						setup = true;

						boolean proxyLoop = true;

						while (proxyLoop) {
							if(clientSocket.isClosed()) {
								break;
							}
							System.out.println("Client To Server");
							proxyToServer.writeObject(clientToProxy.readObject());
							System.out.println("Flushing");
							proxyToServer.flush();

							if(socketToServer.isClosed()) {
								break;
							}
							System.out.println("Server to Client");
							proxyToClient.writeObject(serverToProxy.readObject());
							System.out.println("Flushing");
							proxyToClient.flush();
						}
					} finally {
						if (setup) {
							proxyToClient.writeObject(new CmdConnected(false));
							proxyToClient.flush();
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

	public static void main(String[] args) {
		new Proxy();
	}
}

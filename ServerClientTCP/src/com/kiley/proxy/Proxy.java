package com.kiley.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.kiley.proxy.cmds.CmdConnect;
import com.kiley.proxy.cmds.CmdMessage;

public class Proxy {
	public static void main(String[] args) {
		try (ServerSocket proxyServerSocket = new ServerSocket(5525);
				Socket proxyToServerSocket = new Socket("localhost", 5525)) {
			ObjectInputStream servertToProxy = new ObjectInputStream(
					new BufferedInputStream(proxyToServerSocket.getInputStream()));
			ObjectOutputStream proxyToServer = new ObjectOutputStream(
					new BufferedOutputStream(proxyToServerSocket.getOutputStream()));

			Socket clientToProxySocket = proxyServerSocket.accept();

			ObjectInputStream clientToProxy = new ObjectInputStream(
					new BufferedInputStream(clientToProxySocket.getInputStream()));
			ObjectOutputStream proxyToClient = new ObjectOutputStream(
					new BufferedOutputStream(clientToProxySocket.getOutputStream()));

			while (true) {
				Object cmd = clientToProxy.readObject();
				if (cmd.getClass() == CmdConnect.class) {
					CmdConnect cmdCon = (CmdConnect) cmd;

				} else if (cmd.getClass() == CmdMessage.class) {
					CmdMessage cmdMsg = (CmdMessage) cmd;
					
				} else {
					System.out.println("Unhandled Cmd");
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private ServerSocket serverSocketToClient;

	private Socket socketToClient;

	private ObjectInputStream clientToProxy;
	private ObjectOutputStream proxyToClient;

	private Socket socketToServer;

	private ObjectInputStream serverToProxy;
	private ObjectOutputStream proxyToServer;

	public Proxy(int port) throws IOException {
		this.setupServerSocket(port);
	}

	private void setupServerSocket(int port) throws IOException {
		this.serverSocketToClient = new ServerSocket(port);
	}

	public void waitForConnect() throws IOException {
		this.socketToClient = this.serverSocketToClient.accept();
	}

}

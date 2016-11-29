package com.kiley.proxy;

//reader and stream imports
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.kiley.proxy.cmds.CmdConnect;

public class Client {
	public static void main(String[] args) {
		try (Socket s = new Socket("localhost", 5525); Scanner conIn = new Scanner(System.in)) {
			ObjectInputStream dataIn = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			ObjectOutputStream dataOut = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));

			String MsgIn = "", MsgOut = "";
			
			System.out.println("IP to connect to: ");
			
			String ip = conIn.next();
			
			dataOut.writeObject(new CmdConnect(ip));

			while (!MsgIn.equals("end")) {
				MsgOut = conIn.nextLine();
				dataOut.writeUTF(MsgOut);
				MsgIn = dataIn.readUTF();
				System.out.println(MsgIn);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);

		}
	}
}

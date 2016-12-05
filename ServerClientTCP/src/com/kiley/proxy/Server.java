package com.kiley.proxy;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
	public static void main(String[] args1) {
		try (ServerSocket ss = new ServerSocket(5525); Scanner conIn = new Scanner(System.in)) {
			Socket s = ss.accept();

			ObjectInputStream dataIn = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			ObjectOutputStream dataOut = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));

			String MsgIn = "", MsgOut = "";

			while (!MsgIn.equals("end")) {
				MsgIn = dataIn.readUTF();
				System.out.println(MsgIn);
				MsgOut = conIn.nextLine();
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

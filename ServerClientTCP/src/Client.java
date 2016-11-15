
import java.io.*;  //reader and stream imports

import java.net.*;

import javax.xml.crypto.Data;

public class Client {
     public static void main(String [] args){
    	 try{
    		 Socket s = new Socket("localhost",5525);
    		 
    		 DataInputStream dataIn = new DataInputStream(s.getInputStream());
    		 DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
    		 
    		 BufferedReader conIn = new BufferedReader(new InputStreamReader(System.in));
    		 String MsgIn="",MsgOut="";
    		 
    		 while(!MsgIn.equals("end")){
    			 MsgOut = conIn.readLine();
    			 dataOut.writeUTF(MsgOut);
    			 MsgIn = dataIn.readUTF();
    			 System.out.println(MsgIn);
    		 }
    	 }catch(Exception e){
    		 System.out.println(e.getMessage());
     		System.exit(1);
    		 
    	 }
     }
     
}



import java.io.*;
import java.net.*;
class MyServer{
	public static void main(String[] args)throws Exception{
			ServerSocket ss = new ServerSocket(3333);
			Socket s = ss.accept(); //establishes connection
			DataInputStream din = new DataInputStream(s.getInputStream());
			DataOutputStream dout= new DataOutputStream(s.getOutputStream());
			String str = din.readUTF();
			if(str.equals("HELO")){
				dout.writeUTF("G'DAY");
				System.out.println("Client says: "+ str);
				str = din.readUTF();
				if (str.equals("G'DAY")){
					System.out.println("Client says: "+ str);
					dout.writeUTF("BYE");	
				}
				str = din.readUTF();
				System.out.println("Client says: "+ str);
			}
			din.close();
			s.close();
			ss.close();
	}	
}



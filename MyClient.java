import java.io.*;
import java.net.*;
class MyClient{
	public static void main(String[] args)throws Exception{
			Socket s = new Socket("localhost",3333);
			DataInputStream din = new DataInputStream(s.getInputStream());
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.writeUTF("HELO");
			String str = din.readUTF();
			if(str.equals("G'DAY")){
				System.out.println("Server says: "+ str);
				dout.writeUTF("G'DAY");
				str = din.readUTF();
				if (str.equals("BYE")){	
					System.out.println("Server says: "+ str);
					dout.writeUTF("BYE");	
				}
			}
			dout.flush();
			dout.close();
			s.close();
	}
}

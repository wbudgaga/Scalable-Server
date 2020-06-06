package cs455.scaling.test;

import java.io.IOException;
import java.net.Socket;

public class ServerTest {
	private Socket		 			connection1;
	private Socket		 			connection2;
	private Socket		 			connection3;
	private Socket		 			connection4;
	private Socket		 			connection5;
	
	public boolean connect(String hostAddress,int  connectingPort) throws IOException{
		connection1 = new Socket(hostAddress, connectingPort);
		if (connection1 == null)
			return false;
		return true;
	}

	public boolean connect1(String hostAddress,int  connectingPort) throws IOException{
		connection2 = new Socket(hostAddress, connectingPort);
		if (connection2 == null)
			return false;
		return true;
	}

	public void sendMessage(String msg, Socket connection) throws IOException {		
		if (connection != null){
			connection.getOutputStream().write(msg.getBytes());
			connection.getOutputStream().flush();
		}
		else
			System.out.println(" connectin is null");
	}
	public void start() throws IOException{
		if (!connect("localhost",8000)){
			System.out.println("couldn't connect ..");
			return;
		}
/*		
		if (!connect1("localhost",8000)){
			System.out.println("couldn't connect1 ..");
			return;
		}
		sendMessage("wwwwwwwwww",connection2);

		sendMessage("hello",connection1);
		sendMessage("hello1",connection1);
		sendMessage("hello2",connection1);
*/		
		byte[] msg = new byte[250];
		int i =0;
		while (i<100){
			System.out.println("sending..........");
			sendMessage("hello"+i,connection1);
		//	sendMessage("hello3",connection1);
			int b = connection1.getInputStream().read(msg);
			if (b==-1){
				System.out.println("############  problem by reading " );
				break;
			}
			String s = new String(msg);
			System.out.println("############  " + s);
			++i;
		}
	}
	
	
	public static void main(String args[]) throws IOException{
		ServerTest s = new ServerTest();
		s.start();
	}
}

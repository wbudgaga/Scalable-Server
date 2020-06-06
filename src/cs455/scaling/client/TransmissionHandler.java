package cs455.scaling.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import cs455.scaling.util.ByteStream;

public class TransmissionHandler extends Thread{
	protected 	Socket	 		socketConnection;
	private 	InputStream 	inStream;
	private 	OutputStream 	outStream;
	private 	Client 			client;
	
	public  TransmissionHandler(Client client) throws IOException {
		this.client 		= client;
	}

	protected void connect(String serverHost, int port) throws IOException{
		socketConnection =  new Socket(serverHost, port);
		if (socketConnection== null)
			throw new SocketException("It could not connect to the server with the given address");

		this.inStream 		= socketConnection.getInputStream();
		this.outStream 		= socketConnection.getOutputStream();

	}
	
	protected void sendMessage(byte[] data) throws IOException{
		if (outStream== null)
			throw new SocketException("the connection to the server is broken");
		
		outStream.write(data);
		outStream.flush();
	}
	
	private byte[] readMessageBody(int bodyLength) throws SocketException, IOException{
		int totalBytesRcvd 		= 0;  // Total bytes received so far
		byte[] byteBuffer		= new byte[bodyLength];
		int bytesRcvd;           // Bytes received in last read

		while (totalBytesRcvd < bodyLength) {
		      if ((bytesRcvd = inStream.read(byteBuffer, totalBytesRcvd, bodyLength - totalBytesRcvd)) == -1)
		    	  throw new SocketException("Connection close prematurely");
		      
		      totalBytesRcvd += bytesRcvd;
		}
	    return byteBuffer;
	}
	
	 private void receivingMessage() throws SocketException, IOException {
		byte[] byteBuffer	= new byte[4];	 
		while (true){
			if (inStream.read(byteBuffer, 0,4)<4)
				throw new SocketException("Connection close prematurely");
			
			int bodyLength 	= ByteStream.byteArrayToInt(byteBuffer); // reading the message length and removing the message header
			
			client.dataReceivedCallbackFunction(ByteStream.byteArrayToString(readMessageBody(bodyLength)));
		}
	}

	 
	public void run(){
		while (true){
			try {
				receivingMessage();
			} catch (SocketException e) {
				System.err.println(e.getMessage());
				client.keepRunning = false;
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	

}

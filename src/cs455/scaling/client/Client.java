package cs455.scaling.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;

import cs455.scaling.server.ScalingServer;
import cs455.scaling.util.RandomData;
import cs455.scaling.util.SHA1;

public class Client {
	
	private 	TransmissionHandler handler;
	private 	int 				messageRate;
	private 	Queue<String> 		sentDataTrackerQueue = new LinkedList<String>();
	private		int 				messageCounter = 0;
	protected   boolean				keepRunning = true;
	
	public Client(int messageRate){
		this.messageRate = messageRate;
	}
	
	private void start(String serverHost, int port) throws IOException, InterruptedException, NoSuchAlgorithmException{
		handler = new TransmissionHandler(this);
		handler.connect(serverHost, port);
		handler.start();
		System.out.println("Client at "+InetAddress.getLocalHost().getHostName()+" running " );
		while(keepRunning){
			Thread.sleep(1000/messageRate);
			messageCounter++;
			sendMessage();
		}
	}
	
	private void sendMessage() throws NoSuchAlgorithmException, IOException{
		byte[] dataToSend 			= RandomData.getRandomData(ScalingServer.BUFFERSIZE);
		String hashCodeOfSentData 	= SHA1.fromBytes(dataToSend);
		handler.sendMessage(dataToSend);
		sentDataTrackerQueue.offer(hashCodeOfSentData);
		System.out.println("[Msg-"+messageCounter+"] Sent    : "+hashCodeOfSentData);
	}
	
	protected void dataReceivedCallbackFunction(String hashCodeOfReceivedData){
		String hashCodeOfSentData = sentDataTrackerQueue.poll();
		System.out.println("[Msg-"+messageCounter+"] Received: "+hashCodeOfReceivedData);
		if(hashCodeOfReceivedData.compareTo(hashCodeOfSentData)!=0)
			System.out.println("[ClientStatus] Message "+messageCounter+" transmitted incorrectly\n"); 
		else
			System.out.println("[ClientStatus] Message "+messageCounter+" transmitted correctly\n"); 
	}
	
	public static void main(String args[]){
		if (args.length < 2) {
			System.err.println("Server:  Usage:");
			System.err.println("   java cs455.scaling.client.Client server-host server-port [message-rate]");
		    return;
		}
		
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		int messageRate 	= ScalingServer.MESSAGERATE; // read it from interface
		if (args.length == 3)
			messageRate  =  Integer.parseInt(args[2]);
			
		try {
			Client client = new Client(messageRate);
			client.start(host, port);
		} catch (SocketException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}	
	}

}

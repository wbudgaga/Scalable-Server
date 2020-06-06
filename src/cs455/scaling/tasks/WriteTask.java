package cs455.scaling.tasks;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

import cs455.scaling.server.Statistic;
import cs455.scaling.util.ByteStream;
import cs455.scaling.util.SHA1;

public class WriteTask extends TaskType{
	
	@Override
	public boolean execute(Task task){
		try {
			String dataHashCode 	= SHA1.fromBytes(((TaskImpl) task).buffer.array());
			byte[] data 			= ByteStream.packString(dataHashCode);
			ByteBuffer outputBuffer	=  ByteBuffer.wrap(data);
			SocketChannel channel  	= (SocketChannel) ((TaskImpl) task).key.channel();
			// Make sure that the buffer is fully drained
			while (outputBuffer.hasRemaining( )) {
				channel.write (outputBuffer);
			}
			
			Statistic.incNumberOfSentPackets();
			if(Statistic.timeToPrint()){
				String hsotName = channel.socket().getInetAddress().getHostName();
				System.out.println("\n[ClientMessage-"+hsotName+"] Hash:"+dataHashCode);
				System.out.println("[ServerStatus] Message from Client at "+ hsotName +" was handled by " + Thread.currentThread().getName());
			}
			
		} catch (NoSuchAlgorithmException e1) {
			return false;
		} catch (ClosedChannelException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public TaskType nextTask() {
		return new ReadTask();
	}

	@Override
	public int getOperationKey() {
		return SelectionKey.OP_WRITE;
	}
}

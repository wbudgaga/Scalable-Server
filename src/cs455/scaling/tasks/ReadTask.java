package cs455.scaling.tasks;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.server.ScalingServer;

public class ReadTask  extends TaskType{

	@Override
	// There is pending data on socket channel ready to read
	public boolean execute(Task task) {
		int bytesRead=0; 
		try {
			ByteBuffer inputBuffer = ((TaskImpl) task).buffer;
			SocketChannel channel  = (SocketChannel) ((TaskImpl) task).key.channel();
			inputBuffer.clear();
			while(bytesRead < ScalingServer.BUFFERSIZE){
			    int bytes = channel.read(inputBuffer);
			    if (bytes == -1)
			    	return false;
				bytesRead += bytes;
			}			
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public TaskType nextTask() {
		// Return the new write task in order to send a response on the same channel.
		return new WriteTask();
	}
	
	@Override
	public int getOperationKey() {
		return  SelectionKey.OP_READ;
	}
}

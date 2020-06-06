package cs455.scaling.tasks;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


// this class should be a separate task class that creates TaskImpl
public class AcceptTask implements Task{
	private 	SelectionKey		key;
	
	public void  setKey(SelectionKey key){
		this.key = key;
	}
	
	@Override
	public synchronized void execute() {
		try {
			ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
			if (serverChannel == null)
				return;
			// Accept a remote connection and put it in non-blocking mode
				SocketChannel socketChannel = serverChannel.accept();
			    if (socketChannel == null)
			    	return;
			    			
			    socketChannel.configureBlocking(false);	
			//  create new task whose type changes between ReadTask and WriteTask 
			    TaskImpl task 			= new TaskImpl();
			    // register the new task as ReadTask
			    SelectionKey  newkey 	= socketChannel.register(key.selector(),  SelectionKey.OP_READ,task);
			    task.setKey(newkey);
			    key.selector().wakeup();
		}catch(IOException ioEx) { }
	}
}

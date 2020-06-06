package cs455.scaling.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import cs455.scaling.tasks.AcceptTask;
import cs455.scaling.tasks.Task;
import cs455.scaling.threadpool.ThreadPoolManager;

public class Server {

	private final Selector 				selector;
	private final ServerSocketChannel 	serverChannel;
	private 	  ThreadPoolManager 	threadPool;
	private 	  Statistic 			report; 
	public Server(int port) throws IOException {
		selector = Selector.open();
	 
	    serverChannel = ServerSocketChannel.open();
	    initServerChannel(port);    
	}
	
	private void initServerChannel(int port) throws IOException{
		// Set the server channel in non-blocking mode 
		serverChannel.configureBlocking(false);
		InetSocketAddress address = new InetSocketAddress(port);
	    // Bind the server socket to the specified port on the local machine
		serverChannel.socket().bind(address);
	}
	
	//Starting the thread pool manager
	private void startThreadPool(int threadPoolSize){
		threadPool = new ThreadPoolManager(threadPoolSize);
		threadPool.start();
	}
	
	protected void printReport(){
		System.out.println("\nThread pool size :"+threadPool.getSize());
		System.out.println("Total clients connected:"+ (selector.keys().size() - 1));
		System.out.println("Packets/Second:"+report.getNumberOfPacketsInSecond());
		System.out.println("Server Uptime:"+report.getUptime());
	}
	
	private void start() throws IOException{
		report = new Statistic();
		new TimerThread(this, ScalingServer.SERVER_PRINTING_RATE).start();
		// create and attach accept task with the selection key of server channel 
		Task atask = new AcceptTask();
		SelectionKey acceptSelectionKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT, atask);
		atask.setKey(acceptSelectionKey);
		System.out.println("Server at "+InetAddress.getLocalHost().getHostName()+" running " );
		
		while(true){
			if (selector.select(100)==0)  continue;
			
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			
			Iterator<SelectionKey> selectedKeysIter = selectedKeys.iterator();
			while (selectedKeysIter.hasNext()){ 
				SelectionKey sKey 	= (SelectionKey)selectedKeysIter.next();
				Task task 			= (Task) sKey.attachment();
				threadPool.addTask(task);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			selectedKeys.clear();
		}
	}
	
	protected void startup(int threadPoolSize) throws IOException{
		startThreadPool(threadPoolSize);
		start();
	}

	public static void main(String args[]){
		if (args.length < 1) {
			System.err.println("Server:  Usage:");
			System.err.println("       java cs455.scaling.server.Server portnum [thread-pool-size]");
		    return;
		}
		
		int port = Integer.parseInt(args[0]);		
		
		int threadPoolSize 	= 10; // read it from interface
		if (args.length == 2)
			threadPoolSize  =  Integer.parseInt(args[1]);
		if (threadPoolSize < 1)	{
			System.err.println("The server must have  thread pool of size at least one");
			return;
		}
		try {
			Server server = new Server(port);
			server.startup(threadPoolSize);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}

package cs455.scaling.server;


// the class is a thread class that is used to update the weight in in fixed timeInterval time.
// the thread will not started if the timeInterval = 0
public class TimerThread extends Thread{
	private Server 	server;
	private long 		timeInterval;
	private boolean 	keepRunning;
	public TimerThread(Server server, long timeInterval){
		this.server 		=  	server;
		this.timeInterval 	=  	timeInterval;
		if (timeInterval > 0)
			this.keepRunning	=	true;
		else
			this.keepRunning	=	false;
	}
	
	protected void stopRunning(){
		this.keepRunning	=	false;
	}
	
	@Override
	public void run() {
		int pauseTime = 0;
		while (keepRunning){
			try {
				sleep(timeInterval - pauseTime);
				Statistic.setTimeToPrint(true);
				server.printReport();// calling printReport() to print info about # of connections and sending packets
				pauseTime = 500;
				sleep(pauseTime);
				Statistic.setTimeToPrint(false);

			} catch (InterruptedException e) {
			}
		}
	}
}

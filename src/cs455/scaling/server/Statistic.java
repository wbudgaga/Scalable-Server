package cs455.scaling.server;

import java.util.Date;

public class Statistic {
	static private volatile int 		numberOfSentPackets	= 0;
	static private volatile boolean 	timeToPrint 		= false;
	private 	   volatile long 		startTime			= new Date().getTime();
	
	
	public Statistic(){
	}
	
	public static synchronized void incNumberOfSentPackets(){
		++ numberOfSentPackets;
	}
	
	private long getDauar(){
		long now =  new Date().getTime();
		return (now - startTime)/1000;
	}
	
	public int getNumberOfPacketsInSecond(){
		return (int) (numberOfSentPackets/getDauar());
	}
	
	public String getUptime(){
		long time = getDauar();
		String seconds 	= Integer.toString((int)(time % 60));  
		String minutes 	= Integer.toString((int)((time % 3600) / 60));  
		String hours 	= Integer.toString((int)(time / 3600));  

		return hours+":"+minutes+":"+seconds;
	}
	
	public static void setTimeToPrint(boolean status){
		timeToPrint = status;
	}
	
	public static boolean timeToPrint(){
		return timeToPrint;
	}

}

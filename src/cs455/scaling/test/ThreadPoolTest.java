package cs455.scaling.test;

import java.nio.channels.SelectionKey;
import java.util.Vector;

import cs455.scaling.tasks.Task;
import cs455.scaling.threadpool.ThreadPoolManager;

public class ThreadPoolTest {
	public Vector<String> s = new Vector<String>();
	class Job implements Task{
		private String myJob;
		public Job(String job){
			myJob = job;
		}

		@Override
		public void execute() {
			//System.out.println(myJob);
			String m = Thread.currentThread().getName();
			s.add(m+" "+ myJob);
			
		}

		@Override
		public void setKey(SelectionKey key) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public void startup() throws InterruptedException{
		 ThreadPoolManager tpm = new ThreadPoolManager(10);
		 tpm.start();
		 
		 for (int i=0; i<100;++i){
			 tpm.addTask(new Job("job-"+i));
		 }
		 
		 synchronized(this){
		 for(String st:s)
			 System.out.println(st);
		 }
		 tpm.stop();
	}
	
	 public static void main(String args[]) throws InterruptedException{
		 ThreadPoolTest tst = new ThreadPoolTest();
		 tst.startup();
	 }

}

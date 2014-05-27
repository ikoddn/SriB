package no.srib.app.client.util;

public class AsyncTaskCompleted {

	private int NUMBER_ASYNCTASKS;
	private int count;
	private AsyncTaskFinished asyncTaskCompleted;
	
	public AsyncTaskCompleted(final AsyncTaskFinished asyncTask, final int NUMBER_ASYNCTASK) {
		this.NUMBER_ASYNCTASKS = NUMBER_ASYNCTASK;
		count = 0;
		asyncTaskCompleted = asyncTask;
	}
	
	public void increaseCount(){
		count++;
		if(count == NUMBER_ASYNCTASKS){
			asyncTaskCompleted.onFinished();
		}
	}
	
	public interface AsyncTaskFinished{
		
		public void onFinished();
	}

}


package cs455.scaling.tasks;

public abstract class TaskType {
	public abstract TaskType 			nextTask();
	public abstract	int 				getOperationKey();
	public abstract boolean				execute(Task task);
}

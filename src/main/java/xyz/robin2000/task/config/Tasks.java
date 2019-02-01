package xyz.robin2000.task.config;

import java.util.List;

public class Tasks {
	private String title;
	private List<Task> task;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Task> getTask() {
		return task;
	}

	public void setTask(List<Task> task) {
		this.task = task;
	}
}

package TodoApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodoItem {
	private int no;
	private Status status;
	private String title;
	private String description;
	private LocalDate deadline;
	private LocalDateTime statusChangedAt;
	private static int nextNo = 1;
	
	public TodoItem(String title, String description, LocalDate deadline) {
		this.no = nextNo++;
		status = Status.TODO;
		this.title = title;
		this.description = description;
		this.deadline = deadline;
		statusChangedAt = LocalDateTime.now();
		
	}
	
	public TodoItem(int no, Status status, String title,
            String description, LocalDate deadline,
            LocalDateTime statusChangedAt) {
		this.no = no;
		this.status = status;
		this.title = title;
		this.description = description;
		this.deadline = deadline;
		this.statusChangedAt = statusChangedAt;
}
	
	public enum Status {
	    TODO,
	    DOING,
	    DONE
	}
	
	public int getNo() {
		return no;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public String getFormattedDeadline() {
		return deadline.format(formatter);
	}
	
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	public LocalDateTime getStatusChangedAt() {
	    return statusChangedAt;
	}
	
	public void setStatusChangedAt() {
		statusChangedAt = LocalDateTime.now();
	}
}

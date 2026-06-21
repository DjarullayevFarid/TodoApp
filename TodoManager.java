package TodoApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import TodoApp.TodoItem.Status;
import java.time.format.DateTimeFormatter;

public class TodoManager implements ITodoManager {
	private ArrayList<TodoItem> todoItems;
	
	public TodoManager(ArrayList<TodoItem> todoItems) {
		this.todoItems = todoItems;
	}
	
	public void addTodoItem(TodoItem todoItem) {
		todoItems.add(todoItem);
	}
	
	public void getAllTodoItems() {
		int count = 1;
		for (TodoItem todoItem : todoItems) {
			System.out.println(count + ") №" + todoItem.getNo() + " | " + todoItem.getTitle() + " | " + todoItem.getDescription() + " | " + todoItem.getStatus() + " | " + todoItem.getFormattedDeadline());
			count++;
		}
		System.out.println();
	}
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public void getAllDelayedTasks() {
		int count = 1;
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getDeadline().isBefore(LocalDate.now())) {
				System.out.println(count + ") №" + todoItem.getNo() + " | " + todoItem.getTitle() + " - " + todoItem.getFormattedDeadline());
				count++;
			}
		}
		System.out.println();
	}
	
	public void changeTodoItemStatus(int no, Status status) {
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getNo() == no) {
				if (todoItem.getStatus() == Status.TODO && status == Status.DOING) {
					todoItem.setStatus(status);
					todoItem.setStatusChangedAt();
					System.out.println("№" + todoItem.getNo() + " todo's status has been successfully changed to " + status);
				} else if (todoItem.getStatus() == Status.DOING && status == Status.DONE) {
					todoItem.setStatus(status);
					todoItem.setStatusChangedAt();
					System.out.println("№" + todoItem.getNo() + " todo's status has been successfully changed to " + status);
				}
				break;
			}
		}
		System.out.println();
	}
	
	public void editTodoItem(int no, String title, String description, LocalDate deadline) {
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getNo() == no) {
				if(title != null)
				    todoItem.setTitle(title);
				if(description != null)
				    todoItem.setDescription(description);
				if(deadline != null)
				    todoItem.setDeadline(deadline);
				break;
			}
		}
	}
	
	public void deleteTodoItem(int no) {
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getNo() == no) {
				todoItems.remove(no - 1);
				break;
			}
		}
	}
	
	public void GetAllTodoItemsByStatus(Status status) {
		int count = 1;
		System.out.println("All the todos with " + status + " status:");
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getStatus() == status) {
				System.out.println(count + ") №" + todoItem.getNo() + " | " + todoItem.getTitle() + " | " + todoItem.getDescription() + " | " + todoItem.getStatus() + " | " + todoItem.getFormattedDeadline());
				count++;
			}
		}
		System.out.println();
	}
}
package TodoApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import TodoApp.TodoItem.Status;
import TodoApp.exceptions.InvalidStatusTransitionException;
import TodoApp.exceptions.TaskNotFoundException;

import java.time.format.DateTimeFormatter;

public class TodoManager implements ITodoManager {
	private ArrayList<TodoItem> todoItems;
	
	public TodoManager(ArrayList<TodoItem> todoItems) {
		this.todoItems = todoItems;
	}
	
	public void addTodoItem(TodoItem todoItem) {
		todoItems.add(todoItem);
	}
	
	public void showTodoItem(int count, TodoItem todoItem) {
		System.out.println(count + ") №" + todoItem.getNo() + " | " 
				+ todoItem.getTitle() + " | " + todoItem.getDescription() + " | " 
				+ todoItem.getStatus() + " | " 
				+ todoItem.getFormattedDeadline());
	}
	
	public void getAllTodoItems() {
		int count = 1;
		for (TodoItem todoItem : todoItems) {
			showTodoItem(count, todoItem);
			count++;
		}
		System.out.println();
	}
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public void getAllDelayedTasks() {
		int count = 1;
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getDeadline().isBefore(LocalDate.now()) && todoItem.getStatus() != Status.DONE) {
				showTodoItem(count, todoItem);
				count++;
			}
		}
		System.out.println();
	}
	
	public void changeTodoItemStatus(int no, Status status) {
		boolean isFound = false;
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getNo() == no) {
				if ((todoItem.getStatus() == Status.TODO 
					&& status != Status.DOING) || 
					(todoItem.getStatus() == Status.DOING 
					&& status != Status.DONE))
						throw new InvalidStatusTransitionException(
							"Unable to change status from " + todoItem.getStatus() + " to " + status + ".");
				if (todoItem.getStatus() == Status.TODO 
					&& status == Status.DOING) {
					todoItem.setStatus(status);
					todoItem.setStatusChangedAt();
					System.out.println("№" + todoItem.getNo() 
					+ " This task's status has been successfully changed to " 
							+ status);
				} else if (todoItem.getStatus() == Status.DOING 
						&& status == Status.DONE) {
					todoItem.setStatus(status);
					todoItem.setStatusChangedAt();
					System.out.println("№" + todoItem.getNo() 
					+ " task's status has been successfully changed to " 
							+ status);
				}
				isFound = true;
				break;
			}
		}
		if (!isFound) 
			throw new TaskNotFoundException(
	            "Task №" + no + " not found");
	}
	
	public void editTodoItem(int no, String title, 
			String description, LocalDate deadline) {
		boolean isFound = false;
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getNo() == no) {
				if (title != null) {
					todoItem.setTitle(title);
					System.out.println("Task №" + no + "'s title has been changed.");
				}
				if (description != null) {
					todoItem.setDescription(description);
					System.out.println("Task №" + no + "'s description has been changed.");
				}
				if (deadline != null) {
					todoItem.setDeadline(deadline);
					System.out.println("Task №" + no + "'s deadline has been changed.");
				}
				isFound = true;
				break;
			}
		}
		if (!isFound) 
			throw new TaskNotFoundException(
	            "Task №" + no + " not found");
	}
	
	public void deleteTodoItem(int no) {
		boolean isFound = false;
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getNo() == no) {
				todoItems.remove(todoItem);
				System.out.println("This task was removed.");
				isFound = true;
				break;
			}
		}
		if (!isFound) 
			throw new TaskNotFoundException(
	            "Task №" + no + " not found");
	}
	
	public void getAllTodoItemsByStatus(Status status) {
		int count = 1;
		if (status != Status.TODO && status != Status.DOING && status != Status.DONE) {
			throw new InvalidStatusTransitionException(
				status + " status does not exist.");
		}
		System.out.println("All the todos with " + status + " status:");
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getStatus() == status) {
				showTodoItem(count, todoItem);
				count++;
			}
		}
	}
	
	public void searchTodoItems(String searchText) {
		int count = 1;
		for (TodoItem todoItem : todoItems) {
			if(todoItem.getTitle()
			        .toLowerCase()
			        .contains(searchText.toLowerCase())) {
				showTodoItem(count, todoItem);
			}
			count++;
		}
	}
	
	public void filterTodoItems(Status status, LocalDate fromDate, LocalDate toDate) {
		int count = 1;
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getStatus() == status) {
				if (todoItem.getDeadline().isAfter(fromDate) && 
						todoItem.getDeadline().isBefore(toDate)) {
					showTodoItem(count, todoItem);
				}
			}
			count++;
		}
	}
}
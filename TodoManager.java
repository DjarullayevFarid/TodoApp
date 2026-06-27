package TodoApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import TodoApp.TodoItem.Status;
import TodoApp.exceptions.InvalidDeadlineException;
import TodoApp.exceptions.InvalidStatusTransitionException;
import TodoApp.exceptions.TaskNotFoundException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TodoManager implements ITodoManager {
	private ArrayList<TodoItem> todoItems;
	
	public TodoManager(ArrayList<TodoItem> todoItems) {
		this.todoItems = todoItems;
	}
	
	private TodoItem findByNo(int no) {
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getNo() == no) {
				return todoItem;
			} 
		}
			throw new TaskNotFoundException(
				"Task №" + no + " not found");
	}
	
	private void showTodoItem(int count, TodoItem todoItem) {
		System.out.println(count + ") №" + todoItem.getNo() + " | " 
				+ todoItem.getTitle() + " | " + todoItem.getDescription() + " | " 
				+ todoItem.getStatus() + " | " 
				+ todoItem.getFormattedDeadline());
	}
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	Scanner scan = new Scanner(System.in);
	
	public LocalDate deadlineCheck(String deadlineInput) {
		LocalDate deadline = null;
		
		try {
			deadline =
					LocalDate.parse(deadlineInput, formatter);
		} catch (DateTimeParseException ex) {
			System.out.println("Invalid date format! Try again.");
		}
		
		if (!deadline.isAfter(LocalDate.now())) {
		    throw new InvalidDeadlineException(
		            "Deadline must be a future date.");
		}
		
		return deadline;
	}
	
	public TodoItem createTodoItem() {
		System.out.print("Title: ");
		String title = scan.nextLine();
		
		System.out.print("description: ");
		String description = scan.nextLine();
		
		System.out.print("deadline (dd.MM.yyyy): ");
		String deadlineInput = scan.nextLine();
		
		LocalDate deadline = deadlineCheck(deadlineInput);
		
		TodoItem todoItem = new TodoItem(title, description, deadline);
		return todoItem;
	}
	
	public void addTodoItem(TodoItem todoItem) {
		todoItems.add(todoItem);
	}
	
	public void getAllTodoItems() {
		int count = 1;
		for (TodoItem todoItem : todoItems) {
			showTodoItem(count, todoItem);
			count++;
		}
		System.out.println();
	}
	
	public void getAllDelayedTasks() {
		int count = 1;
		System.out.println("All delayed tasks: ");
		for (TodoItem todoItem : todoItems) {
			if (todoItem.getDeadline().isBefore(LocalDate.now()) && todoItem.getStatus() != Status.DONE) {
				showTodoItem(count, todoItem);
				count++;
			}
		}
	}
	
	public Status getStatusByChoice(int choice) {
	    switch (choice) {
	        case 1: return Status.TODO;
	        case 2: return Status.DOING;
	        case 3: return Status.DONE;
	        default:
	            throw new IllegalArgumentException("Invalid status.");
	    }
	}
	
	public void changeTodoItemStatus(int no, Status status) {
		TodoItem todoItem = findByNo(no);
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
					+ " task's status has been successfully changed to " 
							+ status);
				} else if (todoItem.getStatus() == Status.DOING 
						&& status == Status.DONE) {
					todoItem.setStatus(status);
					todoItem.setStatusChangedAt();
					System.out.println("№" + todoItem.getNo() 
					+ " task's status has been successfully changed to " 
							+ status);
				}
			}
	
	public void editTodoItem(int no, String title, 
			String description, LocalDate deadline) {
		TodoItem todoItem = findByNo(no);
				if (title != null) {
					todoItem.setTitle(title);
					System.out.println("Task №" + no + "'s title has been changed to " + todoItem.getTitle() + ".");
				}
				if (description != null) {
					todoItem.setDescription(description);
					System.out.println("Task №" + no + "'s description has been changed to " + todoItem.getDescription() + ".");
				}
				if (deadline != null) {
					todoItem.setDeadline(deadline);
					System.out.println("Task №" + no + "'s deadline has been changed to " + todoItem.getDeadline() + ".");
				}
	}
	
	public void deleteTodoItem(int no) {
		TodoItem todoItem = findByNo(no);
				todoItems.remove(todoItem);
				System.out.println("This task was removed.");
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
			        .trim()
			        .contains(searchText.toLowerCase())) {
				showTodoItem(count, todoItem);
				count++;
			}
		}
	}
	
	public void filterTodoItems(Status status, LocalDate fromDate, LocalDate toDate) {
		int count = 1;
		if (status != null) {
			for (TodoItem todoItem : todoItems) {
				if (todoItem.getStatus() == status) {
					if (!todoItem.getDeadline().isBefore(fromDate) && 
							!todoItem.getDeadline().isAfter(toDate)) {
						showTodoItem(count, todoItem);
					}
				}
				count++;
			}
		} else {
			for (TodoItem todoItem : todoItems) {
					if (!todoItem.getDeadline().isBefore(fromDate) && 
							!todoItem.getDeadline().isAfter(toDate)) {
						showTodoItem(count, todoItem);
				}
				count++;
			}
		}
	}
}
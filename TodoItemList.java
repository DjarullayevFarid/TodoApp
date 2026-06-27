package TodoApp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import TodoApp.TodoItem.Status;
import TodoApp.exceptions.InvalidStatusTransitionException;
import TodoApp.exceptions.TaskNotFoundException;

public class TodoItemList {

	public static void main(String args[]) {
		ArrayList<TodoItem> todoList = new ArrayList<>();
		TodoItem todo1 = new TodoItem("Wash the dishes", "Need to wash the dishes as quickly as possible", LocalDate.of(2026, 7, 1));
		TodoItem todo2 = new TodoItem("finish the homework", "finish the homework until tomorrow", LocalDate.of(2026, 9, 3));
		
		ITodoManager manager = new TodoManager(todoList);
		
		manager.addTodoItem(todo1);
		manager.addTodoItem(todo2);
		
		Scanner scan = new Scanner(System.in);
		
		int choice;
		
		while (true) {
			System.out.println("1. Add task\r"
					+ "2. Show all\r"
					+ "3. Delayed tasks\r"
					+ "4. Search\r"
					+ "5. Filter\r"
					+ "6. Change status\r"
					+ "7. Edit\r"
					+ "8. Delete");
			choice = scan.nextInt();
			switch (choice) {
				case 1:
					manager.addTodoItem(manager.createTodoItem());
					System.out.println();
					break;
				case 2:
					manager.getAllTodoItems();
					break;
				case 3:
					manager.getAllDelayedTasks();
					System.out.println();
					break;
				case 4: {
					scan.nextLine();
					String searchText = scan.nextLine();
					manager.searchTodoItems(searchText);
					System.out.println();
					break;
				}
				case 5: {
					System.out.println("Select status: \r"
							+ "1. TODO \r"
							+ "2. DOING \r"
							+ "3. DONE \r"
							+ "4. any");
					int statusInput = scan.nextInt();
					Status status = null;
					if (statusInput == 1 || statusInput == 2 || statusInput == 3) {
						try {
							status = manager.getStatusByChoice(statusInput);
						} catch (IllegalArgumentException ex) {
							System.out.println(ex.getMessage());
							System.out.println();
							break;
						}
					}
					
					System.out.println("Select a start date: ");
					scan.nextLine();
					String fromDateInput = scan.nextLine();
					System.out.println("Select an end date: ");
					String toDateInput = scan.nextLine();
					DateTimeFormatter formatter =
					        DateTimeFormatter.ofPattern("dd.MM.yyyy");
					
					LocalDate fromDate = null;
					LocalDate toDate = null;
					try {
						fromDate =
								LocalDate.parse(fromDateInput, formatter);
						toDate =
								LocalDate.parse(toDateInput, formatter);
					} catch (DateTimeParseException ex) {
						System.out.println("Invalid date format! Try again.");
						break;
					} 
						
					manager.filterTodoItems(status, fromDate, toDate);
					System.out.println();
					break;
				}
					
				case 6: {
					System.out.println("Enter №: ");
					int no = scan.nextInt();
					System.out.println("Select status: \r"
							+ "1. TODO \r"
							+ "2. DOING \r"
							+ "3. DONE \r"
							+ "4. any");
					scan.nextLine();
					int statusInput = scan.nextInt();
					Status status = null;
					if (statusInput == 1 || statusInput == 2 || statusInput == 3) {
						try {
							status = manager.getStatusByChoice(statusInput);
						} catch (IllegalArgumentException ex) {
							System.out.println(ex.getMessage());
							System.out.println();
							break;
						}
					}
					
					manager.changeTodoItemStatus(no, status);
					break;
				}
					
				case 7: {
					System.out.println("Enter №: ");
					int no = scan.nextInt();
					
					System.out.print("Title: ");
					scan.nextLine();
					String title = scan.nextLine();
					
					System.out.print("description: ");
					String description = scan.nextLine();
					
					System.out.print("deadline (dd.MM.yyyy): ");
					String deadlineInput = scan.nextLine();
					
					LocalDate deadline = manager.deadlineCheck(deadlineInput);
					
					manager.editTodoItem(no, title, description, deadline);
					break;
				}
				case 8:
					System.out.println("Enter №: ");
					int no = scan.nextInt();
					manager.deleteTodoItem(no);
					break;
			}
		}
	}
}
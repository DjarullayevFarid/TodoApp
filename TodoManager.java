package TodoApp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.Scanner;

import TodoApp.TodoItem.Status;

import TodoApp.exceptions.InvalidDeadlineException;
import TodoApp.exceptions.InvalidStatusTransitionException;
import TodoApp.exceptions.TaskNotFoundException;

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
	
	private TodoItem getTodoItemFromDBByNo(int no) {
		String sql = """
				SELECT * FROM todo_items
				WHERE no = ?
				""";
		
		try (
				Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			) {
			
				statement.setInt(1, no);
				
				ResultSet resultSet = statement.executeQuery();

				if (resultSet.next()) {
					
					Status status = 
							Status.valueOf(resultSet.getString("status"));
		            String title = resultSet.getString("title");
		            String description = resultSet.getString("description");
		            LocalDate deadline =
		                    resultSet.getDate("deadline").toLocalDate();
		            LocalDateTime statusChangedAt = 
		            		resultSet.getTimestamp("status_changed_at")
		                    .toLocalDateTime();
		            
		            return new TodoItem(
		                    no,
		                    status,
		                    title,
		                    description,
		                    deadline,
		                    statusChangedAt);
		        }
				
		throw new TaskNotFoundException("Task №" + no + " not found");
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
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
			throw new InvalidDeadlineException("Invalid date format.");
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
		
		String sql = """
		        INSERT INTO todo_items
		        (no, status, title, description, deadline, status_changed_at)
		        VALUES (?, ?, ?, ?, ?, ?)
		        """;

		    try (
		        Connection connection = DatabaseConnection.getConnection();
		        PreparedStatement statement = connection.prepareStatement(sql)
		    ) {

		        statement.setInt(1, todoItem.getNo());
		        statement.setString(2, todoItem.getStatus().name());
		        statement.setString(3, todoItem.getTitle());
		        statement.setString(4, todoItem.getDescription());
		        statement.setDate(5, Date.valueOf(todoItem.getDeadline()));
		        statement.setTimestamp(
		                6,
		                Timestamp.valueOf(todoItem.getStatusChangedAt()));

		        statement.executeUpdate();

		        System.out.println("Saved to database!");

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
	}
	
	public void getAllTodoItems() {
		int count = 1;
		for (TodoItem todoItem : todoItems) {
			showTodoItem(count, todoItem);
			count++;
		}
		System.out.println();
	}
	
	public void getAllTodoItemsFromDB() {
		String sql = "SELECT * FROM todo_items";

	    try (
	        Connection connection = DatabaseConnection.getConnection();
	        Statement statement = connection.createStatement();
	    ) {

	        ResultSet resultSet = statement.executeQuery(sql);

	        while (resultSet.next()) {

	            int no = resultSet.getInt("no");
	            String title = resultSet.getString("title");
	            String description = resultSet.getString("description");
	            String status = resultSet.getString("status");
	            LocalDate deadline =
	                    resultSet.getDate("deadline").toLocalDate();

	            System.out.println(
	                    no + " | "
	                    + title + " | "
	                    + description + " | "
	                    + status + " | "
	                    + deadline
	            );
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
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
	
	public void changeTodoItemStatusInDB(int no, Status status) {
		TodoItem todo = getTodoItemFromDBByNo(no);
		
		String sql = """
				UPDATE todo_items
				SET status = ?, status_changed_at = ?
				WHERE no = ?
				""";
		
		try (
				Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			) {
			
			if ((todo.getStatus() == Status.TODO 
					&& status != Status.DOING) || 
					(todo.getStatus() == Status.DOING 
					&& status != Status.DONE))
						throw new InvalidStatusTransitionException(
							"Unable to change status from " 
									+ todo.getStatus() 
									+ " to " 
									+ status 
									+ ".");
			
			statement.setString(1, status.name());
			statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			statement.setInt(3, no);
			
			int rows = statement.executeUpdate();
			
			if (rows == 0) {
				throw new TaskNotFoundException(
						"Task №" + no + " not found");
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
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
	
	public void editTodoItemInDB(int no, String title, 
			String description, LocalDate deadline) {
		
		TodoItem todo = getTodoItemFromDBByNo(no);
		
		String sql = """
				UPDATE todo_items
				SET title = ?, description = ?, deadline = ?
				WHERE no = ?
				""";
		
		try (
				Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			) {
			
				if (title == null) {
					title = todo.getTitle();
				}
				
				if (description == null) {
					description = todo.getDescription();
				}
				
				if (deadline == null) {
					deadline = todo.getDeadline();
				}
			
				statement.setString(1, title);
				statement.setString(2, description);
				statement.setDate(3, Date.valueOf(deadline));
				statement.setInt(4, no);
				
				int rows = statement.executeUpdate();
				
				if (rows == 0) {
					throw new TaskNotFoundException(
							"Task №" + no + " not found");
				}
				
				System.out.println("The task has been edited.");

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
	}
	
	public void deleteTodoItem(int no) {
		TodoItem todoItem = findByNo(no);
				todoItems.remove(todoItem);
				System.out.println("This task was removed.");
			}
	
	public void deleteTodoItemFromDB(int no) {
		String sql = """
				DELETE FROM todo_items
				WHERE no = ?
				""";
		
		try (
				Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			) {
			
				statement.setInt(1, no);
				
				int rows = statement.executeUpdate();
				
				if (rows == 0) {
					throw new TaskNotFoundException(
							"Task №" + no + " not found");
				}

		        System.out.println("Deleted from database!");
				
			} catch (SQLException e) {
		        e.printStackTrace();
		    }
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
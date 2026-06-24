package TodoApp;

import java.time.LocalDate;
import java.util.ArrayList;

import TodoApp.TodoItem.Status;
import TodoApp.exceptions.InvalidStatusTransitionException;
import TodoApp.exceptions.TaskNotFoundException;

public class TodoItemList {

	public static void main(String args[]) {
		ArrayList<TodoItem> todoList = new ArrayList<>();
		TodoItem todo1 = new TodoItem("wash the dishes", "Need to wash the dishes as quickly as possible", LocalDate.of(2026, 7, 1));
		TodoItem todo2 = new TodoItem("finish the homework", "finish the homework until tomorrow", LocalDate.of(2026, 9, 3));
		
		ITodoManager manager = new TodoManager(todoList);
		
		manager.addTodoItem(todo1);
		manager.addTodoItem(todo2);
		
		manager.getAllTodoItems();
		
		try {
		    manager.deleteTodoItem(3);
		}
		catch(TaskNotFoundException ex) {
		    System.out.println(ex.getMessage());
		}
		
		try {
			manager.editTodoItem(2, "asd", null, null);
		} 
		catch(TaskNotFoundException ex) {
		    System.out.println(ex.getMessage());
		}
		
		try {
			manager.changeTodoItemStatus(1, Status.DOING);
		}
		catch(TaskNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		catch(InvalidStatusTransitionException ex) {
			System.out.println(ex.getMessage());
		}
		manager.getAllDelayedTasks();
		manager.getAllTodoItemsByStatus(Status.DOING);
		System.out.println();
		manager.searchTodoItems("asd");
		System.out.println();
		manager.filterTodoItems(Status.DOING, LocalDate.of(2026, 4, 14), LocalDate.of(2026, 8, 4));
		
	}
}
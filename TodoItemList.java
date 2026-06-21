package TodoApp;

import java.time.LocalDate;
import java.util.ArrayList;

import TodoApp.TodoItem.Status;

public class TodoItemList {

	public static void main(String args[]) {
		ArrayList<TodoItem> todoList = new ArrayList<>();
		TodoItem todo1 = new TodoItem("wash the dishes", "Need to wash the dishes as quickly as possible", LocalDate.of(2026, 06, 01));
		TodoItem todo2 = new TodoItem("finish the homework", "finish the homework until tomorrow", LocalDate.of(2026, 06, 01));
		
		ITodoManager manager =
		        new TodoManager(todoList);
		
		manager.addTodoItem(todo1);
		manager.addTodoItem(todo2);
		
		manager.deleteTodoItem(2);
		manager.changeTodoItemStatus(1, Status.DOING);
		manager.getAllTodoItems();
		manager.getAllDelayedTasks();
		manager.GetAllTodoItemsByStatus(Status.DOING);
		
		
	}
}
package TodoApp;

import java.time.LocalDate;

import TodoApp.TodoItem.Status;

public interface ITodoManager {

	void addTodoItem(TodoItem todoItem);

    void getAllTodoItems();

    void getAllDelayedTasks();

    void changeTodoItemStatus(int no, Status status);

    void editTodoItem(int no, String title,
                      String description,
                      LocalDate deadline);
    
    void deleteTodoItem(int no);
    
    void GetAllTodoItemsByStatus(Status status);
}

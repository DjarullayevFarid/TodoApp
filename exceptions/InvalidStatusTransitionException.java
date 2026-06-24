package TodoApp.exceptions;

public class InvalidStatusTransitionException extends RuntimeException {

	public InvalidStatusTransitionException(String message) {
		super(message);
	}
}

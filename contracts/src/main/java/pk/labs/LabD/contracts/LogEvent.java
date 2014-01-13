package pk.labs.LabD.contracts;

public class LogEvent {
	private Object source;
	private String message;

	public LogEvent(Object source, String message) {
		this.source = source;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Object getSource() {
		return source;
	}

}

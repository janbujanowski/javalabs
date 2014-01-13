package pk.labs.LabD.contracts;

public interface Logger {

	void log(Object src, String msg);

	void addLogListener(LogListener listener);

	void removeLogListener(LogListener listener);
}

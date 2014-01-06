package pk.labs.LabC.logger;

import java.util.ArrayList;
import java.util.List;

public class Logger {

	private static Logger instance;
	private List<LogListener> listeners;

	private Logger() {
		listeners = new ArrayList<>();
	}

	public static Logger get() {
		if (instance == null)
			instance = new Logger();
		return instance;
	}

	public void log(Object src, String msg) {
		LogEvent evt = new LogEvent(src, msg);
		for (LogListener listener : listeners)
			listener.performLogEvent(evt);
	}

	public void addLogListener(LogListener listener) {
		listeners.add(listener);
	}

	public void removeLogListener(LogListener listener) {
		listeners.remove(listener);
	}
}

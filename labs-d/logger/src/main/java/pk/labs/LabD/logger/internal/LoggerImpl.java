package pk.labs.LabD.logger.internal;

import pk.labs.LabD.contracts.LogEvent;
import pk.labs.LabD.contracts.LogListener;
import pk.labs.LabD.contracts.Logger;

import java.util.ArrayList;
import java.util.List;

public class LoggerImpl implements Logger {

	private List<LogListener> listeners;

	public LoggerImpl() {
		listeners = new ArrayList<>();
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

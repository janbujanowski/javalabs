package pk.labs.LabB;

import java.util.Arrays;

import pk.labs.LabB.Contracts.Logger;

public class LoggerImpl implements Logger {
	
	public void logMethodEntrance(String methodName, Object[] methodArgs) {
		System.out.printf("Enter in %s with args %s\n", methodName, methodArgs != null ? Arrays.asList(methodArgs) : "[]");
	}
	
	public void logMethodExit(String methodName, Object methodResult) {
		System.out.printf("Exit from %s with result %s\n", methodName, methodResult);
	}
	
}

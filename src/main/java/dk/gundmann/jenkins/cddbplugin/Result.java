package dk.gundmann.jenkins.cddbplugin;

import java.sql.SQLException;

public class Result {

	private final boolean ok;
	private final String message;
	private final Exception exception;

	public Result(boolean ok, String message, Exception exception) {
		this.ok = ok;
		this.message = message;
		this.exception = exception;
	}

	public static Result ok() {
		return new Result(true, "", null);
	}
	
	public static Object faild() {
		return new Result(false, "", null);
	}

	public static Result faild(String message, Exception exception) {
		return new Result(false, message, exception);
	}
	
	public boolean isOk() {
		return ok;
	}
	
	public boolean failed() {
		return !ok;
	}

	public String message() {
		return message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ok ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (ok != other.ok)
			return false;
		return true;
	}

	public Exception exception() {
		return exception;
	}

}

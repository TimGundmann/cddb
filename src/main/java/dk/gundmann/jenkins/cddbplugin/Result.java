package dk.gundmann.jenkins.cddbplugin;


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
	
	public static Result faild() {
		return new Result(false, "", null);
	}

	public static Result faild(String message, Exception exception) {
		Result result = new Result(false, message, exception);
		System.out.println(message);
		exception.printStackTrace();
		return result;
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

	public Exception exception() {
		return exception;
	}

	@Override
	public String toString() {
		return "Result [ok=" + ok + ", message=" + message + ", exception=" + exception + "]";
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

}

package dk.gundmann.jenkins.cddbplugin.parameters;

public class Parameter {

	private final String key;
	
	private final Object value;

	private Parameter(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public static Builder aBuilder() {
		return new Builder();
	}
	
	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public static class Builder {
		private String key;
		private Object value;
		
		public Builder withKey(String key) {
			this.key = key;
			return this;
		}
		
		public Builder withValue(Object value) {
			this.value = value;
			return this;
		}
		
		public Parameter build() {
			return new Parameter(key, value);
		}
		
	}
}

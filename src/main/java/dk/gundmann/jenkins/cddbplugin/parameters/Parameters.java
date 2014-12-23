package dk.gundmann.jenkins.cddbplugin.parameters;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parameters {

	private Map<String, Parameter> parameters = new HashMap<String, Parameter>();
	
	public Parameters(Parameter... parameters) {
		addAll(parameters);
	}
	
	public Parameter getParameter(String key) {
		return parameters.get(key);
	}

	public String valueAsString(String key) {
		if (parameters.containsKey(key)) {
			return parameters.get(key).getValue().toString();
		}
		throw new MissingParameterException(key);
	}

	public Parameters addAll(Parameter... parameters) {
		for (Parameter parameter : parameters) {
			add(parameter);
		}
		return this;
	}

	public Parameters add(Parameter parameter) {
		this.parameters.put(parameter.getKey(), parameter);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T valueAsType(String key, Class<T> classType) {
		return (T)parameters.get(key).getValue();
	}

	@SuppressWarnings("unchecked")
	public List<File> valueAsFiles(String key) {
		return (List<File>)parameters.get(key).getValue();
	}

}

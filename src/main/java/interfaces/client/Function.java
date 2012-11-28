package interfaces.client;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 28/11/12
 * Time: 14:32
 */
public class Function implements Serializable {

	private final Class<?> returnType;
	private final String name;
	private final Class<?>[] parameterTypes;

	public Function(Class<?> returnType, String methodName, Class<?>... parameterTypes) {
		this.returnType = returnType;
		this.name = methodName;
		this.parameterTypes = parameterTypes;
	}

	public String getName() {
		return name;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
}

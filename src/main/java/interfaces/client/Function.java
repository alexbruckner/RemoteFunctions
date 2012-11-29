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

	@Override
	public String toString() {
		return String.format("[%s %s(%s)]", returnType != null ? returnType.getName() : "void", name, toList(parameterTypes));
	}

	private String toList(Class<?>[] parameterTypes) {
		StringBuilder sb = new StringBuilder();
		for (Class<?> parameterType : parameterTypes) {
			sb.append(parameterType.getName()).append(", ");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
}

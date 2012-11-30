package interfaces.client.generate;

import interfaces.client.Function;
import interfaces.client.RemoteClient;

/**
 * User: Alex
 * Date: 30/11/12
 * Time: 19:48
 */
public class Functions {

	private final String host;
	private final int port;

	public Functions(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getSource() {
		StringBuilder sb = new StringBuilder();
		sb.append("package interfaces.client.generate;");
		sb.append("import interfaces.client.RemoteClient;");
		sb.append("import java.util.Set;");

		sb.append("public class Functions_").append(host).append("_").append(port).append("{");

		sb.append("private static final RemoteClient remoteClient = new RemoteClient(\"").append(host).append("\",").append(port).append(");");

		for (Function function : new RemoteClient(host, port).getAvailableFunctions()) {
			String returnType = function.getReturnType().getSimpleName();
			String functionName = function.getName();
			sb.append("public static ").append(returnType).append(" ").append(functionName)
					.append("(");
			int i = 0;
			for (Class<?> param : function.getParameterTypes()) {
				sb.append(param.getSimpleName()).append(" param").append(i++).append(",");
			}

			if (function.getParameterTypes().length > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("){");

			if (!function.getReturnType().getSimpleName().equals("void")){
				sb.append("return ");
			}
			sb.append("remoteClient.call(").append(returnType).append(".class, \"").append(functionName).append("\"");
			for (i = 0; i < function.getParameterTypes().length; i++) {
				sb.append(", param").append(i);
			}
			sb.append(");");
			sb.append("}");

		}


		return sb.append("}").toString();
	}
}

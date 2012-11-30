package interfaces.client.generate;

import interfaces.client.Function;
import interfaces.client.RemoteClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

	public static void main(String[] args) throws IOException {
		String host = System.getProperty("functions.host");
		int port = Integer.parseInt(System.getProperty("functions.port"));
		new Functions(host, port).generateSource();
	}

	public void generateSource() throws IOException {
		String source = getSource();
		File sourceFile = new File("src/main/java/interfaces/client/generate/Functions_" + host + "_" + port + ".java");
		FileWriter writer = new FileWriter(sourceFile);
		writer.write(source);
		writer.flush();
		writer.close();
	}

	public String getSource() {
		StringBuilder sb = new StringBuilder();
		sb.append("package interfaces.client.generate;\n");
		sb.append("import interfaces.client.RemoteClient;\n");
		sb.append("import java.util.Set;\n");

		sb.append("public class Functions_").append(host).append("_").append(port).append(" {\n");

		sb.append("\tprivate static final RemoteClient remoteClient = new RemoteClient(\"").append(host).append("\",").append(port).append(");\n");

		for (Function function : new RemoteClient(host, port).getAvailableFunctions()) {
			String returnType = function.getReturnType().getSimpleName();
			String functionName = function.getName();
			sb.append("\tpublic static ").append(returnType).append(" ").append(functionName)
					.append("(");
			int i = 0;
			for (Class<?> param : function.getParameterTypes()) {
				sb.append(param.getSimpleName()).append(" param").append(i++).append(",");
			}

			if (function.getParameterTypes().length > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("){\n");

			if (!function.getReturnType().getSimpleName().equals("void")) {
				sb.append("\t\treturn ");
			}
			sb.append("remoteClient.call(").append(returnType).append(".class, \"").append(functionName).append("\"");
			for (i = 0; i < function.getParameterTypes().length; i++) {
				sb.append(", param").append(i);
			}
			sb.append(");\n");
			sb.append("\t}\n");

		}


		return sb.append("}").toString();
	}
}

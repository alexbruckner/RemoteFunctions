package interfaces.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class RemoteClient {

	private String host;
	private int port;

	public RemoteClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void call(String functionName, Object... args) {
		call(null, functionName, args);
	}

	@SuppressWarnings("unchecked")
	public <T> T call(Class<T> returnType, String functionName, Object... args) {
		Class<?>[] parameterTypes = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			parameterTypes[i] = args[i].getClass();
		}
		interfaces.client.Function function = new interfaces.client.Function(returnType, functionName, parameterTypes);
		FunctionCall functionCall = new FunctionCall(function, args);
		return (T) call(functionCall);
	}

	@SuppressWarnings("unchecked")
	public <T> T call(Class<T> returnType, Function function, Object... args) {
		assert(returnType == function.getReturnType());
		FunctionCall functionCall = new FunctionCall(function, args);
		return (T) call(functionCall);
	}

	private Object call(FunctionCall functionCall) {
		Socket socket = null;
		ObjectOutputStream objectOutputStream = null;
		ObjectInputStream objectInputStream = null;
		Object returned = null;
		try {
			socket = new Socket(host, port);
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(functionCall);
			objectInputStream = new ObjectInputStream((socket.getInputStream()));
			returned = objectInputStream.readObject();
		} catch (Exception e) {
			throw new RemoteException(e);
		} finally {
			ClientUtils.close(objectOutputStream);
			ClientUtils.close(objectInputStream);
			ClientUtils.close(socket);
		}

		if (returned instanceof RemoteException) {
			throw (RemoteException) returned;
		}

		return returned;
	}

	@SuppressWarnings("unchecked")
	public Set<Function> getAvailableFunctions() {
		return call(Set.class, "getAvailableFunctions");
	}

}
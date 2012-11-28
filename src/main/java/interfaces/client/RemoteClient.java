package interfaces.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RemoteClient {

	private String host;
	private int port;

	public RemoteClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public <T> T call(Class<T> returnType, String functionName, Object... args) throws IOException, ClassNotFoundException {
		Class<?>[] parameterTypes = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			parameterTypes[i] = args[i].getClass();
		}
		interfaces.client.Function function = new interfaces.client.Function(returnType, functionName, parameterTypes);
		FunctionCall functionCall = new FunctionCall(function, args);
		Object returned = call(functionCall);
		if (returned instanceof RemoteException) {
			throw (RemoteException)returned;
		}
		return (T) returned;
	}

	private Object call(FunctionCall functionCall) throws IOException, ClassNotFoundException {
		Socket socket = new Socket(host, port);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeObject(functionCall);
		ObjectInputStream objectInputStream = new ObjectInputStream((socket.getInputStream()));
		Object returned = objectInputStream.readObject();
		objectOutputStream.close();
		objectInputStream.close();
		socket.close();
		return returned;
	}

}
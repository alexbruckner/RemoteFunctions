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

	public <T> T call(FunctionCall functionCall) throws IOException, ClassNotFoundException {
		Socket socket = new Socket(host, port);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeObject(functionCall);
		ObjectInputStream objectInputStream = new ObjectInputStream((socket.getInputStream()));
		Object returned = objectInputStream.readObject();
		objectOutputStream.close();
		objectInputStream.close();
		socket.close();
		return (T) returned;       //TODO
	}

}
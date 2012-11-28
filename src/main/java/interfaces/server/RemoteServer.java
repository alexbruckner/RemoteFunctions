package interfaces.server;

import interfaces.client.FunctionCall;
import interfaces.client.RemoteException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

/**
 * User: Alex
 * Date: 25/11/12
 * Time: 20:34
 */
public class RemoteServer {

	public static final Logger LOG = Logger.getLogger(RemoteServer.class.getName());

	private static final Set<Method> FUNCTIONS = new CopyOnWriteArraySet<Method>();
//	private static final ExecutorService EXECUTION_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public RemoteServer(final int port) {
		// check annotated functions
		LOG.info("Loading declared functions...");
		for (Method m : Classes.listAllAnnotatedMethods(Server.class, Function.class)) {
			FUNCTIONS.add(m);
		}

		LOG.info(String.valueOf(FUNCTIONS));

		// open server socket
		new Thread(new Runnable() {
			public void run() {
				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(port);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				while (!Thread.currentThread().isInterrupted()) {
					try {
						Socket socket = serverSocket.accept();
						submit(socket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		LOG.info(String.format("started server on port: %s", port));

	}

	//TODO multithreading
	private void submit(Socket socket) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
		ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
		FunctionCall functionCall = (FunctionCall) objectInputStream.readObject();
		Method m = lookup(functionCall);
		Object returned;
		if (m != null) {
			returned = m.invoke(null, functionCall.getArgs());
		} else {
			returned = new RemoteException(String.format("%s not found", functionCall.getFunction()));
		}
		ObjectOutputStream objectOutputStream = new ObjectOutputStream((socket.getOutputStream()));
		objectOutputStream.writeObject(returned);
		objectInputStream.close();
		objectOutputStream.close();
		socket.close();
	}

	private Method lookup(FunctionCall functionCall) {
		interfaces.client.Function function = functionCall.getFunction();
		for (Method m : FUNCTIONS) {    //TODO check no parameters
			if (m.getName().equals(function.getName()) && m.getReturnType().equals(function.getReturnType()) && Arrays.equals(m.getParameterTypes(), function.getParameterTypes())) {
				return m;
			}
		}
		return null;
	}


	public static void main(String[] args) {
		new RemoteServer(Integer.parseInt(System.getProperty("server.port")));
	}

}

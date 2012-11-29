package interfaces.server;

import interfaces.client.FunctionCall;
import interfaces.client.RemoteException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * User: Alex
 * Date: 25/11/12
 * Time: 20:34
 */
@Server
public class RemoteServer {

	public static final Logger LOG = Logger.getLogger(RemoteServer.class.getName());

	private static final Set<Method> FUNCTIONS = new CopyOnWriteArraySet<Method>();
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	@Function
	public static Set<interfaces.client.Function> getAvailableFunctions(){
		Set<interfaces.client.Function> functions = new HashSet<interfaces.client.Function>();
		for (Method m : FUNCTIONS){
			functions.add(interfaces.client.Function.toFunction(m));
		}
		return functions;
	}

	public RemoteServer(final int port) {
		// check annotated functions
		LOG.info("Loading declared functions...");
		for (Method m : Classes.listAllAnnotatedMethods(Server.class, Function.class)) {
			if (Modifier.isStatic(m.getModifiers())){
				FUNCTIONS.add(m);
				for (Class<?> parameterType : m.getParameterTypes()) {
					if (parameterType.isPrimitive()) {
						LOG.warning(String.format("%s has a primitive parameter type, whilst not a problem, the client may autobox that parameter into the wrong non-primitive class and therefor trying to call a method with the wrong signature.", m));
					}
				}
			} else {
				LOG.warning(String.format("Not adding method [%s]. Currently we only support static methods.", m));
			}
		}

		LOG.info(String.format("Found the following defined functions (class annotated with '@Server', static method annotated with '@Function'): %s%n", FUNCTIONS));

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
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		LOG.info(String.format("started server on port: %s", port));

	}

	private void submit(final Socket socket) {
		EXECUTOR_SERVICE.submit(new Runnable() {
			public void run() {
				try {
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Method lookup(FunctionCall functionCall) {
		interfaces.client.Function function = functionCall.getFunction();
		for (Method m : FUNCTIONS) {
			if (m.getName().equals(function.getName()) && checkReturnTypes(m, function) && Arrays.equals(m.getParameterTypes(), function.getParameterTypes())) {
				return m;
			}
		}
		return null;
	}

	private boolean checkReturnTypes(Method m, interfaces.client.Function function) {
		return function.getReturnType() == null && m.getReturnType().toString().equals("void") || m.getReturnType().equals(function.getReturnType());
	}

	public static void main(String[] args) {
		new RemoteServer(Integer.parseInt(System.getProperty("server.port")));
	}

}


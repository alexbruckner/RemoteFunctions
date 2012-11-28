package interfaces;

import interfaces.client.FunctionCall;
import interfaces.client.RemoteClient;
import interfaces.server.Function;
import interfaces.server.RemoteServer;
import interfaces.server.Server;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 28/11/12
 * Time: 12:29
 */
@Server
public class ServerTest {

	@Function
	public static String testFunction(String str, int i){
		return String.format("%s called with params [%s, %s]", "testFunction", str, i);
	}

	@Test
	public void test() throws IOException, ClassNotFoundException {
		new RemoteServer(7777);

		RemoteClient remote = new RemoteClient("localhost", 7777);

		interfaces.client.Function function = new interfaces.client.Function(String.class, "testFunction", String.class, int.class);
		FunctionCall functionCall = new FunctionCall(function, "test", 7);

		String returnValue = remote.call(functionCall);

		System.out.println(returnValue);

	}
}

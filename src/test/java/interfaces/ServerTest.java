package interfaces;

import interfaces.client.RemoteClient;
import interfaces.client.RemoteException;
import interfaces.server.Function;
import interfaces.server.RemoteServer;
import interfaces.server.Server;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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

	private static RemoteClient REMOTE;

	@Function
	public static String testFunction(String str, Integer i){
		return String.format("%s called with params [%s, %s]", "testFunction", str, i);
	}

	@Function
	public static String testFunction2(){
		return String.format("%s called with no params", "testFunction2");
	}

	@Function
	public static int testFunction3(){
		return 7;
	}

	@BeforeClass
	public static void init(){
		new RemoteServer(7777);
		REMOTE = new RemoteClient("localhost", 7777);
	}

	@Test
	public void test() throws IOException, ClassNotFoundException {
		String returnValue = REMOTE.call(String.class, "testFunction", "test", 7);
		Assert.assertEquals(returnValue, "testFunction called with params [test, 7]");
	}

	@Test(expectedExceptions = RemoteException.class)
	public void testException() throws IOException, ClassNotFoundException {
		REMOTE.call(String.class, "testFunction");
	}

	@Test
	public void testNoParameterFunction() throws IOException, ClassNotFoundException {
		String returnValue = REMOTE.call(String.class, "testFunction2");
		Assert.assertEquals(returnValue, "testFunction2 called with no params");
	}

	@Test
	public void testIntReturnType() throws IOException, ClassNotFoundException {
		int returnValue = REMOTE.call(int.class, "testFunction3");
		Assert.assertEquals(returnValue, 7);
	}

}

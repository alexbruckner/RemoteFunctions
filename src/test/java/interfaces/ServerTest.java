package interfaces;

import interfaces.client.RemoteClient;
import interfaces.client.RemoteException;
import interfaces.server.Function;
import interfaces.server.RemoteServer;
import interfaces.server.Server;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	public static String testFunction(String str, Integer i) {
		return String.format("%s called with params [%s, %s]", "testFunction", str, i);
	}

	@Function
	public static String testFunction2() {
		return String.format("%s called with no params", "testFunction2");
	}

	@Function
	public static int testFunction3() {
		return 7;
	}

	@Function
	public static void testFunction4(Integer i) {
		System.out.println("testFunction4 method called with: " + i);
	}

	@BeforeClass
	public static void init() {
		new RemoteServer(7777);
		REMOTE = new RemoteClient("localhost", 7777);
	}

	@Test
	public void test() {
		String returnValue = REMOTE.call(String.class, "testFunction", "test", 7);
		Assert.assertEquals(returnValue, "testFunction called with params [test, 7]");
	}

	@Test(expectedExceptions = RemoteException.class)
	public void testException() {
		REMOTE.call(String.class, "testFunction");
	}

	@Test
	public void testNoParameterFunction() {
		String returnValue = REMOTE.call(String.class, "testFunction2");
		Assert.assertEquals(returnValue, "testFunction2 called with no params");
	}

	@Test
	public void testIntReturnType() {
		int returnValue = REMOTE.call(int.class, "testFunction3");
		Assert.assertEquals(returnValue, 7);
	}

	@Test
	public void testNoReturnType() {
		REMOTE.call("testFunction4", 7);
	}

	@Test
	public void getAvailableFunctions() throws InterruptedException {
		List<interfaces.client.Function> availableFunctions = new ArrayList<interfaces.client.Function>(REMOTE.getAvailableFunctions());
		Collections.sort(availableFunctions, new Comparator<interfaces.client.Function>() {
			public int compare(interfaces.client.Function f1, interfaces.client.Function f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		Assert.assertEquals(String.valueOf(availableFunctions),
				"" +
						"[[java.util.Set getAvailableFunctions()], " +
						"[java.lang.String testFunction(java.lang.String, java.lang.Integer)], " +
						"[java.lang.String testFunction2()], " +
						"[int testFunction3()], " +
						"[void testFunction4(java.lang.Integer)]]");
	}

}

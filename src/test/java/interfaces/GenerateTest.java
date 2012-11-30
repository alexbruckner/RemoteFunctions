package interfaces;

import interfaces.client.generate.Functions;
import interfaces.server.RemoteServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 30/11/12
 * Time: 20:00
 */
public class GenerateTest {

	private static Functions FUNCTIONS;

	@BeforeClass
	public static void init() {
		new RemoteServer(8888);
		FUNCTIONS = new Functions("localhost", 8888);
	}

	@Test
	public void test(){
		System.out.println(FUNCTIONS.getSource());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

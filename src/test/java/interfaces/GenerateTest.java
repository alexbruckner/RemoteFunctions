package interfaces;

import interfaces.client.generate.Functions;
import interfaces.client.generate.Functions_localhost_8888;
import interfaces.server.Function;
import interfaces.server.RemoteServer;
import interfaces.server.Server;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * User: Alex
 * Date: 30/11/12
 * Time: 20:00
 */
@Server
public class GenerateTest {

	@Function
	public static String someFunction(String input){
		return new StringBuilder().append(input).reverse().toString();
	}

	@BeforeClass
	public static void init() throws IOException {
		new RemoteServer(8888);
		Functions FUNCTIONS = new Functions("localhost", 8888);
		FUNCTIONS.generateSource();
	}

	@Test
	public void test(){
		System.out.println(Functions_localhost_8888.getAvailableFunctions());
		String reversed = Functions_localhost_8888.someFunction("ahoi, sailor!");
		System.out.println(reversed);
		Assert.assertEquals(reversed, "!rolias ,ioha");
	}
}

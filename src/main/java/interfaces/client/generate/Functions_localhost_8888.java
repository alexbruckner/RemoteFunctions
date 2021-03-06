/*
 * Dynamically created source via interfaces.client.generate.Functions for remote client to server at localhost:8888
 * Date: Sun Dec 02 13:41:43 GMT 2012
 */
package interfaces.client.generate;
import interfaces.client.RemoteClient;
import java.util.Set;
public class Functions_localhost_8888 {
	private static final RemoteClient remoteClient = new RemoteClient("localhost",8888);
	public static void testFunction4(Integer param0){
		remoteClient.call(void.class, "testFunction4", param0);
	}
	public static String testFunction(String param0,Integer param1){
		return remoteClient.call(String.class, "testFunction", param0, param1);
	}
	public static int testFunction3(){
		return remoteClient.call(int.class, "testFunction3");
	}
	public static String testFunction2(){
		return remoteClient.call(String.class, "testFunction2");
	}
	public static Set getAvailableFunctions(){
		return remoteClient.call(Set.class, "getAvailableFunctions");
	}
	public static String someFunction(String param0){
		return remoteClient.call(String.class, "someFunction", param0);
	}
	public static int testFunction5(int param0){
		return remoteClient.call(int.class, "testFunction5", param0);
	}
}
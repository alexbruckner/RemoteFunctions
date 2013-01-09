package interfaces;

import interfaces.client.RemoteClient;
import interfaces.client.generate.Functions;
import interfaces.server.RemoteServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * User: root
 * Date: 30/11/12
 * Time: 17:27
 */
public class GenerateFunctionsTest {

    private static RemoteClient REMOTE;

    @BeforeClass
    public static void init() {
        new RemoteServer(8888);
        REMOTE = new RemoteClient("localhost", 8888);
    }

    @Test
    public void test(){
        Functions functions = new Functions("localhost", 8888);
        System.out.println(functions.getSource());
    }


}

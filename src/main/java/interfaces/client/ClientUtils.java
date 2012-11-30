package interfaces.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 30/11/12
 * Time: 14:01
 */
public final class ClientUtils {
	private ClientUtils(){}
	public static void close(Closeable closeable){
		if (closeable != null){
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Socket socket) {
		if (socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

RemoteFunctions
===============

leightweight RPC calls over sockets

Usage
-----

Any static method that is annotated with @Function
of a class that is annotated with @Server will be able to be called remotely.

1. Example Server Classes (The TestServer class' main method starts the serversocket on 8888):

<pre>
<code>
@Server
public class TestServer {

	@Function
	public static String test(){
		return "test";
	}


	public static void main(String[] args) {
		new RemoteServer(8888);
	}

}

@Server
public class AnotherClass {
	@Function
	public static double pow(Double a, Double b){
		return Math.pow(a, b);
	}
}
</code>
</pre>

2. Example remote call:

<pre>
<code>
public class TestClient {
	public static void main(String[] args) {
		RemoteClient client = new RemoteClient("localhost", 8888);
		System.out.println(client.getAvailableFunctions());
		System.out.println(client.call(String.class, "test"));
		System.out.println(client.call(double.class, "pow", 2.0, 8.0));
	}
}
</code>
</pre>

3. Example output:

<pre>
<code>
[[java.lang.String test()], [double pow(java.lang.Double, java.lang.Double)], [java.util.Set getAvailableFunctions()]]
test
256.0
</code>
</pre>


package interfaces.client;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 28/11/12
 * Time: 15:06
 */
public class FunctionCall implements Serializable {
	private final Function function;
	private final Object[] args;

	public FunctionCall(Function function, Object... args) {
		this.function = function;
		this.args = args;
	}

	public Function getFunction() {
		return function;
	}

	public Object[] getArgs() {
		return args;
	}
}

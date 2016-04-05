package com.beng.op;

import com.beng.op.args.OpArg;
import com.beng.op.call.OpCall;

public class IncompleteArgumentException extends OpRunException {

	public IncompleteArgumentException(OpCall op, OpArg argument) {
		super("The compiler attempted to run the op `" + op.toString() + "` containing an incomplete argument `"
				+ argument + " at " + op.getLocation() + "`!");
	}

}

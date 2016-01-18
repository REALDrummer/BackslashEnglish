package com.beng.op.parseexceptions;

import com.beng.op.Op;

public abstract class OpParseException extends Exception {
	final Op op_to_parse;

	public OpParseException(Op op_to_parse) {
		this.op_to_parse = op_to_parse;
	}

	public Op getOpToParse() {
		return op_to_parse;
	}
}

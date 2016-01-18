package com.beng.op.parseexceptions;

import com.beng.op.Op;

public class TypeMismatchException extends OpParseException {
	final Op expected_type;

	public TypeMismatchException(Op op_to_parse, Op expected_type) {
		super(op_to_parse);

		this.expected_type = expected_type;
	}

	@Override
	public String getMessage() {
		return "I could not match the return type of " + op_to_parse.toString() + " ("
				+ op_to_parse.getReturnType().toString() + ") with the expected type "
				+ expected_type.toString();
	}

}

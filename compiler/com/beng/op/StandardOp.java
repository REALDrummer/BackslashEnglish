package com.beng.op;

import com.beng.opsyntax.OpSyntax;

public class StandardOp extends Op {
	public static final StandardOp TYPE = new StandardOp(SysOp.THING, "type"), BIT = new StandardOp(TYPE, "bit"),
			NUMBER = new StandardOp(TYPE, "number"), POINTER = new StandardOp(TYPE, "<type Type>\\*"),
			NASM_COMMAND = new StandardOp(TYPE, "NASM command"),
			TYPE_MATCHER = new StandardOp(BIT, "<type Child Type> =~ <type Parent Type>");

	public StandardOp(Op return_type, String syntax) {
		super(return_type, OpSyntax.parseDeclaration(syntax), null, null);
	}

	public static boolean typeMatches(Op child_type, Op parent_type) {
		return !TYPE_MATCHER.run(new Op[] { child_type, parent_type }).equals(0);
	}
}

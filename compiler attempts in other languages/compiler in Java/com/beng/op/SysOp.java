package com.beng.op;

import com.beng.op.args.OpArg;
import com.beng.opsyntax.OpArguments;
import com.beng.opsyntax.OpSyntax;

public class SysOp extends Op {
	public static final SysOp OP = new SysOp(StandardOp.TYPE,
			"op(\\(<type Arg Types>{\", \"}\\))?(=><type Return Type>)?", arguments -> null),
			THING = new SysOp(StandardOp.TYPE, "thing", arguments -> 0),
			DEFINE = new SysOp(OP,
					"define\"Modifiers\"(.+)\\<<type Return Type>> \"Declaration\"([^:]|\\:): <code block Body>",
					arguments -> define(arguments));

	private static Op define(OpArguments arguments) {
		// parse the modifiers
		
		
		return new Op(arguments.getArguments().get("Return Type"),
				arguments.getCapturedLiterals().get("Declaration"),
				arguments.getArguments().get("Body"), null);
	}

	private final SysOpRunner runner;

	public static interface SysOpRunner {
		public Object run(OpArguments arguments);
	}

	public SysOp(Op return_type, String syntax, SysOpRunner runner) {
		super(return_type, OpSyntax.parseDeclaration(syntax), null, null);

		this.runner = runner;
	}
}

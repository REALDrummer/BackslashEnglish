package com.beng.op;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.beng.BEngCompiler;
import com.beng.emulator.NASMEmulator;
import com.beng.op.args.OpArg;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCall;
import com.beng.op.call.OpCallException;
import com.beng.opsyntax.OpSyntax;

public class StandardOp extends OpDefinition {
	public static final List<StandardOp> STD_OPS = new ArrayList<>();

	public static final FullyAppliedOpCall TYPE = new StandardOp(null, "type") {
		@Override
		public FullyAppliedOpCall getReturnType() {
			return TYPE;
		}
	}.callNullary();
	public static final FullyAppliedOpCall THING = new StandardOp(TYPE, "thing").callNullary();
	public static final StandardOp OP = new StandardOp(TYPE,
			"op(\\(((<type First Arg Types>, )*<type Last Arg Type>)?\\))?(=><type Return Type>)?");
	public static final FullyAppliedOpCall VARIABLE = new StandardOp(TYPE, "variable").callNullary();
	public static final FullyAppliedOpCall INTEGER = new StandardOp(StandardOp.TYPE, "integer").callNullary();

	public static final FullyAppliedOpCall NASM_COMMAND = new StandardOp(TYPE, "NASM command").callNullary();
	public static final FullyAppliedOpCall EFFECTIVE_ADDRESS = new StandardOp(StandardOp.TYPE,
			"NASM effective address").callNullary();
	public static final FullyAppliedOpCall GENERAL_REGISTER = new StandardOp(StandardOp.TYPE,
			"NASM general register").callNullary();

	public static final FullyAppliedOpCall NUMBER = new StandardOp(TYPE, "number").callNullary();
	public static final StandardOp POINTER = new StandardOp(TYPE, "<type Type>\\*");
	public static final StandardOp CODE_BLOCK = new StandardOp(TYPE, "code block(=><type Return Type>)?");
	public static final StandardOp LIST = new StandardOp(TYPE,
			"list of <type Inner Type>s"/* `list of <type of collection Inner Type>` can be redefined later in the code; here, we define it this naive way to avoid having to deal with accessing the inner type from a collection type */);
	public static final StandardOp ARRAY = new StandardOp(TYPE, "array of <type Inner Type>");

	private static final FullyAppliedOpCall THING_ARRAY;

	static {
		try {
			THING_ARRAY = ARRAY.callWith(Arrays.asList(THING), null).checkFull(null);
		} catch (OpRunException | OpCallException exception) {
			throw new RuntimeException(
					"There was a problem trying to establish an array of things type for defining the array standard ops!");
		}
	}

	public static final StandardOp NEW_2_ITEM_ARRAY = new StandardOp(THING_ARRAY,
			"<thing First Item> and <thing Second Item>");
	public static final StandardOp NEW_3_PLUS_ITEM_ARRAY = new StandardOp(THING_ARRAY,
			"(<thing First Items>, )and <thing Last Item>");

	public static final StandardOp ADD = new StandardOp(StandardOp.NUMBER, "<number Addend> \\+ <number Augend>");

	protected StandardOp(FullyAppliedOpCall return_type, String syntax) {
		super(return_type, OpSyntax.parseDeclaration(syntax));

		STD_OPS.add(this);
	}

	@Override
	public Op runWith(FullyAppliedOpCall call) throws OpRunException {
		return BEngCompiler.emulator.emulateOrOutput(call.assemble(), BEngCompiler.output);
	}
}

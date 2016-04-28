package com.beng.op;

import java.util.ArrayList;
import java.util.List;

import com.beng.BEngCompiler;
import com.beng.emulator.MemoryBlock;
import com.beng.emulator.StackMemoryBlock;
import com.beng.emulator.StaticMemoryBlock;
import com.beng.emulator.args.NASMEffectiveAddress;
import com.beng.emulator.args.NASMGeneralRegister;
import com.beng.emulator.args.NASMInteger;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.PartiallyAppliedOpCall;
import com.beng.opsyntax.OpSyntax;
import com.beng.parser.Parser;

public abstract class SysOp<R extends Assemblable> extends OpDefinition {
	public static final List<SysOp<?>> SYS_OPS = new ArrayList<>();

	public static final SysOp<CodedOpDefinition> DEFINE = new SysOp<CodedOpDefinition>(StandardOp.OP.callNullary(),
			"define\"Modifiers\"(.+)\\<<type Return Type>> \"Declaration\"([^:]|\\:): <code block Body> <op Scope>*") {
		@Override
		public CodedOpDefinition runWith(FullyAppliedOpCall call) throws OpRunException {
			// parse the modifiers within the current context
			// TODO: add the op's argument types to the type of op in he missing argument below
			PartiallyAppliedOpCall modifier = (PartiallyAppliedOpCall) Parser.parseOpInContext(
					call.getCapturedLiteral("Modifiers") + " <op Defined Op>",
					StandardOp.OP.callNullary());

			return new CodedOpDefinition(call.getLocation(),
					call.getArgument("Return Type").checkFull(call),
					OpSyntax.parseDeclaration(call.getCapturedLiteral("Declaration")),
					call.getArgument("Body").checkFull(call).markDefinitionBody(), modifier);
		}
	};

	public static final SysOp<MemoryBlock> BYTES = new SysOp<MemoryBlock>(StandardOp.VARIABLE,
			"\"Size In Bytes\"[1-8] bytes \"Var Name\"(\\w+)") {
		@Override
		public MemoryBlock runWith(FullyAppliedOpCall call) throws OpRunException {
			int size_in_bytes = Integer.parseInt(call.getCapturedLiteral("Size In Bytes")) / 8;

			if (call.isInOperatorBody())
				return new StackMemoryBlock(BEngCompiler.emulator, size_in_bytes, null);
			else
				return new StaticMemoryBlock(BEngCompiler.emulator, size_in_bytes);
		}
	};

	public static final SysOp<NASMEffectiveAddress> EFFECTIVE_ADDRESS = new SysOp<NASMEffectiveAddress>(
			StandardOp.EFFECTIVE_ADDRESS, "(\"Size Specifier\"(byte|d?word) )?\\[<thing* Address>\\]") {
		@Override
		public NASMEffectiveAddress runWith(FullyAppliedOpCall call) throws OpRunException {
			return new NASMEffectiveAddress(call.getCapturedLiteral("Size Specifier"),
					call.getArgument("Address").checkFull(call));
		}
	};
	public static final SysOp<NASMGeneralRegister> GENERAL_REGISTER = new SysOp<NASMGeneralRegister>(
			StandardOp.GENERAL_REGISTER,
			"\"Register Name\"([re]?ax|a[lh]|[c-gs]s|[re]?[ds]i|[re]?[sbi]p|r[89][dwb]?|r1[0-5][dwb]?)") {

		@Override
		public NASMGeneralRegister runWith(FullyAppliedOpCall call) throws OpRunException {
			return NASMGeneralRegister.valueOf(call.getCapturedLiteral("Register Name").toUpperCase());
		}
	};
	public static final SysOp<NASMInteger> INTEGER = new SysOp<NASMInteger>(StandardOp.INTEGER,
			"0x\"Number String\"([0-7]?[0-9A-Fa-f]{1-3})") {
		@Override
		public NASMInteger runWith(FullyAppliedOpCall call) throws OpRunException {
			return new NASMInteger(Integer.parseInt(call.getCapturedLiteral("Number String"), 16));
		}
	};

	protected SysOp(FullyAppliedOpCall return_type, String syntax) {
		super(return_type, OpSyntax.parseDeclaration(syntax));

		SYS_OPS.add(this);
	}

	@Override
	public abstract R runWith(FullyAppliedOpCall call) throws OpRunException;
}

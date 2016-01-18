package com.beng.emulator;

import com.beng.emulator.args.NASMArgRestriction;
import com.beng.op.Assemblage;
import com.beng.op.Op;
import com.beng.op.StandardOp;
import com.beng.op.args.OpArg;
import com.beng.opsyntax.OpSyntax;

public class NASMCommand extends Op {
	private final String command;
	private final NASMArgRestriction[] arg_restrictions;

	private static OpSyntax formulateSyntax(String command, byte number_of_arguments) {
		StringBuilder arguments = new StringBuilder();
		if (number_of_arguments > 1) {
			arguments.append("<thing Arg1>");
			for (int i = 1; i < number_of_arguments; i++) {
				arguments.append(", <thing Arg" + i + ">");
			}
		}

		final String NASM_LABEL_REGEX = "\\.?\\w+\\:";
		return OpSyntax.parseDeclaration(NASM_LABEL_REGEX + "asm " + command + " " + arguments);
	}

	public NASMCommand(String command, NASMArgRestriction... arg_restrictions) {
		super(new Op(StandardOp.POINTER, StandardOp.NASM_COMMAND),
				formulateSyntax(command, (byte) arg_restrictions.length), null, null);
		this.command = command;
		this.arg_restrictions = arg_restrictions;
	}

	public NASMCommand(NASMCommand declaration, OpArg... arguments) {
		super(declaration, arguments);

		this.command = declaration.command;
		this.arg_restrictions = declaration.arg_restrictions;
	}

	@Override
	public Assemblage assemble() {
		return new Assemblage(new NASMCommand[] { this }, null);
	}

	@Override
	public String toString() {
		return "asm " + assemble();
	}
}

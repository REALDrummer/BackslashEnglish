package com.beng;

import com.beng.opsyntax.OpSyntax;
import com.beng.parser.Parser;

public class BEngCompiler {
	public static boolean strictest = false;
	public static boolean debug = true;
	public static boolean verbose = true;
	public static boolean vv = true;

	public enum CompilerExecutionError implements ErrorCode {
		NO_INPUT_FILE;

		@Override
		public int getErrorCode() {
			return ordinal() + 1;
		}
	}

	public static void err(ErrorCode exit_status, String message) {
		final String ERROR_MESSAGE_FORMAT = "\u001B[1;31mERROR: %s\u001B[0m\n";

		System.out.println(String.format(ERROR_MESSAGE_FORMAT, message));
		Thread.dumpStack();
		System.exit(exit_status.getErrorCode());
	}

	public static void main(String[] args) {
		// get the input file
		Parser parser;
		if (args.length == 0) {
			err(CompilerExecutionError.NO_INPUT_FILE,
					"There was no input file given! What should I compile?!");
			return;
		} else {
			parser = new Parser(args[0]);
		}

		// TODO TEMP TESTING
		OpSyntax declaration_parsed = OpSyntax.parseDeclaration(parser);

		System.out.println("successfully parsed declaration:");
		System.out.println(declaration_parsed.toString());
	}
}

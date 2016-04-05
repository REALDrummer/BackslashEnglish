package com.beng;

import com.beng.emulator.NASMEmulator;
import com.beng.opsyntax.OpSyntax;
import com.beng.output.OutputHandler;
import com.beng.parser.Parser;

public class BEngCompiler {
	public static final ErrorCode INTERNAL_ERROR = new ErrorCode() {
		@Override
		public int getErrorCode() {
			return -1;
		}
	};

	/** This {@link Parser} is the only one to be used; in the future, we should be able to use multi-threading and
	 * concurrency and whatnot to have a bunch of parsers at once, but for now, because of concurrency issues and
	 * context switching issues, we'll have to stick with a single parser. */
	public static Parser parser;
	public static NASMEmulator emulator = new NASMEmulator();
	public static OutputHandler output;

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
		if (args.length == 0) {
			err(CompilerExecutionError.NO_INPUT_FILE,
					"There was no input file given! What should I compile?!");
			return;
		} else {
			parser = new Parser(args[0]);
			output = OutputHandler.getOutputHandler(args[0].substring(0, args[0].lastIndexOf('.')) + ".asm");
		}

		// TODO TEMP TESTING
		OpSyntax declaration_parsed = OpSyntax.parseDeclaration(parser);

		System.out.println("successfully parsed declaration:");
		System.out.println(declaration_parsed.toString());
	}
}

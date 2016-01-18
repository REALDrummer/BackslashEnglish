package com.beng.opsyntax;

import com.beng.op.Op;
import com.beng.op.args.OpArg;
import com.beng.parser.Parser;

public abstract class OpSyntax {
	public static OpSyntax parseDeclaration(String string) {
		return parseDeclaration(new Parser(string));
	}

	public static OpSyntax parseDeclaration(Parser input) {
		OpOrSyntax syntax = new OpOrSyntax(input);

		// make sure that we ended on a ':' as we should
		switch (input.peekByte()) {
		case ':': {
			// all is good
			if (syntax.getGroups().length == 1)
				return syntax.getGroups()[0];
			else
				return syntax;
		}
		case ')': {
			// there is an extra unmatched closing parenthesis that stopped the or group parsing
			input.err(OperatorDefinitionErrorType.UNMATCHED_PARENTHESIS,
					"there was an unmatched closing parenthesis found in an operator definition.\n"
							+ "If you meant to use a literal parenthesis, escape it with a backslash (\"\\)\").");
			return null;
		}
		default:
			// ...what?
			input.err(OperatorDefinitionErrorType.UNEXPECTED_END,
					"the operator definition parsing seems to have ended unexpectedly on a '"
							+ input.peekByte() + "'!");
			return null;
		}
	}

	public abstract OpArguments parse(Parser input, Op op_to_parse);

	public abstract boolean canMatchNothing();

	@Override
	public abstract String toString();

	/** This method prints this {@link OpSyntax}, but replaces the argument fields with the <b>
	 * <tt>arguments</b></tt> given.
	 * 
	 * @param arguments is the list of arguments to print instead of the argument fields of the {@link OpSyntax}.
	 * 
	 * @return the <tt>String</tt> representation of this {@link OpSyntax} (as would be obtained using
	 *         {@link #toString()}, but with the argument fields replaces with the given {@link OpArg}s. */
	public String toString(OpArguments arguments) {
		// TODO
		return toString();
	}
}

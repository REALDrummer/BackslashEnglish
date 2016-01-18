package com.beng.op;

import com.beng.SourceFileLocation;
import com.beng.op.args.OpArg;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.op.parseexceptions.TypeMismatchException;
import com.beng.opsyntax.OpArguments;
import com.beng.opsyntax.OpSyntax;
import com.beng.parser.OpsEntry;
import com.beng.parser.Parser;

public class Op implements OpArg, OpsEntry {
	private final Op return_type;
	private final OpSyntax syntax;

	private final SourceFileLocation declaration_location;
	private final Op body;

	private final OpArguments arguments;

	public Op(Op return_type, OpSyntax syntax, Op body, SourceFileLocation declaration_location) {
		this.return_type = return_type;
		this.syntax = syntax;
		this.body = body;
		this.declaration_location = declaration_location;

		arguments = null;
	}

	public Op(Op declaration, OpArguments arguments) {
		return_type = declaration.return_type;
		syntax = declaration.syntax;
		declaration_location = declaration.declaration_location;
		body = declaration.body;
		this.arguments = arguments;
	}

	@Override
	public Op parse(Parser input, Op type) throws OpParseException {
		if (!StandardOp.typeMatches(getReturnType(), type))
			throw new TypeMismatchException(this, type);

		// this may throw an OpParseException
		OpArguments parsed_arguments = syntax.parse(input);

		return new Op(this, parsed_arguments);
	}

	public Number run(Op[] arguments, Op... contextual_ops) {
		// TODO stub
		return null;
	}

	@Override
	public Op getType() {
		return getReturnType();
	}

	public Op getReturnType() {
		return return_type;
	}

	public OpSyntax getSyntax() {
		return syntax;
	}

	public SourceFileLocation getDeclaration_location() {
		return declaration_location;
	}

	public Op getBody() {
		return body;
	}

	public OpArguments getArguments() {
		return arguments;
	}

	public Assemblage assemble() {
		return body.assemble();
	}

	public final Op parseOp(Parser input) {
		return new Op(this, syntax.parse(input));
	}

	@Override
	public String toString() {
		return syntax.toString(arguments);
	}
}

package com.beng.op;

import java.util.List;

import com.beng.SourceFileLocation;
import com.beng.op.args.GivenOpArg;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCall;
import com.beng.op.call.OpCallException;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.opsyntax.OpSyntax;
import com.beng.parser.OpsEntry;
import com.beng.parser.Parser;

public abstract class OpDefinition implements IncompleteOp, OpsEntry {
	private final FullyAppliedOpCall return_type;
	private final OpSyntax syntax;

	public OpDefinition(FullyAppliedOpCall return_type, OpSyntax syntax) {
		this.return_type = return_type;
		this.syntax = syntax;
	}

	@Override
	public OpCall callWith(List<GivenOpArg> arguments, SourceFileLocation location) throws OpCallException {
		return getSyntax().callWith(arguments, this, location);
	}

	@Override
	public OpSyntax getSyntax() {
		return syntax;
	}

	@Override
	public FullyAppliedOpCall getReturnType() {
		return return_type;
	}

	@Override
	public OpCall parse(Parser input, FullyAppliedOpCall type) throws OpParseException {
		return getSyntax().parseCall(input, this, null);
	}

	/** This method "runs" this {@link FullyAppliedOpCall}. This method assumes that the {@link FullyAppliedOpCall}
	 * given is derived using this {@link OpDefinition}. {@link FullyAppliedOpCall#run()} calls this method using
	 * the {@link FullyAppliedOpCall}'s {@link OpDefinition} in order to use inheritance to run {@link SysOp}s and
	 * other special types of operators in different ways as needed. */
	public abstract Assemblable runWith(FullyAppliedOpCall call) throws OpRunException;
}

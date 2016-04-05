package com.beng.op.args;

import java.util.List;

import com.beng.SourceFileLocation;
import com.beng.op.OpDefinition;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCall;
import com.beng.op.call.OpCallException;
import com.beng.op.call.PartiallyAppliedOpCall;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.op.recallentries.ArgRecall;
import com.beng.op.recallentries.RecallEntry;
import com.beng.opsyntax.OpSyntax;
import com.beng.parser.Parser;

public class MissingOpArg implements OpArg, OpSyntax {
	private final FullyAppliedOpCall type;
	private final String name; // can be null if unnamed in an op call, e.g. "<int> = 5"

	public MissingOpArg(FullyAppliedOpCall type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public OpCall callWith(List<GivenOpArg> arguments, RecallEntry previous_arguments, OpDefinition definition,
			SourceFileLocation location) throws OpCallException {
		if (arguments.size() == 0)
			return new PartiallyAppliedOpCall(definition, location, null);
		else
			return new FullyAppliedOpCall(definition, location, new ArgRecall(arguments.get(0), this));
	}

	@Override
	public RecallEntry callWithSubset(List<GivenOpArg> arguments, RecallEntry previous_arguments)
			throws OpCallException {
		if (arguments.size() == 0)
			return null;
		else
			return new ArgRecall(arguments.get(0), this);
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

	@Override
	public OpCall parseCall(Parser input, OpDefinition op_to_parse, RecallEntry recall_entry)
			throws OpParseException {
		return input.parseOp(type);
	}

	@Override
	public FullyAppliedOpCall getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "<" + type.toString() + (name != null ? " " + name : "") + ">";
	}

	@Override
	public String toString(RecallEntry recall_entry) {
		if (recall_entry == null)
			return toString();
		else
			return ((ArgRecall) recall_entry).getValue().toString();
	}
}

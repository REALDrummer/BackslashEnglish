package com.beng.op.call;

import com.beng.SourceFileLocation;
import com.beng.op.CodedOp;
import com.beng.op.OpDefinition;
import com.beng.op.args.OpArg;
import com.beng.op.recallentries.RecallEntry;
import com.beng.opsyntax.OpSyntax;

public abstract class OpCall implements CodedOp {
	private final OpDefinition definition;

	private final SourceFileLocation location;
	private final RecallEntry recall_entry;

	public OpCall(OpDefinition declaration, SourceFileLocation location, RecallEntry recall_entry) {
		this.definition = declaration;

		this.location = location;
		this.recall_entry = recall_entry;
	}

	public OpDefinition getDefinition() {
		return definition;
	}
	
	public String getCapturedLiteral(String name) {
		return recall_entry.getCapturedLiteral(name);
	}
	
	public OpArg getArgument(String name) {
		return recall_entry.getArgument(name);
	}

	@Override
	public SourceFileLocation getLocation() {
		return location;
	}

	public RecallEntry getRecallEntry() {
		return recall_entry;
	}

	@Override
	public FullyAppliedOpCall getReturnType() {
		return definition.getReturnType();
	}

	@Override
	public OpSyntax getSyntax() {
		return definition.getSyntax();
	}

	@Override
	public String toString() {
		return getSyntax().toString(this);
	}
}

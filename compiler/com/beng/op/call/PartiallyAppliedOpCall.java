package com.beng.op.call;

import java.util.List;
import com.beng.SourceFileLocation;
import com.beng.op.IncompleteOp;
import com.beng.op.OpDefinition;
import com.beng.op.args.GivenOpArg;
import com.beng.op.recallentries.RecallEntry;

public class PartiallyAppliedOpCall extends OpCall implements IncompleteOp {
	public PartiallyAppliedOpCall(OpDefinition declaration, SourceFileLocation location, RecallEntry recall_entry) {
		super(declaration, location, recall_entry);
	}
	
	@Override
	public OpCall callWith(List<GivenOpArg> arguments, SourceFileLocation location) throws OpCallException {
		return getSyntax().callWith(arguments, this, location);
	}
}

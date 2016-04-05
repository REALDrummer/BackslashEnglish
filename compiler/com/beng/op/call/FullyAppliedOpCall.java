package com.beng.op.call;

import com.beng.SourceFileLocation;
import com.beng.op.Assemblable;
import com.beng.op.Assemblage;
import com.beng.op.OpDefinition;
import com.beng.op.OpRunException;
import com.beng.op.recallentries.RecallEntry;

public class FullyAppliedOpCall extends OpCall {
	boolean is_in_operator_body = false;

	public FullyAppliedOpCall(OpDefinition declaration, SourceFileLocation location, RecallEntry recall_entry) {
		super(declaration, location, recall_entry);
	}

	public Assemblable run() throws OpRunException {
		return getDefinition().runWith(this);
	}

	/** This method designates this op as being part of an operator definition body; this is important to designate
	 * because it means that all variables declared in this operator must be allocated in the stack memory rather
	 * than the static memory! */
	public FullyAppliedOpCall markDefinitionBody() {
		is_in_operator_body = true;
		return this;
	}
	
	public boolean isInOperatorBody() {
		return is_in_operator_body;
	}
}

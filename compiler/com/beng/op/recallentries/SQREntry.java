package com.beng.op.recallentries;

import java.util.Iterator;

import com.beng.op.args.OpArg;
import com.beng.op.args.QuantifiedOpArg;
import com.beng.op.call.FullyAppliedOpCall;

/** A Single Quantifier {@link RecallEntry} (SQREntry) represents the recall entry of a {@link QuantifiedOpArg}
 * group. */
public class SQREntry implements RecallEntry {
	private final RecallEntry[] recalls;

	public SQREntry(int quantifier_count) {
		this.recalls = new RecallEntry[quantifier_count];
	}

	public int getQuantifierCount() {
		return recalls.length;
	}

	@Override
	public OpArg getArgument(String name) {
		OpArg[] all_arguments = new OpArg[recalls.length];
		FullyAppliedOpCall arguments_type = null;
		for (int i = 0; i < recalls.length; i++) {
			all_arguments[i] = recalls[i].getArgument(name);
			if (all_arguments[i] != null)
				arguments_type = all_arguments[i].getType();
		}

		return new QuantifiedOpArg(arguments_type, all_arguments);
	}

	@Override
	public Iterator<OpArg> getArguments() {
		
	}

	@Override
	public String getCapturedLiteral(String name) {

	}

	@Override
	public Iterator<String> getCapturedLiterals() {

	}

}

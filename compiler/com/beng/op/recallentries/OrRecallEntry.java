package com.beng.op.recallentries;

import java.util.Iterator;

import com.beng.op.args.OpArg;

public class OrRecallEntry implements RecallEntry {
	private int successful_match_group_index;
	private RecallEntry matched_group_recall_entry;

	public OrRecallEntry(int successful_match_group_index, RecallEntry matched_group_QR_entry) {
		this.successful_match_group_index = successful_match_group_index;
		this.matched_group_recall_entry = matched_group_QR_entry;
	}
	
	@Override
	public OpArg getArgument(String name) {
		return matched_group_recall_entry.getArgument(name);
	}
	
	@Override
	public Iterator<OpArg> getArguments() {
		return matched_group_recall_entry.getArguments();
	}
	
	@Override
	public String getCapturedLiteral(String name) {
		return matched_group_recall_entry.getCapturedLiteral(name);
	}
	
	@Override
	public Iterator<String> getCapturedLiterals() {
		return matched_group_recall_entry.getCapturedLiterals();
	}

	public int getSuccessfulMatchGroupIndex() {
		return successful_match_group_index;
	}

	public RecallEntry getMatchedGroupRecallEntry() {
		return matched_group_recall_entry;
	}

}

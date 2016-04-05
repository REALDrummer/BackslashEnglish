package com.beng.op.recallentries;

import java.util.Iterator;

import com.beng.op.args.OpArg;

public interface RecallEntry {
	public OpArg getArgument(String name);
	
	public Iterator<OpArg> getArguments();

	public String getCapturedLiteral(String name);
	
	public Iterator<String> getCapturedLiterals();
}

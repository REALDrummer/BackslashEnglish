package com.beng.op.recallentries;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import com.beng.op.args.MissingOpArg;
import com.beng.op.args.OpArg;

public class ArgRecall implements RecallEntry {
	private final OpArg value;
	private final MissingOpArg arg_entry;

	public ArgRecall(OpArg value, MissingOpArg arg_entry) {
		this.value = value;
		this.arg_entry = arg_entry;
	}

	@Override
	public OpArg getArgument(String name) {
		if (name.equals(arg_entry.getName()))
			return value;
		else
			return null;
	}

	@Override
	public Iterator<OpArg> getArguments() {
		return Arrays.asList(value).iterator();
	}

	@Override
	public String getCapturedLiteral(String name) {
		return null;
	}

	@Override
	public Iterator<String> getCapturedLiterals() {
		return Collections.emptyIterator();
	}

	public OpArg getValue() {
		return value;
	}

	public MissingOpArg getArgEntry() {
		return arg_entry;
	}
}

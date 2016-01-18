package com.beng.opsyntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.beng.op.args.OpArg;

public class OpArguments {
	private ArrayList<OpArg> arguments = new ArrayList<>();
	private Map<String, String> captured_literals = new HashMap<>();

	public OpArguments(OpArguments... prior_results) {
		for (OpArguments prior_result : prior_results) {
			add(prior_result);
		}
	}

	public OpArguments add(OpArguments result) {
		this.arguments = result.getArguments();
		this.captured_literals = result.getCapturedLiterals();

		return this;
	}
	
	public OpArguments add(OpArg argument) {
		arguments.add(argument);
		
		return this;
	}
	
	public boolean add(String literal_name, String captured_literal) {
		if (captured_literals.containsKey(literal_name)) {
			return false;
		} else {
			captured_literals.put(literal_name, captured_literal);
			return true;
		}
	}

	public ArrayList<OpArg> getArguments() {
		return arguments;
	}

	public Map<String, String> getCapturedLiterals() {
		return captured_literals;
	}
}

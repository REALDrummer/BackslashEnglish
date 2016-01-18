package com.beng.opsyntax;

import java.util.LinkedList;
import java.util.List;

import com.beng.parser.Parser;

public class OpOrSyntax extends OpSyntax {
	OpSyntax[] groups;

	protected static OpSyntax parseOneNonOrGroup(Parser input) {
		OpConcatSyntax concat_syntax = new OpConcatSyntax(input);

		if (concat_syntax.getGroups().length == 1)
			return concat_syntax.getGroups()[0];
		else
			return concat_syntax;
	}

	private static OpSyntax[] parseSubsequentGroups(OpSyntax first_group, Parser input) {
		List<OpSyntax> groups_parsed = new LinkedList<>();
		groups_parsed.add(first_group);

		while (input.peekByte() == '|')
			groups_parsed.add(parseOneNonOrGroup(input));

		return groups_parsed.toArray(new OpSyntax[groups_parsed.size()]);
	}

	public OpOrSyntax(Parser input) {
		this(parseOneNonOrGroup(input), input);
	}

	public OpOrSyntax(OpSyntax first_group, Parser input) {
		// the first group is already parsed and given, so just continue to parse the next groups
		groups = parseSubsequentGroups(first_group, input);
	}

	@Override
	public boolean canMatchNothing() {
		for (OpSyntax group : groups)
			if (group.canMatchNothing())
				return true;

		return false;
	}

	public OpSyntax[] getGroups() {
		return groups;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// TODO TEMP
		System.out.println(groups.length + " or groups");

		for (OpSyntax group : groups)
			builder.append(group.toString());

		return builder.toString();
	}
}

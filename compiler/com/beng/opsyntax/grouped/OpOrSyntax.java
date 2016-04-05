package com.beng.opsyntax.grouped;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.beng.op.Op;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.op.recallentries.OrRecallEntry;
import com.beng.op.recallentries.RecallEntry;
import com.beng.op.types.OpType;
import com.beng.opsyntax.OpSyntax;
import com.beng.parser.Parser;

public class OpOrSyntax implements OpGroupedSyntax {
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

	@Override
	public boolean matches(OpType type, OpArguments arguments) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public OpArguments parseCall(Parser input, Op op_to_parse, RecallEntry QR_entry) throws OpParseException {
		/* keeps track of all the OpSyntaxes attempted that threw exceptions indicating the highest match priority failures, i.e. the 
		 * ones closest to matches */
		OpArguments match_args = null;
		Set<String> closest_failure_messages = new HashSet<>();
		char closest_failure_priority = 0;
		int successful_group_index = -1;
		for (int i = 0; i < groups.length; i++) {
			OpSyntax group = groups[i];
			try {
				match_args = group.parseCall(input, op_to_parse, QR_entry);
				successful_group_index = i;
				break;
			} catch (OpParseException exception) {
				// prioritize parse exceptions; save the highest priorities
				if (closest_failure_messages.size() == 0
						|| closest_failure_priority < exception.getMatchPriority()) {
					closest_failure_messages.clear();
					closest_failure_messages.add(exception.getMessage());
					closest_failure_priority = exception.getMatchPriority();
				} else if (closest_failure_priority == exception.getMatchPriority()) {
					closest_failure_messages.add(exception.getMessage());
				} // else do nothing
			}
		}

		if (match_args != null) {
			return new OpArguments(new OrRecallEntry(successful_group_index, match_args.getRecallEntry()),
					match_args);
		} else {
			throw new OpParseException(op_to_parse, closest_failure_priority) {
				private static final long serialVersionUID = 3832643765992487061L;

				@Override
				public String getMessage() {
					String message = "I was unable to parse any of the groups in this \"or\" syntax.\n";
					for (String failure_message : closest_failure_messages) {
						message += "\n" + failure_message;
					}

					return message;
				}
			};
		}
	}

	public OpSyntax[] getGroups() {
		return groups;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		boolean first_group = true;
		for (OpSyntax group : groups) {
			if (first_group)
				first_group = false;
			else
				builder.append("|");

			builder.append(group.toString());
		}

		return builder.toString();
	}

	@Override
	public String toString(OpArguments arguments) {
		return toString();
	}
}

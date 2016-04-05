package com.beng.opsyntax;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import com.beng.SourceFileLocation;
import com.beng.op.IncompleteOp;
import com.beng.op.Op;
import com.beng.op.OpDefinition;
import com.beng.op.args.GivenOpArg;
import com.beng.op.args.OpArg;
import com.beng.op.call.OpCall;
import com.beng.op.call.OpCallException;
import com.beng.op.call.PartiallyAppliedOpCall;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.op.recallentries.RecallEntry;
import com.beng.opsyntax.grouped.OpGroupedSyntax;
import com.beng.opsyntax.grouped.OpOrSyntax;
import com.beng.parser.Parser;
import com.beng.op.args.MissingOpArg;

public interface OpSyntax {
	public static OpSyntax parseDeclaration(String string) {
		return parseDeclaration(new Parser(string));
	}

	public static OpSyntax parseDeclaration(Parser input) {
		OpOrSyntax syntax = new OpOrSyntax(input);

		// make sure that we ended on a ':' as we should
		switch (input.peekByte()) {
		case ':': {
			// all is good
			if (syntax.getGroups().length == 1)
				return syntax.getGroups()[0];
			else
				return syntax;
		}
		case ')': {
			// there is an extra unmatched closing parenthesis that stopped the or group parsing
			input.err(OperatorDefinitionErrorType.UNMATCHED_PARENTHESIS,
					"there was an unmatched closing parenthesis found in an operator definition.\n"
							+ "If you meant to use a literal parenthesis, escape it with a backslash (\"\\)\").");
			return null;
		}
		default:
			// ...what?
			input.err(OperatorDefinitionErrorType.UNEXPECTED_END,
					"the operator definition parsing seems to have ended unexpectedly on a '"
							+ input.peekByte() + "'!");
			return null;
		}
	}

	public default OpCall parseCall(String string, OpDefinition op_to_parse) throws OpParseException {
		return parseCall(new Parser(string), op_to_parse);
	}

	public default OpCall parseCall(Parser input, OpDefinition op_to_parse) throws OpParseException {
		return parseCall(input, op_to_parse, null);
	}

	/** This method parses a call (rather than a declaration) of this {@link OpSyntax}.
	 * 
	 * @param input is the {@link Parser} that will be used to grab and read input.
	 * @param op_to_parse is the {@link OpDefinition} that is being parsed using this syntax; this is useful for
	 *                throwing errors.
	 * 
	 * @return <b>true</b> if the {@link OpCall} parsed is "fully applied", i.e. has no missing arguments;
	 *         <b>false</b> if the {@link OpCall} is partially applied.
	 * 
	 * @throws OpParseException if the {@link Parser} does not include a match to this {@link OpSyntax} at its
	 *                 current location. */
	OpCall parseCall(Parser input, final OpDefinition op_to_parse, RecallEntry recall_entry)
			throws OpParseException;

	public default OpCall callWith(List<GivenOpArg> arguments, OpDefinition definition, SourceFileLocation location)
			throws OpCallException {
		return callWith(arguments, null, definition, location);
	}

	public default OpCall callWith(List<GivenOpArg> arguments, PartiallyAppliedOpCall partial_application,
			SourceFileLocation location) throws OpCallException {
		return callWith(arguments, partial_application.getRecallEntry(), partial_application.getDefinition(),
				location);
	}

	/** This method attempts to call an {@link IncompleteOp} via its {@link OpSyntax}, possibly with some arguments
	 * already filled (as in the case of a {@link PartiallyAppliedOpCall}).<br>
	 * <br>
	 * This method returns via the arguments. Any arguments in <b><tt>arguments</b></tt> that were successfully
	 * matched to an argument in the {@link OpSyntax} will be removed from the <tt>List</tt>; any arguments matched
	 * will be added to the <b><tt>matched_arguments</b></tt> <tt>Map</tt> under the proper name(s).<br>
	 * <br>
	 * By default, this method simply throws an error if it is given any arguments in <b><tt>arguments</b></tt> at
	 * all; this is the default behavior for most {@link OpSyntax} subclasses, which usually simply match literal
	 * characters. The one to most notably override this behavior is {@link OpGroupedSyntax}es and {@link MissingOpArg}.
	 * 
	 * @param arguments is the list of {@link GivenOpArg}s that this method will attempt to use as arguments to this
	 *                {@link IncompleteOp}'s {@link OpSyntax}.
	 * @param previous_arguments is a {@link Map} containing all the names and values of previously found
	 *                {@link OpArg}s. This {@link Map} may be empty, e.g. if it was obtained from an
	 *                {@link OpDefinition}, or it may contain some values from the start, e.g. if it was obtained
	 *                from a {@link PartiallyAppliedOpCall}.
	 * @param definition is the {@link OpDefinition} of the {@link Op} for which this {@link OpSyntax} is being
	 *                parsed. This will be used to construct the {@link OpCall} to return.
	 * @param location is the {@link SourceFileLocation} at which this call was made (or <b>null</b>).
	 * 
	 * @return the {@link OpCall} describing the result of calling the given <b><tt>definition</b></tt> with the
	 *         given <b><tt>arguments</b></tt>, possibly with previously given arguments (<b>
	 *         <tt>previous_arguments</b></tt>).
	 * 
	 * @throws OpCallException if there was any issue while calling, most commonly an argument in <b>
	 *                 <tt>arguments</b></tt> that was not matched to any argument in this {@link OpSyntax}. */
	OpCall callWith(List<GivenOpArg> arguments, RecallEntry previous_arguments, OpDefinition definition,
			SourceFileLocation location) throws OpCallException;

	RecallEntry callWithSubset(List<GivenOpArg> arguments, RecallEntry previous_arguments) throws OpCallException;

	public boolean canMatchNothing();

	@Override
	public String toString();

	public default String toString(OpCall call) {
		return toString(call.getRecallEntry());
	}

	String toString(RecallEntry recall_entry);
}

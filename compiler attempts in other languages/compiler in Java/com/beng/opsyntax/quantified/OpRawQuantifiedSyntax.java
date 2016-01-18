package com.beng.opsyntax.quantified;

import java.math.BigInteger;

import com.beng.opsyntax.OperatorDefinitionErrorType;
import com.beng.opsyntax.quantifiable.QuantifiableOpSyntax;
import com.beng.parser.Parser;

public class OpRawQuantifiedSyntax extends QuantifiedOpSyntax {
	final byte lower_bound;
	final byte upper_bound;

	public OpRawQuantifiedSyntax(QuantifiableOpSyntax inner_group, Parser input) {
		super(inner_group);

		input.parseOrErr("{", "opening raw quantifier brace");

		// parse the lower bound
		BigInteger parsed_lower_bound = input.parseInteger();
		if (parsed_lower_bound == null)
			if (input.peekByte() == '}')
				input.err(OperatorDefinitionErrorType.MISSING_QUANTIFIER_BOUND,
						"there was a raw quantifier with no lower bound! What do you want me to do with this?");
			else
				input.err(OperatorDefinitionErrorType.INVALID_QUANTIFIER_BOUND,
						"I encountered an invalid quantifier lower bound! I expect base-10 numbers (with no commas or decimal points).");
		else if (parsed_lower_bound.compareTo(BigInteger.valueOf(Byte.MAX_VALUE)) == 1)
			input.err(OperatorDefinitionErrorType.LARGE_QUANTIFIER_BOUND,
					"I encountered a very large raw quantifier lower bound; quantifier bounds should be less than 128.");
		else if (parsed_lower_bound.compareTo(BigInteger.ZERO) == -1)
			input.err(OperatorDefinitionErrorType.NEGATIVE_QUANTIFIER_BOUND,
					"I encountered a negative raw quantifier lower bound."
							+ "That doesn't make sense; it implies that you want to match a negative amount of the group before it.");

		// convert the parsed lower bound to a byte
		byte lower_bound = parsed_lower_bound.byteValue();

		// parse the optional comma and optional upper bound
		switch (input.peekByte()) {
		case '-':
			input.verboseDebug(
					"found '-' in raw quantifier; checking for upper bound or designating 'or less'...");
			input.readByte(); // skip the comma

			// attempt to parse the upper bound
			BigInteger parsed_upper_bound = input.parseInteger();
			// if there was no integer to parse, there was no upper bound given, which means the '-' means
			// 'or less'
			if (parsed_upper_bound == null) {
				input.verboseDebug("no upper bound found; designating 'or less'");
				upper_bound = lower_bound;
				lower_bound = 0;
			} // if there was an integer parsed, make sure it's valid and in range, then save it
			else if (parsed_upper_bound.compareTo(BigInteger.valueOf(Byte.MAX_VALUE)) == 1) {
				input.err(OperatorDefinitionErrorType.LARGE_QUANTIFIER_BOUND,
						"I encountered a very large raw quantifier upper bound; quantifier bounds should be less than 128.");
				upper_bound = NO_UPPER_BOUND;
			} else if (parsed_lower_bound.compareTo(BigInteger.ZERO) == -1) {
				input.err(OperatorDefinitionErrorType.NEGATIVE_QUANTIFIER_BOUND,
						"I encountered a negative raw quantifier upper bound."
								+ "That doesn't make sense; it implies that you want to match a negative amount of the group before it.");
				upper_bound = NO_UPPER_BOUND;
			} else if (parsed_lower_bound.compareTo(BigInteger.valueOf(lower_bound)) == 1) {
				input.err(OperatorDefinitionErrorType.INVALID_QUANTIFIER_BOUND,
						"I encountered an upper bound of " + parsed_lower_bound.byteValue()
								+ ", which is less than the lower bound of "
								+ lower_bound
								+ "! If these are the bounds you want, please reorder them.");
				upper_bound = NO_UPPER_BOUND;
			} else {
				input.verboseDebug("upper bound found");
				upper_bound = parsed_upper_bound.byteValue();
			}
			break;
		case '+':
			input.verboseDebug("found '+' in raw quantifier; designating 'or more'");
			input.readByte(); // consume the '+'
			upper_bound = NO_UPPER_BOUND;
			break;
		default:
			// if there is no upper bound or '+' or '-', then the upper bound is equal to the lower bound
			upper_bound = lower_bound;
		}

		// check for invalid extra characters
		if (input.readByte() != '}')
			input.err(OperatorDefinitionErrorType.INVALID_QUANTIFIER_BOUND,
					"I encountered an invalid quantifier "
							+ (upper_bound == NO_UPPER_BOUND ? "lower" : "upper")
							+ " bound! I expect base-10 numbers (with no commas or decimal points).");

		this.lower_bound = lower_bound;
	}

	@Override
	public byte getLowerBound() {
		return lower_bound;
	}

	@Override
	public byte getUpperBound() {
		return upper_bound;
	}

	@Override
	public String quantifierToString() {
		if (lower_bound == upper_bound)
			return "{" + lower_bound + "}";
		else if (upper_bound == NO_UPPER_BOUND)
			return "{" + lower_bound + "+}";
		else
			return "{" + lower_bound + "-" + upper_bound + "}";
	}

}

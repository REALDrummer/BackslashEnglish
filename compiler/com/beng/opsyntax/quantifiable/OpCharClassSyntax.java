package com.beng.opsyntax.quantifiable;

import java.util.LinkedList;
import java.util.List;

import com.beng.op.Op;
import com.beng.op.parseexceptions.NoMatchException;
import com.beng.opsyntax.OpArguments;
import com.beng.opsyntax.OperatorDefinitionErrorType;
import com.beng.parser.Parser;

public class OpCharClassSyntax extends QuantifiableOpSyntax {
	public static class OpSyntaxCharRange {
		char lower_bound;
		char upper_bound;

		public OpSyntaxCharRange(Parser input) {
			lower_bound = input.parseEscapedChar();

			if (input.peekByte() == '-') {
				input.readByte();
				switch (input.peekByte()) {
				// if the char range has a dash and no upper bound, throw an error
				case ']':
					input.err(OperatorDefinitionErrorType.BAD_CHAR_RANGE,
							"I encountered a char range with no upper bound given!\n"
									+ "In regex, this dash would just be literal, but in this language, it must be escaped with a backslash.");
					break;
				// if the char range uses a regex escaped char, throw an error
				case 'b':
				case 'B':
				case 's':
				case 'S':
				case 'w':
				case 'W':
				case 'd':
				case 'D':
					input.err(OperatorDefinitionErrorType.BAD_CHAR_RANGE,
							"I encountered a char range with a regex special escape sequence as an upper bound!"
									+ "That doesn't make sense!");
					break;
				case '\\':
					upper_bound = input.parseEscapedChar();
					break;
				default:
					upper_bound = (char) input.readByte();
				}
			}
		}

		public OpSyntaxCharRange(char lower_bound) {
			this.lower_bound = lower_bound;
			upper_bound = lower_bound;
		}

		public OpSyntaxCharRange(char lower_bound, char upper_bound) {
			this.lower_bound = lower_bound;
			this.upper_bound = upper_bound;
		}

		public boolean includes(char character) {
			return character <= upper_bound && character >= lower_bound;
		}
		
	}

	private final OpSyntaxCharRange[] ranges;
	private final boolean negated;

	public OpCharClassSyntax(Parser input) {
		input.parseOrErr("[", "opening bracket of a char class");

		// check for a negating '^'
		if (input.peekByte() == '^') {
			input.readByte();
			negated = true;
		} else
			negated = false;

		// parse the characters inside
		List<OpSyntaxCharRange> ranges = new LinkedList<>();
		while (input.peekByte() != ']')
			if (input.peekByte() == -1)
				input.err(OperatorDefinitionErrorType.INCOMPLETE_DEFINITION,
						"someone forgot to complete a character class in an operator definition!\n"
								+ "If you want to use a literal \"[\" instead of starting a character class, "
								+ "please escape it with a backslash.");
			else
				ranges.add(new OpSyntaxCharRange(input));
		this.ranges = ranges.toArray(new OpSyntaxCharRange[ranges.size()]);

		input.parseOrErr("]", "closing bracket of a char class");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

	public boolean isNegated() {
		return negated;
	}

	public OpSyntaxCharRange[] getRanges() {
		return ranges;
	}
	
	@Override
	public OpArguments parse(Parser input, Op op_to_parse) {
		byte character = input.peekByte();
		for (OpSyntaxCharRange range : ranges) {
			if (range.includes((char) character))
				if (negated)
					throw new NoMatchException(op_to_parse, this);
				else
					return null;
		}
		
		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");

		for (OpSyntaxCharRange range : ranges)
			builder.append(range.toString());

		builder.append("]");
		return builder.toString();
	}

}

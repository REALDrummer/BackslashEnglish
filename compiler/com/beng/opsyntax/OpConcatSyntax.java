package com.beng.opsyntax;

import java.util.ArrayList;

import com.beng.opsyntax.quantifiable.*;
import com.beng.opsyntax.quantifiable.escaped.*;
import com.beng.opsyntax.quantified.*;
import com.beng.parser.Parser;

public class OpConcatSyntax extends OpSyntax {
	final OpSyntax[] groups;

	private static final OpSyntax parseEscapeSequence(Parser input) {
		input.parseOrErr("\\", "escape sequence backslash");

		/* find a valid escape for the next character */
		byte escaped_char = input.peekByte();
		input.ungetByte(); // unget the backslash to allow constructors to parse it
		switch (escaped_char) {
		case -1:
			input.err(OperatorDefinitionErrorType.INCOMPLETE_DEFINITION,
					"I reached the end of a file while attempting to parse an escape sequence!");
			return null;
		// escapes representing meta-characters, e.g. '\b' => word boundary (regex)
		case 'b':
			return new OpWordBoundarySyntax(input);
		case 'B':
			return new OpNonWordBoundarySyntax(input);
		case 'd':
			return new OpDigitSyntax(input);
		case 'D':
			return new OpNonDigitSyntax(input);
		case 'w':
			return new OpWordCharSyntax(input);
		case 'W':
			return new OpNonWordCharSyntax(input);
		case 's':
			return new OpWhitespaceCharSyntax(input);
		case 'S':
			return new OpNonWhitespaceCharSyntax(input);
		case 'Q':
			return new OpMetaquoteSyntax(input);
		default:
			// hope that it's an escape character for a literal, e.g. '\n'
			return new OpLiteralCharSyntax(input);
		}
	}

	public OpConcatSyntax(Parser input) {
		input.debug("parsing op concat syntax...");
		/* just continue to parse op syntaxes case-by-case; here are the possible cases:
		 * CASE 1: a premature EOF
		 * error case;
		 * handled by the while loop's condition and checks after the loop
		 * CASE 2: a terminator
		 * end the parsing;
		 * handled by the while loop's condition;
		 * CASE 3: '|', an "or" delimiter where "or" has a greater precedence than concatenation;
		 * ends the op concat syntax;
		 * handled in the while loop's condition;
		 * CASE 4: ')', a capturing group terminator;
		 * ends the op concat syntax;
		 * handled in the while loop's condition;
		 * CASE 5: an escaped character (with a '\')
		 * capture one character (e.g. \n) or a metacharacter (e.g. \b or \Q)
		 * handled inside the while loop's switch statement
		 * CASE 6: a special character that starts an operator syntax region of lower precedence
		 * start parsing that operator syntax region;
		 * handled inside the while loop's switch statement;
		 * this case can be caused by one of the following characters:
		 * * '(' to open a group, e.g. a capturing group
		 * * '[' to open a character class
		 * * '<' to start parsing an argument
		 * * '`' to start parsing a string insertion
		 * handled inside the while loop's switch statement
		 * CASE 7: a quantifier start character;
		 * in the switch/case, this is an error that indicates that the quantifier comes with either no group or a non-quantifiable group before it;
		 * after the switch/case, it will be handled properly if the last group parsed was quantifiable;
		 * CASE 8: a regular character, signifying the start of a literal group;
		 * handled inside the while loop's switch statement */
		ArrayList<OpSyntax> groups = new ArrayList<>();

		byte last_character;
		while ((last_character = input.peekByte()) != -1 /* CASE 1 */ && last_character != ':' /* CASE 2 */
				&& last_character != '|' /* CASE 3 */ && last_character != ')' /* CASE 4 */) {
			OpSyntax group_parsed;
			if (Character.isWhitespace(last_character)) {
				input.verboseDebug("found whitespace character; parsing whitespace group...");
				group_parsed = new OpWhitespaceGroupSyntax(input);
			} else {
				switch (last_character) {
				// CASE 5 starts below
				case '\\': {
					input.verboseDebug("found '\\'; parsing escape sequence...");
					group_parsed = parseEscapeSequence(input);
					break;
				}
				case '.': {
					input.verboseDebug("found '.'; parsing dot metacharacter...");
					group_parsed = new OpDotSyntax(input);
					break;
				}
					// CASE 6 starts below
				case '(': {
					input.verboseDebug("found '('; parsing capturing group...");
					group_parsed = new OpGroupSyntax(input);
					break;
				}
				case '[': {
					input.verboseDebug("found '['; parsing char class...");
					group_parsed = new OpCharClassSyntax(input);
					break;
				} // TODO: add angle-bracket-delimited arguments and backtick-delimited insertions here
					// CASE 7 (part 1, switch/case error case) starts below
				case '{':
				case '*':
				case '+':
				case '?': {
					String message;
					if (groups.isEmpty())
						message = "I found a quantifier with nothing before it!";
					else
						message = "I found a quantifier with a non-quantifiable group before it!";

					input.err(OperatorDefinitionErrorType.NO_OBJECT_FOR_QUANTIFIER, message);
					group_parsed = null;
					break;
				}
				default: // CASE 8 starts below
					input.verboseDebug("found "
							+ OpLiteralCharSyntax.toPrintable((char) last_character)
							+ "; parsing literal char...");
					group_parsed = new OpLiteralCharSyntax(input);
				}
			}

			// CASE 7 (part 2, quantifier parsing case) starts below
			if (group_parsed instanceof QuantifiableOpSyntax) {
				QuantifiedOpSyntax quantified_wrapper = QuantifiedOpSyntax.parseQuantifier(input,
						(QuantifiableOpSyntax) group_parsed);
				if (quantified_wrapper != null)
					// null means there was no quantifier, only a plain group
					group_parsed = quantified_wrapper;
			}

			groups.add(group_parsed);
		}

		switch (last_character) {
		// CASE 1
		case -1:
			input.err(OperatorDefinitionErrorType.INCOMPLETE_DEFINITION,
					"I reached the end of a file while attempting to parse an operator definition!");
			break;
		// CASE 2
		case ':':
			input.debug("ended on operator definintion terminator, ':'");
			break;
		// CASE 3
		case '|':
			input.debug("ended on or group delimiter, '|'");
			break;
		// CASE 4
		case ')':
			input.debug("ended on capturing group terminator, ')'");
			break;
		}

		this.groups = groups.toArray(new OpSyntax[groups.size()]);
	}

	public OpConcatSyntax(OpSyntax[] groups) {
		this.groups = groups;
	}
	
	public OpSyntax[] getGroups() {
		return groups;
	}

	@Override
	public boolean canMatchNothing() {
		for (OpSyntax opSyntax : groups)
			if (!opSyntax.canMatchNothing())
				return false;

		return true;
	}

	@Override
	public String toString() {
		StringBuilder concat = new StringBuilder();

		for (OpSyntax op_syntax : groups) {
			concat.append(op_syntax.toString());
		}

		return concat.toString();
	}
}

/*
 * operator syntaxes.h
 *
 *  Created on: Oct 24, 2015
 *      Author: connor
 */

#ifndef OPERATOR_SYNTAXES_H_
#define OPERATOR_SYNTAXES_H_

#include "parser.h"
#include "operator arguments.h"

#include <istream>
#include <vector>

#define OPERATOR_DEFINITION_TERMINATOR ':'

#define NUMBER_OF_OPERATOR_DEFINITION_ERROR_TYPES 7
enum operator_definition_error_type {
	/** This error type specifies that the operator definition stopped unexpectedly, e.g. an EOF was found */
	/* The purpose of assigning the first of these enum values to the number of these other error types is to avoid conflicts between the numerical values of the error types,
	 * allowing them to be used as exit values to the program. */
	INCOMPLETE_DEFINITION = 0x10000000,
	NO_OBJECT_FOR_QUANTIFIER,
	QUANTIFYING_QUANTIFIER,
	NO_OBJECT_FOR_OR,
	POTENTIALLY_EMPTY_OR_GROUP,
	EMPTY_METAQUOTE,
	BAD_ESCAPED_CHARACTER
};

// TODO: be sure to free all pointer instance variables in op syntax subclass destructors

class op_syntax {
public:
	virtual ~op_syntax() {
	}

	// TODO: implement in the main .cpp file
	virtual string parse(parser input);

	virtual bool isQuantifiedGroup() {
		return false;
	}

	virtual bool canBeEmpty();

	virtual string toString();
};

class op_quantified_syntax: public op_syntax {
	op_syntax& previous_group;
	unsigned int lower_bound, upper_bound;

public:
	op_quantified_syntax(parser input, op_syntax previous_group) :
			previous_group(previous_group) {
		this->previous_group = previous_group;

		// parse over the "{"
		// TODO
	}

	bool isQuantifiedGroup() {
		return true;
	}
};

class op_metaquote_syntax: public op_syntax {
private:
	string literal;
public:
	op_metaquote_syntax(parser input) {
		if (input.parse("\\Q"))
			input.err(INTERNAL_ERROR, "I attempted to parse a 'metaquote' syntax when there was no '\\Q'! How did that happen?");
		vector<char> literal;
		while (true) {
			if (input.peek() == '\\') {
				input++;
				if (input.peek() == 'E') {
					input++;
					break;
				} else {
					input--;
				}
			}
			literal.push_back(++input);
		}

		if (literal.size() == 0)
			input.err(EMPTY_METAQUOTE, "there was an empty metaquote region!");

		this->literal = *new string(&literal[0]);
	}
};

class op_literal_syntax: public op_syntax {
private:
	string literal;

public:
	op_literal_syntax(parser input) {
		char special_characters[] = { '\\', '(', '[', '|', '{', '*', '+', '?', ')', ']', '}', '<' };
		bool is_special_char = false;
		unsigned int literal_group_length = 0;
		vector<char> literal;
		do {
			literal_group_length++;
			for (unsigned int i = 0; i < sizeof(special_characters); i++)
				if (special_characters[i] == input.peek()) {
					is_special_char = true;
					break;
				}
			literal.push_back(++input);
		} while (is_special_char);

		this->literal = *new string(&literal[0], literal_group_length);
	}
};

class op_group_syntax: public op_syntax {
private:
	op_syntax contained_group;

public:
	op_group_syntax(parser input);
};

class op_char_class_syntax: public op_syntax {
private:
	class char_range {
	private:
		char lower_bound;
		char upper_bound;
	public:
		char_range(parser input) {
			lower_bound = ++input;

			if (input.peek() == '-') {
				input++;
				if (input.peek() == ']') {
					lower_bound = '-';
				} else {
					upper_bound = ++input;
				}
			}
		}
	};

	char_range* character_ranges;

public:
	op_char_class_syntax(parser input) {
		// TODO
	}
};

class op_or_syntax: public op_syntax {
	op_syntax* groups;

public:
	op_or_syntax(parser input, const op_syntax& previous_group);
};

class op_concat_syntax: public op_syntax {
private:
	op_syntax* pieces;
	unsigned int length;

public:
	op_concat_syntax(parser input) {
		/* just continue to parse op syntaxes case-by-case; here are the possible cases:
		 * CASE 1: a premature EOF
		 * error case;
		 * handled by the while loop's condition and checks after the loop
		 * CASE 2: a terminator
		 * end the parsing;
		 * handled by the while loop's condition and checks after the loop;
		 * either OPERATOR_DEFITION_TERMINATOR or a ')' can cause this case
		 * CASE 3: an escaped character (with a '\')
		 * capture one character (e.g. \n) or a metacharacter (e.g. \b) OR start a metaquote region (with \Q);
		 * handled inside the while loop's switch statement
		 * CASE 4: a special character that starts an operator syntax region of lower precedence
		 * start parsing that operator syntax region;
		 * handled inside the while loop's switch statement;
		 * this case can be caused by one of the following characters:
		 * * '(' to open a group, e.g. a capturing group
		 * * '[' to open a character class
		 * * '<' to start parsing an argument
		 * * '`' to start parsing a string insertion
		 * CASE 5: another special character that delimits something of greater precedence, namely ')' or '|'
		 * ends the op concat syntax
		 * handled inside the while loop's switch statement
		 * CASE 6: another special character that is or starts a special notation, e.g. '|' or '*'
		 * use the previously captured group as the first argument and call the appropriate constructor
		 * handled inside the while loop's switch statement
		 * CASE 7: a regular character, signifying the start of a literal group;
		 * handled inside the while loop's switch statement */
		char last_character;
		op_syntax* last_syntax_group_parsed = nullptr;
		while ((last_character = input.peek()) != EOF /* CASE 1 */&& last_character != OPERATOR_DEFINITION_TERMINATOR /* CASE 2 */) {
			switch (last_character) {
			// CASE 3 starts below
			case '\\': {
				input++;	// parse over the '\'
				/* find a valid escape for the next character */
				switch (input.peek()) {
				case EOF:  // escapes representing the use of special characters as their literal selves, e.g. '\(' => regular parenthese
					input.err(INCOMPLETE_DEFINITION, "I reached the end of a file while attempting to parse an operator definition!");
				case '(':
					// TODO
					break;
				case 'n':  // escapes representing special literal characters, e.g. '\n' => new line
					// TODO
					break;
				case 'b':  // escapes representing meta-characters, e.g. '\b' => word boundary (regex)
					// TODO
					break;
				case 'Q':  // starts a metaquote region
					input--;  // unget the backslash
					last_syntax_group_parsed = new op_metaquote_syntax(input);
					break;
				}
				break;
			}
			case '(': { // CASE 4 starts below
				// opening parentheses start groups
				last_syntax_group_parsed = new op_group_syntax(input);
				break;
			}
			case '[': {
				// opening brackets start character classes
				last_syntax_group_parsed = new op_char_class_syntax(input);
				break;
			} // TODO: more on CASE 4
			case '|': {  // CASE 5 starts below
				if (last_syntax_group_parsed == NULL)
					input.err(NO_OBJECT_FOR_OR,
							"I found a '|' in an operator definition with no group before it!\n"
									"Yeah, okay, that may be acceptable in some regex, but it's not okay here. If you want to use the group or nothing, please use the '?'.");
				last_syntax_group_parsed = new op_or_syntax(input, *this);
				break;
			}

			case '{':
			case '*':
			case '+':
			case '?': {
				// make sure the postfix quantifiers have a group before them
				if (last_syntax_group_parsed == NULL)
					input.err(NO_OBJECT_FOR_QUANTIFIER, "this '%c' quantifier has no group before it! That doesn't make sense!", input.peek());
				// make sure that the postfix quantifiers have a non-quantifier group before them
				else if (last_syntax_group_parsed->isQuantifiedGroup())
					input.err(QUANTIFYING_QUANTIFIER, "this '%c' quantifier is quantifying an already quantified group!", input.peek());

				switch (input.peek()) {
				case '{': {
					// TODO
					break;
				}
				case '*': {
					// TODO
					break;
				}
				case '+': {
					// TODO
					break;
				}
				case '?': {
					// TODO
					break;
				}
				}
				break;
			}
			default: {  // CASE 6 starts below
				last_syntax_group_parsed = new op_literal_syntax(input);
			}
			}
		}

		// CASE 1
		if (last_character == EOF)
			input.err(INCOMPLETE_DEFINITION, "I reached the end of a file while attempting to parse an operator definition!");
	}

	string parse(parser input) {
		// TODO
		return NULL;
	}

	op_syntax* getPieces() {
		return pieces;
	}

	unsigned int getLength() {
		return this->length;
	}
};

op_syntax parseGroup(parser input) {
	op_syntax* parsed_group = new op_concat_syntax(input);
	if (((op_concat_syntax*) parsed_group)->getLength() == 0)
		parsed_group = &((op_concat_syntax*) parsed_group)->getPieces()[0];
	return *parsed_group;
}

op_group_syntax::op_group_syntax(parser input) {
	// parse the opening parenthese
	if (!input.parse("("))
		input.err(INTERNAL_ERROR, "I attempted to parse a 'capturing group' syntax when there was no '('! How did that happen?");
	contained_group = parseGroup(input);

	// parse the closing parenthese
	if (!input.parse(")")) {

	}
}

op_or_syntax::op_or_syntax(parser input, const op_syntax& previous_group) {
// continually parse the next group as a concat syntax; because '|' has a lower precedence, the concat parser will stop if it hits another '|'
	vector<op_syntax> or_groups;
	or_groups.push_back(previous_group);
	do {
		if (!input.parse("|"))
			input.err(INTERNAL_ERROR, "I attempted to parse an 'or' syntax when there was no '|'! How did that happen?");

		op_syntax next_group = parseGroup(input);
		if (next_group.canBeEmpty())
			input.err(POTENTIALLY_EMPTY_OR_GROUP, "This \"%s\" portion of this 'or' group could match an empty string! That's not allowed!",
					next_group.toString().c_str());
		or_groups.push_back(next_group);
	} while (input.peek() == '|');

	groups = &or_groups[0];
}

char parser::parseEscapedCharacter(parser input) {
// TODO: at some point, characters of larger sizes will need to be used for \u, \U, and \x
// parse the escaping backslash
	parse("\\");

	switch (++(*this)) {
	case 'n':
		return "\n"[0];
	case 't':
		return "\t"[0];
	case 'v':
		return "\v"[0];
	case 'f':
		return "\f"[0];
	case 'r':
		return "\r"[0];
	case '0':
		return "\0"[0];
	case '\\':
		return "\\"[0];
	default:
		err(BAD_ESCAPED_CHARACTER, "an invalid \"\\%c\" escaped character was found.", *input);
		return -1;
	}
}

#endif /* OPERATOR_SYNTAXES_H_ */

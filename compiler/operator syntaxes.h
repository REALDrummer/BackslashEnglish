/*
 * operator syntaxes.h
 *
 *  Created on: Oct 24, 2015
 *      Author: connor
 */

#ifndef OPERATOR_SYNTAXES_H_
#define OPERATOR_SYNTAXES_H_

#define OPERATOR_DEFINITION_TERMINATOR ':'

#include "messaging and debugging utils.h"
#include "operator arguments.h"

#define NUMBER_OF_OPERATOR_DEFINITION_ERROR_TYPES 1
enum operator_definition_error_type {
	/** This error type specifies that the operator definition stopped unexpectedly, e.g. an EOF was found */
	/* The purpose of assigning the first of these enum values to the number of these other error types is to avoid conflicts between the numerical values of the error types, allowing
	 * them to be used as exit values to the program. */
	INCOMPLETE_DEFINITION = NUMBER_OF_REGULAR_ERROR_TYPES + NUMBER_OF_ARGUMENT_ERROR_TYPES
};

class op_group_syntax: op_syntax {
	// TODO
};

class op_char_class_syntax: op_syntax {
	// TODO
};

class op_concat_syntax: op_syntax {
private:
	op_syntax* concatenated_op_syntaxes;

public:
	op_concat_syntax(istream input) :
			op_syntax(input) {
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
		 * CASE 4: a special character that starts an operator syntax region
		 * start parsing that operator syntax region;
		 * handled inside the while loop's switch statement;
		 * this case can be caused by one of the following characters:
		 * * '(' to open a group, e.g. a capturing group
		 * * '[' to open a character class
		 * * '<' to start parsing an argument
		 * * '`' to start parsing a string insertion
		 * CASE 5: another special character, e.g. "|" or "{" with no group before it
		 * error case (because these cases will be parsed in CASE 6 below);
		 * handled inside the while loop's switch statement
		 * CASE 6: a regular character, signifying the start of a literal group;
		 * handled inside the while loop's switch statement */
		char last_character;
		while ((last_character = input.peek()) >= 0 && last_character != OPERATOR_DEFINITION_TERMINATOR) {
			op_syntax last_op_syntax_piece_parsed;
			switch (last_character) {
			// CASE 3 starts below
			case '\\':
				input.get();	// parse over the '\'
				/* find a valid escape for the next character */
				switch (input.get()) {
				// EOF
				case EOF:
					LOCATIONAL_ERR(INCOMPLETE_DEFINITION, "I reached the end of a file while attempting to parse an operator definition!");
					// escapes representing the use of special characters as their literal selves, e.g. '\(' => regular parenthese
				case '(':
					// TODO
					break;
					// escapes representing special literal characters, e.g. '\n' => new line
				case 'n':
					// TODO
					break;
					// escapes representing meta-characters, e.g. '\b' => word boundary (regex)
				case 'b':
					// TODO
					break;
				}
				break;
				// CASE 4 starts below
			case '(':
				// opening parentheses start groups
				last_op_syntax_piece_parsed = new op_group_syntax(input);
				break;
			case '[':
				// opening brackets start character classes
				last_op_syntax_piece_parsed = new op_char_class_syntax(input);
				break;
				// CASE 5 starts below
				// TODO
				// CASE 6 starts below
			default:
				// TODO
			}
		}

		// if we hit an EOF, but we haven't found a terminating character for the operator definition yet, throw an error
		if (last_character == EOF)
			LOCATIONAL_ERR(INCOMPLETE_DEFINITION, "I reached the end of a file while attempting to parse an operator definition!");
	}

	string parse(istream input) {
		// TODO
		return NULL;
	}
};

class op_syntax {
private:
	/** This contains the overall syntax of the operator. It may be any sub-type  */
	op_syntax syntax;

public:
	virtual op_syntax(istream input) {

	}

	virtual ~op_syntax();

	// TODO: implement in the main .cpp file
	virtual string parse(istream input);

	virtual string toString();

	op_syntax parseSyntaxElement(istream input) {
		// TODO
		return NULL;
	}
};

#endif /* OPERATOR_SYNTAXES_H_ */

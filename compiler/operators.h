/*
 * operators.h
 *
 *  Created on: Oct 8, 2015
 *      Author: connor
 */

#ifndef OPERATORS_H_
#define OPERATORS_H_

using namespace std;

#include <stdlib.h>
#include <string>
#include <string.h>
#include <stdio.h>
#include <tuple>

#include "messaging and debugging utils.h"

enum beng_error_types {
	OPERATOR_DECLARATION = NUMBER_OF_REGULAR_ERROR_TYPES
};

class input_reader {
	// TODO
};

class op_syntax {
private:
	string* separators;
	missing_argument* arguments;
	char number_of_arguments;

public:
	op_syntax(string* separators, char number_of_separators, argument* arguments, char number_of_arguments) {
		// just make sure that there is one more separator than there are arguments
		if (number_of_arguments != number_of_separators - 1)
			ERR(INTERNAL,
					"This op has %u arguments and %u separators! What?!\nThere should be separators on either end of the op and in between all the arguments, so we should have 1 more separator than arguments at all times!",
					number_of_arguments, number_of_separators);

		op(separators, arguments, number_of_arguments);
	}

	op_syntax(string* separators, argument* arguments, char number_of_arguments) {
		this->separators = separators;
		this->arguments = arguments;
		this->number_of_arguments = number_of_arguments;
	}

	// TODO: implement in the main .cpp file
	virtual tuple<string, op> parse(input_reader input);

	virtual string toString() {

	}
};

class type  : op {
	virtual bool matches(op* op_to_match) {
		// TODO
	}
};

class argument {
private:
	type argument_type;

public:
	virtual bool isGiven();

	argument(type argument_type) {
		this->argument_type = argument_type;
	}
};

class missing_argument: argument {
public:
	bool isGiven() {
		return false;
	}

	bool matches(op* given_argument) {
		// TODO
		return false;
	}
};

class given_argument {
private:
	op given_op;

public:
	bool isGiven() {
		return false;
	}
};

class op {
private:
	op_syntax syntax;
	op* given_arguments;
	type return_type;

public:
	op(op_syntax syntax, type return_type, op* given_arguments) {
		this->syntax = syntax;
		this->return_type = return_type;
		this->given_arguments = given_arguments;
	}

	virtual ~op() {
		free (separators);
		free(arguments);
	}

	virtual string toString() {
		return "<" + return_type.toString() + "> " + syntax.toString();
	}
};

class sysop: op {
	// TODO
};

#endif /* OPERATORS_H_ */

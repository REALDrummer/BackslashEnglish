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

#include "messaging and debugging utils.h"

#include "operator arguments.h"
#include "operator syntaxes.h"

class type: op {
	virtual bool matches(op* op_to_match) {
		// TODO
		return true;
	}
};

class op {
private:
	op_syntax syntax;
	argument* arguments;
	unsigned int number_of_arguments;
	type return_type;

public:
	op(op_syntax syntax, type return_type, argument* arguments, unsigned int number_of_arguments) {
		this->syntax = syntax;
		this->return_type = return_type;
		this->arguments = arguments;
		this->number_of_arguments = number_of_arguments;
	}

	virtual ~op() {
		if (number_of_arguments > 0)
			free(arguments);
	}

	virtual string toString() {
		return "<" + return_type.toString() + "> " + syntax.toString();
	}

	virtual op run(argument ... arguments) {
		// TODO: run a normal op
		return NULL;
	}
};

class sysop: op {
	virtual op run(given_argument ... arguments, op ... contextual_ops);
};

#endif /* OPERATORS_H_ */

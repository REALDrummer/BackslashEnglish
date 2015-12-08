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

class type;

#include "operator arguments.h"
#include "operator syntaxes.h"
#include "values.h"

class op {
private:
	op_syntax syntax;
	argument* arguments;
	unsigned int number_of_arguments;
	type& return_type;

public:
	op(op_syntax syntax, type& return_type, argument* arguments, unsigned int number_of_arguments) : syntax(syntax), return_type(return_type) {
		this->syntax = syntax;
		this->arguments = arguments;
		this->number_of_arguments = number_of_arguments;
	}

	virtual ~op() {
		if (number_of_arguments > 0)
			free(arguments);
	}

	virtual string toString();

	virtual value run() {
		// TODO
	}
};

class type: public op {
	virtual bool matches(op* op_to_match) {
		// TODO
		return true;
	}
};

class sysop: public op {
	virtual op run(given_argument* arguments, op* contextual_ops);
};

string op::toString() {
	return "<" + return_type.toString() + "> " + syntax.toString();
}

#endif /* OPERATORS_H_ */

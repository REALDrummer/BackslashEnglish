/*
 * operators.h
 *
 *  Created on: Oct 8, 2015
 *      Author: connor
 */

#ifndef OPERATORS_H_
#define OPERATORS_H_

using std::string;

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
	op_syntax& syntax;
	argument* arguments;
	unsigned int number_of_arguments;
	op& return_type;

public:
	op(op_syntax& syntax, op& return_type, argument* arguments, unsigned int number_of_arguments) : syntax(syntax), return_type(return_type) {
		this->syntax = syntax;
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

	virtual value& run(given_argument* arguments, op* contextual_ops) = 0;
};

class sysop: public op {
	virtual string toString() = 0;
};

#endif /* OPERATORS_H_ */

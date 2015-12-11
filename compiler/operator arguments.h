/*
 * operator arguments.h
 *
 *  Created on: Oct 24, 2015
 *      Author: connor
 */

#ifndef OPERATOR_ARGUMENTS_H_
#define OPERATOR_ARGUMENTS_H_

#define NUMBER_OF_ARGUMENT_ERROR_TYPES 1
enum operator_argument_error_type {
	/* TODO: fill in this enum with values as needed */
	/* The purpose of assigning the first of these enum values to the number of these other error types is to avoid conflicts between the numerical values of the error types, allowing
	 * them to be used as exit values to the program. */
	TODO_NAME_ME = 0x01000000
};

class op;
class type;

class argument {
public:
	virtual bool isGiven();

	virtual type& getType();
};

class missing_argument: argument {
public:
	bool isGiven() {
		return false;
	}

	bool matches(op& given_argument) {
		// TODO
		return false;
	}
};

class given_argument {
private:
	op& given_value;

public:
	given_argument(op& given_value) :
			given_value(given_value) {
	}

	bool isGiven() {
		return true;
	}
};

#endif /* OPERATOR_ARGUMENTS_H_ */

/*
 * operator utils.h
 *
 *  Created on: Nov 23, 2015
 *      Author: connor
 */

#ifndef PARSER_H_
#define PARSER_H_

#include <iostream>
#include <istream>
#include <fstream>
#include <sstream>
#include <vector>
#include <stack>
#include <cstdarg>
#include <stdio.h>
#include <climits>
#include <tuple>

using std::string;
using std::stack;
using std::tuple;
using std::ifstream;
using std::ostringstream;
using std::streampos;
using std::cout;

#define NUMBER_OF_PARSER_ERROR_TYPES 1
enum parser_error_type {
	NUMBER_TOO_LARGE = 0x02000000
};

class parser {
private:
	ifstream& input_file;
	string input_file_path;
	unsigned int line_number;
	unsigned int char_number;

	char last_char;
	stack<unsigned int> line_lengths;

	string message_header() {
		ostringstream message;

		message << "At char " << char_number << " on line " << line_number << ", ";

		return message.str();
	}

	unsigned int parseEscapedCharacter(parser* input);

public:
	parser(string input_file_path) :
			input_file(*new ifstream(input_file_path, ifstream::in)) {
		this->input_file_path = input_file_path;

		line_number = 1;
		char_number = 1;

		last_char = '\0';
	}

#define LOCATIONAL_FORMAT "At char %u on line %u, %s"
#define FORMAT_MESSAGE(overarching_format, message_format, result_name) \
	char* __result_name; \
	asprintf(&__result_name, overarching_format, message_format.c_str()); \
	char* result_name; \
	do { \
		va_list va_args_name; \
		va_start(va_args_name, message_format); \
		vasprintf(&result_name, message_format.c_str(), va_args_name); \
		va_end(va_args_name); \
	} while (0)

	void err(unsigned int exit_status, string message_format...) {
		FORMAT_MESSAGE("\x1B[1;31mERROR: %s\x1B[0m\n", message_format, message);
		cout << message_header() << message;
		exit(exit_status);
	}

	void warn(string message_format...) {
		FORMAT_MESSAGE("\x1B[1;33mWARNING: %s\x1B[0m\n", message_format, message);
		cout << message_header() << message;
	}

	void debug(string message_format...) {
		FORMAT_MESSAGE("%s\n", message_format, message);
		cout << message_header() << message;
	}

	void verbose(string message_format...) {
		FORMAT_MESSAGE("\t%s\n", message_format, message);
		cout << message_header() << message;
	}

	void debugLocation() {
		verbose("current location: %d:%d", line_number, char_number);
	}
#undef FORMAT_STRING

	// TODO: in parse() and parsing utility methods, make parameters for error codes and messages to spit out if the parsing was unsuccessful
	bool parse(string to_parse) {
		streampos old_position = input_file.tellg();

		// try to parse the string given
		const char* current_char = to_parse.c_str();
		for (unsigned int i = 0; i < to_parse.length(); i++, current_char++) {
			if (*current_char != (*this)++) {
				// reset the parser to its old location before attempting this parse
				input_file.seekg(old_position, ifstream::beg);
				return false;
			}
		}

		return true;
	}

	tuple<bool, long int> parseInt() {
		long int result = 0;
		streampos start_position = input_file.tellg();

		// parse the optional positive or negative sign
		bool negative = false;
		if (peek() == '-') {
			(*this)++;
			negative = true;
		} else if (peek() == '+')
			(*this)++;

		// if there are no digits to parse at all, throw an error
		if (peek() < '0' || peek() > '9') {
			// reset the parser to its original position
			input_file.seekg(start_position, ifstream::beg);
			return std::make_tuple(false, -1);
		}

		while (peek() >= '0' && peek() <= '9') {
			unsigned char new_digit = peek() - 0x30;
			// if adding the new digit would cause the long int to overflow, throw an error
			if ((LONG_MAX - new_digit) / 10 < new_digit)
				err(NUMBER_TOO_LARGE, "I attempted to parse a number too big to fit into a long int!");
			else
				result = result * 10 + new_digit;
		}

		if (negative)
			return std::make_tuple(true, -result);
		else
			return std::make_tuple(true, -result);
	}

	char operator++(int) /* postfix */{
		char old_last_char = last_char;
		last_char = input_file.get();

		if (last_char == '\n') {
			line_lengths.push(char_number - 1);
			line_number++;
			char_number = 1;
		} else {
			char_number++;
		}

		return old_last_char;
	}

	char operator++() /* prefix */{
		(*this)++;
		return *(*this);
	}

	char operator--(int) /* postfix */{
		char old_last_char = last_char;
		last_char = input_file.unget().unget().get();

		if (last_char == '\n') {
			line_number--;
			char_number = line_lengths.top();
			line_lengths.pop();
		} else {
			char_number--;
		}

		return old_last_char;
	}

	char operator--() /* prefix */{
		(*this)--;
		return last_char;
	}

	char operator*() {
		return last_char;
	}

	char peek() {
		return input_file.peek();
	}
};

std::vector<parser> parsers = *new std::vector<parser>();

#endif /* PARSER_H_ */

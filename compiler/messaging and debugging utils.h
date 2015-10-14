/*
 * messaging and debugging utils.h
 *
 *  Created on: Oct 8, 2015
 *      Author: connor
 */

#ifndef MESSAGING_AND_DEBUGGING_UTILS_H_
#define MESSAGING_AND_DEBUGGING_UTILS_H_

#include <stdlib.h>
#include <stdio.h>

//////////////////////////// MESSAGE UTILS
#define NUMBER_OF_REGULAR_ERROR_TYPES 5
enum error_type {
	INTERNAL = 1, CMD_ARG, NO_SUCH_FILE, UNREADABLE_FILE, IO
};

#define ERROR_FORMAT "\x1B[1;31mERROR: %s\x1B[0m\n"

#define ERR(exit_status, message_format, ...) \
	do { \
		size_t full_message_length = strlen(message_format) + strlen(ERROR_FORMAT) - 2/* "-2" subtracts the length of the "%s" in ERROR_FORMAT */; \
		char* ERR_full_message_format = malloc(full_message_length); \
		sprintf(ERR_full_message_format, ERROR_FORMAT, message_format); \
		fprintf(stderr, ERR_full_message_format, ##__VA_ARGS__); \
		exit(exit_status); \
	} while (0)

#define WARNING_FORMAT "\x1B[1;33mWARNING: %s\x1B[0m\n"

#define WARN(message_format, ...) \
	do { \
		size_t WARN_full_message_length = strlen(message_format) + strlen(WARNING_FORMAT) - 2/* "-2" subtracts the length of the "%s" in WARNING_FORMAT */; \
		char* WARN_full_message_format[WARN_full_message_length]; \
		sprintf(WARN_full_message_format, WARNING_FORMAT, message_format); \
		printf(WARN_full_message_format, ##__VA_ARGS__); \
	} while (0)

#define DEBUG_ON 0x1
#define VERBOSE_DEBUG_ON 0x2

char debug_mode = DEBUG_ON | VERBOSE_DEBUG_ON;

#define DEBUG_FORMAT "%s\n"

#define DEBUG(message_format, ...) \
	do { \
		if (debug_mode) { \
			char* full_message_format = malloc(strlen(message_format) + strlen(DEBUG_FORMAT) - 2 /* "-2" subtracts the length of the "%s" in WARNING_FORMAT */); \
			sprintf(full_message_format, DEBUG_FORMAT, message_format); \
			printf(full_message_format, ##__VA_ARGS__); \
		}\
	} while (0)

#define VERBOSE_DEBUG(message_format, ...) \
	do { \
		if (debug_mode & VERBOSE_DEBUG_ON) \
			DEBUG(message_format, ##__VA_ARGS__); \
	} while (0)

void doNothing() {
	// do nothing!
	// this exists to have something to put after a "case" label in a switch/case if you want to put a variable declaration
}

char* list(char** items_to_list, int length_of_list) {
	char* result;
	switch (length_of_list) {
	case 0:
		return NULL;
	case 1:
		return items_to_list[0];
	case 2:
		doNothing();	// fixes compiler error about case label follwoed by variable declaration
		unsigned int length_of_both_items = strlen(items_to_list[0]) + strlen(items_to_list[1]);

		result = malloc(length_of_both_items + 5 /* for the " and " */);
		sprintf(result, "%s and %s", items_to_list[0], items_to_list[1]);

		return result;
	default /* 3 or more */:
		doNothing();	// fixes compiler error about case label follwoed by variable declaration
		unsigned int length_of_all_items = 0, i;

		for (i = 0; i < length_of_list; i++)
			length_of_all_items += strlen(items_to_list[i]);

		result = malloc(length_of_all_items + (2 * (length_of_list - 1) /* for the ", " after each item except the last one */) + 5 /* for the " and " */);
		strcpy(result, items_to_list[0]);				// add the first item
		for (i = 1; i < length_of_list - 1; i++)		// add the middle items
			sprintf(result, "%s, %s", result, items_to_list[i]);
		sprintf(result, "%s and %s", result, items_to_list[length_of_list - 1]);		// add the last item

		return result;
	}
}


#endif /* MESSAGING_AND_DEBUGGING_UTILS_H_ */

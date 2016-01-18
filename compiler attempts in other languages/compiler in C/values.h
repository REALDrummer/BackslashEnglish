/*
 * values.h
 *
 *  Created on: Dec 15, 2015
 *      Author: connor
 */

#ifndef COMPILER_IN_C_VALUES_H_
#define VALUES_H_

#define NUMBER_OF_VALUE_TYPES 3
typedef enum value_type {
	REGISTER, LABEL, LITERAL
} value_type;

typedef struct asm_label {

} asm_label;

#endif /* COMPILER_IN_C_VALUES_H_ */

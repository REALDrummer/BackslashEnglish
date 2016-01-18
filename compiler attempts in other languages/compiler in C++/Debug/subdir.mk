################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../compiler.cpp 

OBJS += \
./compiler.o 

CPP_DEPS += \
./compiler.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++-4.9 -D__cplusplus=201303L -O0 -g3 -Wall -fmessage-length=0 -std=c++14 -Wno-parentheses -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '



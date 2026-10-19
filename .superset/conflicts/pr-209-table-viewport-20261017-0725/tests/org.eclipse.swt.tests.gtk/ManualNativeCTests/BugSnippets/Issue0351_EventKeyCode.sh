#!/bin/bash

# Install dependencies:
# sudo dnf install gcc-c++ gtk3-devel

SRC_PATH="Issue0351_EventKeyCode.cpp"
BIN_PATH="Issue0351_EventKeyCode"

# Compile
if [ "$SRC_PATH" -nt "$BIN_PATH" ]; then
	g++ -Wall -g "$SRC_PATH" -o "$BIN_PATH" `pkg-config --cflags gtk+-3.0` `pkg-config --libs gtk+-3.0`
	if [ $? -ne 0 ]; then
		echo "Compilation failed"
		exit 1
	fi
else
	echo "Compilation skipped: binary is up to date"
fi

"./$BIN_PATH"

#!/bin/bash
SRC_PATH="Issue0351_EventKeyCode.mm"
BIN_PATH="Issue0351_EventKeyCode"

# Compile
if [ "$SRC_PATH" -nt "$BIN_PATH" ]; then
	clang++ -std=c++11 -Wno-deprecated-declarations --debug -framework Cocoa -framework Carbon "$SRC_PATH" -o "$BIN_PATH"
	if [ $? -ne 0 ]; then
		echo "Compilation failed"
		exit 1
	fi
else
	echo "Compilation skipped: binary is up to date"
fi

"./$BIN_PATH"

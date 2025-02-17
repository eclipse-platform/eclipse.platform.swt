#!/bin/bash
###############################################################################
# Copyright (c) 2020, 2025 Kichwa Coders Canada Inc and others.
#
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
###############################################################################

set -eu
set -o pipefail

SCRIPT=$( basename "${BASH_SOURCE[0]}" )

###
# Check that executable/so ${FILE}
# use glibc symbols no greater than ${ALLOWED_GLIBC_VERSION} and depend on
# no libs other than ${ALLOWED_LIBS}
FILE=$1; shift
ALLOWED_GLIBC_VERSION=$1; shift
ALLOWED_LIBS="$@"; shift

# grep that doesn't exit 1 on 0 matches
mygrep() { grep "$@" || test $? = 1; }

# Check for permitted libraries using `readelf -d` looking for shared
# libraries that are listed as needed. e.g. lines like:
#  0x0000000000000001 (NEEDED)             Shared library: [libpthread.so.0]
readelf -d ${FILE} | mygrep -E '\(NEEDED\)' | while read needed; do
    needed=${needed//*Shared library: [/}
    needed=${needed//]*/}
    if [[ ! " ${ALLOWED_LIBS} " =~ " ${needed} " ]]; then
        echo "ERROR: $FILE has illegal dependency of ${needed}"
        exit 1
    fi
done

# The way the version check is done is that all symbol version info is extracted
# from relocations match @GLIBC_*, the versions are sorted with the max
# allowed version added to the list too. And then we check the last entry in
# the list to make sure it is == to max allowed version.
objdump -R ${FILE} | mygrep @GLIBC_ | while read version; do
    echo ${version//*@GLIBC_}
done > /tmp/version_check
echo ${ALLOWED_GLIBC_VERSION} >> /tmp/version_check
max_version_in_use=$(cat /tmp/version_check | sort --unique --version-sort | tail -n1)
if [ "$max_version_in_use" != "$ALLOWED_GLIBC_VERSION" ]; then
    echo "ERROR: $FILE has dependency on glibc greater than allowed version of ${ALLOWED_GLIBC_VERSION} for at least the following symbols"
    # This only lists greatest version number symbols
    objdump -R ${FILE} | grep @GLIBC_${max_version_in_use}
    exit 1
fi

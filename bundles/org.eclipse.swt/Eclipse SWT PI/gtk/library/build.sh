#!/bin/sh

#**********************************************************************
# Copyright (c) 2000, 2002 IBM Corp. All rights reserved.
# The contents of this file are made available under the terms
# of the GNU Lesser General Public License (LGPL) Version 2.1 that
# accompanies this distribution (lgpl-v21.txt).  The LGPL is also
# available at http://www.gnu.org/licenses/lgpl.html.  If the version
# of the LGPL at http://www.gnu.org is different to the version of
# the LGPL accompanying this distribution and there is any conflict
# between the two license versions, the terms of the LGPL accompanying
# this distribution shall govern.
#
# Contributors: 
#    Kevin Cornell (Rational Software Corporation)
#    Tom Tromey    (Red Hat, Inc.)
#**********************************************************************

# Some UNIX/Linux compilers don't like <CR>'s in files (DOS format).
fixup_files=`grep -l "\
" *.[ch]`
if test -n "$fixup_files"; then
    echo "Converting files from DOS to UNIX format:"
    for file in $fixup_files; do
	echo "    $file"
	ex $file << EOF 2> /dev/null
g/\$/s///
w
EOF
    done
fi

# Determine the operating system being built

make -f make_gtk.mak ${1+"$@"}

#*******************************************************************************
# Copyright (c) 2000, 2003 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials 
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#     Tom Tromey (Red Hat, Inc.)
#*******************************************************************************

#!/bin/sh

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

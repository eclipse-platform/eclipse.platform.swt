#*******************************************************************************
# Copyright (c) 2000, 2003 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials 
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

#!/bin/csh

# Some UNIX/Linux compilers don't like <CR>'s in files (DOS format).
set fixup_files = `grep -l "\" *.[ch]`
if ( "$fixup_files" != "" ) then
    echo "Converting files from DOS to UNIX format:"
    foreach file ($fixup_files)
	echo "    $file"
	ex $file << EOF >& /dev/null
g/\$/s///
w
EOF
    end
endif

# Determine the operating system being built
set OS=`uname -s`
switch ($OS )
	case AIX:
        if ( "$1" == "clean" ) then
            make -f make_aix.mak clean
        else
            echo "Building AIX version of SWT OpenGL library."
            make -f make_aix.mak make_swt
        endif
        breaksw

    case Linux:
		if ( "$1" == "clean" ) then
		    make -f make_linux.mak clean
		else
		    echo "Building Linux version of SWT OpenGL library."
		    make -f make_linux.mak make_swt
		endif
		breaksw
	
    case SunOS:
        if ( "$1" == "clean" ) then
            make -f make_solaris.mak clean
        else
            echo "Building Solaris version of SWT OpenGL library."
            make -f make_solaris.mak make_swt
        endif
        breaksw

    case HP-UX:
        if ( "$1" == "clean" ) then
            make -f make_hpux.mak clean
        else
            echo "Building HP-UX version of SWT OpenGL library."
            make -f make_hpux.mak make_swt
        endif
        breaksw

    default:
       echo "Unknown OS -- build aborted"
       breaksw
endsw

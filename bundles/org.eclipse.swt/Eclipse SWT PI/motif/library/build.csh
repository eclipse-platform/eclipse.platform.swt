#!/bin/csh

#**********************************************************************
# Copyright (c) 2000, 2002 IBM Corp. and others.  All rights reserved.
# This file is made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
#   
# Contributors: 
#     Kevin Cornell (Rational Software Corporation)
#********************************************************************** 

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
            echo "Building AIX version of SWT and CDE DLLs."
            make -f make_aix.mak make_swt
            make -f make_aix.mak make_cde
        endif
        breaksw

    case Linux:
		if ( "$1" == "clean" ) then
		    make -f make_linux.mak clean
		else
		    echo "Building Linux version of SWT and GNOME DLLs."
		    make -f make_linux.mak make_swt make_gnome
		    
		    set build_kde = `rpm -q kdebase | grep "not installed"`
		    if ( "$build_kde" == "" ) then
		    	echo "Building Linux version of KDE DLL."
		    	make -f make_linux.mak make_kde
		    endif
		endif
		breaksw
	
    case SunOS:
        if ( "$1" == "clean" ) then
            make -f make_solaris.mak clean
        else
            echo "Building Solaris version of SWT and CDE DLLs."
            make -f make_solaris.mak make_swt
            make -f make_solaris.mak make_cde
        endif
        breaksw

    case HP-UX:
        if ( "$1" == "clean" ) then
            make -f make_hpux.mak clean
        else
            echo "Building HP-UX version of SWT and CDE DLLs."
            make -f make_hpux.mak make_swt
            make -f make_hpux.mak make_cde
        endif
        breaksw

    default:
       echo "Unknown OS -- build aborted"
       breaksw
endsw

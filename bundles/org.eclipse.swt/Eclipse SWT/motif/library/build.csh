#!/bin/csh

# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.

#    This command script can be invoked with the "clean" argument.

# The major and minor version numbers and the build numbers are returned by the
# getSharedLibraryMajorVersionNumber and getSharedLibraryMinorVersionNumber
# calls so java can query the swt shared library version number

setenv MAJOR_VER  1
setenv MINOR_VER  133
setenv BUILD_NUM  0

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
            echo "Building AIX version of SWT DLL."
            make -f make_aix.mak make_swt
        endif
        breaksw

    case Linux:
		if ( "$1" == "clean" ) then
		    make -f make_linux.mak clean
		else
		    echo "Building Linux version of SWT and GNOME DLLs."
		    make -f make_linux.mak make_swt make_gnome
		    
		    set build_kde = `rpm -q kdebase | grep "not installed"`
		    set build_kde = "not ported" # KDE porting not complete yet (KJC)
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
        
    default:
       echo "Unknown OS -- build aborted"
       breaksw
endsw

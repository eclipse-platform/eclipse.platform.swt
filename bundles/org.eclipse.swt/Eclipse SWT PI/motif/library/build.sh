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
OS=`uname -s`
case $OS in

    "AIX")
        if  [ "$1" = "clean" ]; then
            make -f make_aix.mak clean
        else
            echo "Building AIX version of SWT and CDE DLLs."
            make -f make_aix.mak make_swt
            make -f make_aix.mak make_cde
        fi
        ;;

    "Linux")
        if  [ "$1" = "clean" ]; then
            make -f make_linux.mak clean
        else
            echo "Building Linux version of SWT and GNOME DLLs."
            make -f make_linux.mak make_swt make_gnome

            build_kde=`rpm -q kdebase | grep "not installed"`
            if [ "$build_kde" = "" ]; then
                echo "Building Linux version of KDE DLL."
                make -f make_linux.mak make_kde
            fi
        fi
        ;;

    "SunOS")
        if  [ "$1" = "clean" ]; then
            make -f make_solaris.mak clean
        else
            echo "Building Solaris version of SWT and CDE DLLs."
            make -f make_solaris.mak make_swt
            make -f make_solaris.mak make_cde
        fi
        ;;

        "HP-UX")
	if  [ "$1" = "clean" ]; then
            make -f make_hpux.mak clean
        else
            echo "Building HP-UX version of SWT and CDE DLLs."
            make -f make_hpux.mak make_swt
            make -f make_hpux.mak make_cde
        fi
        ;;

    *)
        echo "Unknown OS -- build aborted"
        ;;
esac

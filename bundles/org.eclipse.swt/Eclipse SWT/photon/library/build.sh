 #!/bin/sh
# (c) Copyright IBM Corp 1999, 2000
#

export IVE_HOME=~/ive/bin
export PATH=$IVE_HOME:$PATH

# TEMPORARY CODE
make -f makefile.mak clean
make -f makefile.mak

cp *.so $IVE_HOME

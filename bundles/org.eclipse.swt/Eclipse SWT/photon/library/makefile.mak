# Makefile for module 'swt'

# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.

maj_ver=2
min_ver=012
revision=0

#assumes IVE_HOME is set in the environment

DLLPREFIX=swt
OS_PREFIX=qnx
DLLNAME=lib$(DLLPREFIX)-$(OS_PREFIX)-$(maj_ver)$(min_ver).so
#DLLNAME=lib$(DLLPREFIX)$(maj_ver)$(min_ver)r$(revision).so

DEBUG =  
CFLAGS = -c -shared -DSWT_LIBRARY_VERSION=$(maj_ver)$(min_ver) -w8 $(DEBUG) -DPHOTON -I$(IVE_HOME)/include
LFLAGS = -shared -lph -lphrender

SWTOBJS = swt.o structs.o callback.o globals.o library.o

all: $(DLLNAME)

.c.o:
	cc $(CFLAGS) $*.c

$(DLLNAME): $(SWTOBJS)
	cc -o $(DLLNAME)  $(LFLAGS) $(SWTOBJS)

clean:
	rm -f  $(DLLNAME) *.o
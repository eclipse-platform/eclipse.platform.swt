# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.
#
# Makefile for SWT libraries on Photon

include make_common.mak

#assumes IVE_HOME is set in the environment

SWT_PREFIX=swt
OS_PREFIX=qnx
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(OS_PREFIX)-$(SWT_VERSION).so

DEBUG =  
CFLAGS = -c -shared -DSWT_VERSION=$(SWT_VERSION) -w8 $(DEBUG) -DPHOTON -I$(IVE_HOME)/include
LFLAGS = -shared -lph -lphrender

SWT_OBJS = swt.o structs.o callback.o

all: $(SWT_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJS)
	cc -o $(SWT_LIB)  $(LFLAGS) $(SWT_OBJS)

clean:
	rm -f *.so *.o
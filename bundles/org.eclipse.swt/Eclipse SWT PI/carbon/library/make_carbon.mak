
CLASSPATH= ../../../bin
OSCLASS= $(CLASSPATH)/org/eclipse/swt/internal/carbon/OS.class
OSHEADER= org_eclipse_swt_internal_carbon_OS.h
JNI= /System/Library/Frameworks/JavaVM.framework/Headers
LIBNAME= swt-carbon-2040
LIB= lib$(LIBNAME).jnilib


$(LIB):	OS.o
	cc -bundle -o lib$(LIBNAME).jnilib -framework JavaVM -framework Carbon OS.o

OS.o: OS.c $(OSHEADER)
	cc -I$(JNI) -c OS.c

$(OSHEADER): $(OSCLASS) 
	javah -classpath $(CLASSPATH) -force -d . org.eclipse.swt.internal.carbon.OS

$(OSCLASS): ../org/eclipse/swt/internal/carbon/OS.java
	javac ../org/eclipse/swt/internal/carbon/OS.java


# Notes:
# - Currently 'install' cannot be ran on it's own, only part of a build.
# - At time of writing, this makefile is not used by any build server. 
#   Build servers call build.sh directly.

all:
	./build.sh -gtk-all install

gtk4:
	./build.sh -gtk4 install

gtk4-build-only:
	./build.sh -gtk4

gtk3:
	./build.sh -gtk3 install

clean:
	./build.sh clean

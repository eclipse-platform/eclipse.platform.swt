#!/bin/bash

# This is a wrapper script that you can put into your path and give it a short name: e.g: ~/bin/swtb
# It will make a local copy of the rebuild_swt_natives script (if the one from the repo is newer)
# and execute it.

# The idea is that if you checkout an old SWT codebase that didn't have rebuild_swt_natives,
# you could still rebuild swt bindings by running the local copy via this script.

# Instructions:
# Move this script:  
#    cp ./rebuild_swt_natives_wrapper.sh ~/bin/swtb
# Run it at least once:
#    ~/bin/swtb
# Re-run it when needed via 'swtb'

REBUILD_SCRIPT="$HOME/git/eclipse.platform.swt/bundles/org.eclipse.swt.tools/gtk/rebuild_swt_natives.sh"

if [ -e "${REBUILD_SCRIPT}" ];  then 
	# make a local copy so it's available if you checkout an old SWT code base.
	TEMP_COPY="./rebuild_swt_natives.sh_temp"
	if [ ! -e "$TEMP_COPY" ]; then
		echo "Making local copy of rebuild_swt_natives, so that it can be used by older swt checkouts"
		cp "$REBUILD_SCRIPT" "$TEMP_COPY"
	fi

 	# Update local copy if it's out of date.	
	if [ "$REBUILD_SCRIPT" -nt "$TEMP_COPY" ]; then
		echo "Found new version of script. Updating local copy"
		cp "$REBUILD_SCRIPT" "$TEMP_COPY"
	fi
	$REBUILD_SCRIPT
else
	if [ -e "$TEMP_COPY" ]; then
		echo "Original not available. Running local copy"
		$TEMP_COPY
	else	
		echo "ERROR: could not find rebuld script for initial run: $REBUILD_SCRIPT and no local copy available either"
		exit 1; #fail
	fi
fi


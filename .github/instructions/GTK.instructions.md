---
applyTo: "bundles/org.eclipse.swt/Eclipse SWT PI/gtk/library/*.c,bundles/org.eclipse.swt/Eclipse SWT PI/gtk/library/*.h"
---

- Due to the copilot environment setup in copilot-setup-steps.yml we always use x86_64 architecture
- Always carefully investigate exsiting GTK functions the gtk-dev files are installed in the system
- The GTK docs can be found here https://www.gtk.org/docs/
- Be carefull between the difference of GTK3 and GTK4 we need to check for specific versions in some places already
- The GTK3 > GTK4 migration guide can be found here https://docs.gtk.org/gtk4/migrating-3to4.html
- You will find a shell script ./build_gtk.sh that must be used to compile the code for testing changes

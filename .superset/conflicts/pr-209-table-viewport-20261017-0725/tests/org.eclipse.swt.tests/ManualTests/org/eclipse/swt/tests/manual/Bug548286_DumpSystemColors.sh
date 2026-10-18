JAVA_ARGS=
JAVA_CP=

if [ -z "$JAVA_CP" ]; then
  echo 'ERROR: You need to specify $JAVA_CP and maybe $JAVA_ARGS'
  exit 1
fi

function TestTheme() {
  echo
  echo $1
  echo -----------
  GTK_THEME=$1 java $JAVA_ARGS -classpath $JAVA_CP org.eclipse.swt.tests.manual.Bug548286_DumpSystemColors
}

TestTheme Adwaita
TestTheme Adwaita-dark
TestTheme Ambiance
TestTheme Clearlooks-Phenix
TestTheme HighContrast
TestTheme Radiance
TestTheme Raleigh
TestTheme Yaru
TestTheme Yaru-dark



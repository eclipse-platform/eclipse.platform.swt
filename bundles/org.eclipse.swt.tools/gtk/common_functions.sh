# Utility functions
func_echo_info () {
	GREEN='\033[0;32m'
	NC='\033[0m' # No Color
	echo -e "${GREEN}${@}${NC}"
}

func_echo_input () {
	PURPLE='\033[0;35m'
	NC='\033[0m' # No Color
	echo -e "${PURPLE}${@}${NC}"
}

func_echo_error () {
	RED='\033[0;31m'
	NC='\033[0m' # No Color
echo -e "${RED}*** ${@}${NC}"
}

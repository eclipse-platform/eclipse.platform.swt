
#include <Carbon.r>

resource 'cicn' (128) {
	4,
	{0, 0, 12, 16},
	2,
	$"FFFC FFFC FFFC FFFF FFFF FFFF FFFF FFFF"
	$"1FFF 1FFF 1FFF 0000",
	$"FFFC FFFC FFFC FFFF F001 F001 F001 F001"
	$"1001 1001 1FFF 0000",
	{	/* array ColorSpec: 4 elements */
		/* [1] */
		65535, 65535, 65535,
		/* [2] */
		43690, 43690, 43690,
		/* [3] */
		21845, 21845, 21845,
		/* [4] */
		0, 0, 0
	},
	$"FFFF FFF0 EAAA AAB0 EAAA AAB0 EBFF FFFF"
	$"EB55 5557 EB55 5557 EB55 5557 FF55 5557"
	$"0355 5557 0355 5557 03FF FFFF 0000 0000"
};

resource 'MENU' (134) {
	134,
	63,
	allEnabled,
	enabled,
	"Title",
	{	/* array: 3 elements */
		/* [1] */
		"Text Title", noIcon, noKey, noMark, plain,
		/* [2] */
		"Check Box Title", noIcon, noKey, noMark, plain,
		/* [3] */
		"Popup Button Title", noIcon, noKey, noMark, plain
	}
};

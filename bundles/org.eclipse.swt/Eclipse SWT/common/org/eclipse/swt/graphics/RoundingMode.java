package org.eclipse.swt.graphics;
/**
* @noreference This class is not intended to be referenced by clients
*/
public enum RoundingMode {
 ROUND, UP;

public int round(float x) {
	if (this == ROUND) {
		return Math.round(x);
	}
	if (this == UP) {
		return (int) Math.ceil(x);
	}
	return (int) x;
}
}

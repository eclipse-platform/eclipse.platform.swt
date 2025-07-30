package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;

public class StyleProcessor {
	private ArrayList<String> oneOfArr = new ArrayList<>();
	private ArrayList<ArrayList<String>> ifOneArr = new ArrayList<>();
	private ArrayList<ArrayList<String>> thenOneArr = new ArrayList<>();


	// Just add styles you want to detect — no validation
	public StyleProcessor oneOf(String styles) {
		String[] tempHolder = styles.split(", ");

		oneOfArr.addAll(Arrays.asList(tempHolder));

		return this;
	}

	public StyleProcessor ifOneOf(String styles) {
		String[] tempHolder = styles.split(", ");
		ArrayList<String> tempList = new ArrayList<>(Arrays.asList(tempHolder));

		ifOneArr.add(tempList);

		return this;
	}

	public StyleProcessor thenOneOf(String styles) {
		if (ifOneArr.isEmpty()) {
			throw new IllegalStateException("No 'ifOneOf' condition defined before 'thenOneOf'");
		}

		String[] tempHolder = styles.split(", ");
		ArrayList<String> tempList = new ArrayList<>(Arrays.asList(tempHolder));

		thenOneArr.add(tempList);

		return this;
	}

//	 Given a style bitmask, return or print the styles that are set
	public ArrayList<String> process(int style) {
		System.out.println("Int Style" + style);
		ArrayList<String> finalList = new ArrayList<>();

		for (String s: oneOfArr) {
			try {
				if ((style & SWT.class.getField(s).getInt(null)) != 0) {
					finalList.add(s);
					break;
				}
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace(); // or handle gracefully
			}
		}

		for (int i = 0; i < ifOneArr.size(); i++) {

			for (String s2: ifOneArr.get(i)) {
				try {
					if ((style & SWT.class.getField(s2).getInt(null)) != 0) {
						for (String s3: thenOneArr.get(i)) {
							if ((style & SWT.class.getField(s3).getInt(null)) != 0) {
								finalList.add(s3);
								break;
							}
						}
						break;
					}
				} catch (NoSuchFieldException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
		
		
		return finalList;

	}

}

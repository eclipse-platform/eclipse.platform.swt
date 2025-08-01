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


	public ArrayList<String> process(int style) {
//		System.out.println("Int Style" + style);
		ArrayList<String> finalList = new ArrayList<>();

		for (String s: oneOfArr) {
			try {
				if ((style & SWT.class.getField(s).getInt(null)) != 0) {
					finalList.add(s);
					break;
				}
			} catch (NoSuchFieldException | IllegalAccessException e) {
				System.out.println("Invalid Style: " + s);
				e.printStackTrace(); // or handle gracefully
			}
		}

		for (int i = 0; i < ifOneArr.size(); i++) {

			for (String s2: ifOneArr.get(i)) {
				try {
					//Made it into a variable to make the it easy to read
					int flag = SWT.class.getField(s2).getInt(null);
					
					if ((style & flag)  != 0 ) {
						//Checks if the s2 (String) is inside the oneOfArr 
						if (oneOfArr.contains(s2)) {
						
							for (String s3: thenOneArr.get(i)) {
								if ((style & SWT.class.getField(s3).getInt(null)) != 0) {
									finalList.add(s3);
									break;
								}
							}
							break;
						} else {
							System.out.println("Not inside the oneArr: " + s2);
						}
					}
				} catch (NoSuchFieldException | IllegalAccessException e) {
					System.out.println("Invalid Style: " + s2);
					e.printStackTrace();
				}
			}

		}
		
		
		return finalList;

	}

}

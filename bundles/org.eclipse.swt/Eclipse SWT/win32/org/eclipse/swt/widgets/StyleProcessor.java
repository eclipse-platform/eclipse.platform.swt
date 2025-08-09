package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;

public class StyleProcessor {


	private static class Rule {
	    ArrayList<String> ifOneOf;
	    ArrayList<String> thenOneOf;
	    ArrayList<String> thenSomeOf;

	    Rule(ArrayList<String> ifOneOf) {
	        this.ifOneOf = ifOneOf;
	        this.thenOneOf = new ArrayList<>();
	        this.thenSomeOf = new ArrayList<>();
	    }
	}

	private ArrayList<Rule> rules = new ArrayList<>();
	private ArrayList<ArrayList<String>> oneOfArr = new ArrayList<>();
	private ArrayList<String> someOfArr = new ArrayList<>();
	private Rule currentRule = null;

	public StyleProcessor oneOf(String styles) {
		String[] tempHolder = styles.split(", ");

		oneOfArr.add(new ArrayList<>(Arrays.asList(tempHolder)));

		return this;
	}

	public StyleProcessor someOf(String styles) {
		String[] tempHolder = styles.split(", ");

		someOfArr.addAll(Arrays.asList(tempHolder));

		return this;
	}

	public StyleProcessor ifOneOf(String styles) {
	    String[] tempHolder = styles.split(", ");
	    ArrayList<String> tempList = new ArrayList<>(Arrays.asList(tempHolder));
	    currentRule = new Rule(tempList);
	    rules.add(currentRule);
	    return this;
	}


	public StyleProcessor thenOneOf(String styles) {
	    if (currentRule == null) {
	        throw new IllegalStateException("No 'ifOneOf' defined before 'thenOneOf'");
	    }
	    currentRule.thenOneOf.addAll(Arrays.asList(styles.split(", ")));
	    return this;
	}


	public StyleProcessor thenSomeOf(String styles) {
	    if (currentRule == null) {
	        throw new IllegalStateException("No 'ifOneOf' defined before 'thenSomeOf'");
	    }
	    currentRule.thenSomeOf.addAll(Arrays.asList(styles.split(", ")));
	    return this;
	}



	public ArrayList<String> process(int style) {
	    ArrayList<String> finalList = new ArrayList<>();
	    for (ArrayList<String> a: oneOfArr) {
		    for (String s: a) {
		        try {
		            if ((style & SWT.class.getField(s).getInt(null)) != 0) {
		                finalList.add(s);
		                break;
		            }
		        } catch (NoSuchFieldException | IllegalAccessException e) {
		            System.out.println("Invalid Style: " + s);
		        }
		    }
	    }

	    for (String s : someOfArr) {
	        try {
	            if ((style & SWT.class.getField(s).getInt(null)) != 0) {
	                finalList.add(s);
	            }
	        } catch (NoSuchFieldException | IllegalAccessException e) {
	            System.out.println("Invalid Style: " + s);
	        }
	    }

	    for (Rule rule : rules) {
	        boolean matched = false;

	        for (String condition : rule.ifOneOf) {
	            try {
	                int flag = SWT.class.getField(condition).getInt(null);
	                if ((style & flag) != 0) {
	                    matched = true;
	                    break;
	                }
	            } catch (NoSuchFieldException | IllegalAccessException e) {
	                System.out.println("Invalid condition: " + condition);
	            }
	        }

	        if (matched) {
	            for (String s : rule.thenOneOf) {
	                try {
	                    int flag = SWT.class.getField(s).getInt(null);
	                    if ((style & flag) != 0) {
	                        finalList.add(s);
	                        break;
	                    }
	                } catch (NoSuchFieldException | IllegalAccessException e) {
	                    System.out.println("Invalid thenOneOf: " + s);
	                }
	            }

	            for (String s : rule.thenSomeOf) {
	                try {
	                    int flag = SWT.class.getField(s).getInt(null);
	                    if ((style & flag) != 0) {
	                        finalList.add(s);
	                    }
	                } catch (NoSuchFieldException | IllegalAccessException e) {
	                    System.out.println("Invalid thenSomeOf: " + s);;
	                }
	            }
	        }
	    }

	    return finalList;
	}


}

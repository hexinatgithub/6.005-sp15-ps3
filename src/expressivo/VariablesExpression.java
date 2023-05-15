package expressivo;

public class VariablesExpression implements Expression {
	/**
	 * Abstraction function:
	 * 	AF(var) = variable name
	 * Rep invariant:
	 * 	var contain case-sensitive nonempty sequences of letters
	 * Safety from rep exposure:
	 * 	all fields are private, final and immutable
	 */
	
	private final String name;
	
	public VariablesExpression(String v) throws IllegalArgumentException {
		if (!v.matches("[a-zA-Z]+")) {
			throw new IllegalArgumentException("invalid variable name: " + v);
		}
		this.name = v;
		checkRep();
	}
	
	private void checkRep() {
		assert name.matches("[a-zA-Z]+");
	}
	
	public boolean isPrimitive() {
		return true;
	}
	
	@Override public String toString() {
		return name;
	}
	
	@Override public boolean equals(Object thatObject) {
		if (!(thatObject instanceof VariablesExpression)) {
			return false;
		}
		VariablesExpression that = (VariablesExpression) thatObject;
		return name.equals(that.name);
	}
	
	@Override public int hashCode() {
		return name.hashCode();
	}
}
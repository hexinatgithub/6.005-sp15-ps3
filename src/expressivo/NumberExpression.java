package expressivo;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout.Group;

import org.hamcrest.core.IsInstanceOf;

public class NumberExpression implements Expression {
	/**
	 * Abstraction function:
	 * 	AF(decimal) = number with at least 4 decimal places
	 * Rep invariant:
	 * 	decimal represent a number with no leading and trailing zero
	 * Safety from rep exposure:
	 * 	all fields are private, final and immutable
	 */
	
	private final BigDecimal decimal;
	
	public NumberExpression(String value) throws IllegalArgumentException {
		Pattern regex = Pattern.compile("^0*(?<decimal>\\d+(\\.\\d{1,4})?)\\d*$");
		Matcher m = regex.matcher(value);
		if (!m.matches()) {
			throw new IllegalArgumentException("can't decode into a number: " + value);
		}
		
		decimal = new BigDecimal(m.group("decimal")).stripTrailingZeros();
		checkRep();
	}
	
	private void checkRep() {}
	
	public boolean isPrimitive() {
		return true;
	}
	
	@Override public String toString() {
		return decimal.toPlainString();
	}
	
	@Override public boolean equals(Object thatObject) {
		if (!(thatObject instanceof NumberExpression)) {
			return false;
		}
		NumberExpression that = (NumberExpression) thatObject;
		return decimal.equals(that.decimal);
	}
	
	@Override public int hashCode() {
		return decimal.hashCode();
	}
}
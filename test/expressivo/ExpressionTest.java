/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    //   toString(), equals(), hashCode():
	//		partition on recursive deep level:
	//			none
	//			left child, right child
	//			left child of the left child, right child of the left child
	//			left child of the right child, right child of the right child
	//			more than two level deep
	//		partition on white space:
	//			contain white space before and after operation
	//			no white space
	//		partition on parentheses nest:
	//			none
	//			one deep level left group, one deep level right group
	//			two deep level left group, two deep right group
	//			one deep level left group and two deep level right group
	//			one deep level right group and two deep level right group
	//			more than two level deep group
	//		partition on producer:
	//			add(), multiplication(), parse()
	//		order of operations:
	//			multiplication over addition
	//		partition on case sensitive
	//	equals(), hashCode():
	//		partition on right to left structurally order
	//	toString():
	//		partition on decimal places: 0, 1, 2-4, 5, >5
	//	 
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // TODO tests for Expression
    
    /**
     * cover none
     * 	decimal places 0, 1, 2-4, 5, >5
     */
    @Test
    public void testParseNumber() {
    	// decimal places 0, 1, 2-4, 5, >5
    	Expression expression = Expression.parse("100");
    	assertEquals("expected number expression", "100", expression.toString());
    	assertTrue("expected equals", Expression.parse("100").equals(expression));
    	assertEquals("expected hash code equal", 
    			Expression.parse("100").hashCode(), expression.hashCode());
    	
    	expression = Expression.parse("4.5");
    	assertEquals("expected number expression", "4.5", expression.toString());
    	assertTrue("expected equals", Expression.parse("4.5").equals(expression));
    	assertEquals("expected hash code equal", 
    			Expression.parse("4.5").hashCode(), expression.hashCode());
    	
    	expression = Expression.parse("4.56");
    	assertEquals("expected number expression", "4.56", expression.toString());
    	assertTrue("expected equals", Expression.parse("4.56").equals(expression));
    	assertEquals("expected hash code equal", 
    			Expression.parse("4.56").hashCode(), expression.hashCode());
    	
    	expression = Expression.parse("4.56789");
    	assertEquals("expected number expression", "4.5678", expression.toString());
    	assertTrue("expected equals", Expression.parse("4.56781").equals(expression));
    	assertEquals("expected hash code equal", 
    			Expression.parse("4.56781").hashCode(), expression.hashCode());
    	
    	expression = Expression.parse("4.567891");
    	assertEquals("expected number expression", "4.5678", expression.toString());
    	assertTrue("expected equals", Expression.parse("4.567856").equals(expression));
    	assertEquals("expected hash code equal", 
    			Expression.parse("4.567856").hashCode(), expression.hashCode());
    }
    
    /**
     * cover case sensitive
     */
    @Test
    public void testCaseSensitive() {
    	Expression exp1 = Expression.parse("x");
    	assertEquals("expected expression", "x", exp1.toString());
    	Expression exp2 = Expression.parse("X");
    	assertEquals("expected expression", "X", exp2.toString());
    	assertFalse("expected expression not equal", exp1.equals(exp2));
    }
    
    /**
     * cover no deep
     */
    @Test
    public void testNoDeep() {
    	Expression exp1 = Expression.parse("x+y");
    	assertEquals("expected expression", "x+y", exp1.toString());
    	
    	Expression exp2 = Expression.add(Expression.parse("x"), Expression.parse("y"));
    	assertEquals("expected expression", "x+y", exp2.toString());
    	assertTrue("expected expression equals", exp1.equals(exp2));
    	assertEquals("expected hash code equals", exp1.hashCode(), exp2.hashCode());
    }
    
    /**
     * cover left child
     * 	no white space and contain white space before and after operation
     * 	none and one deep level left group
     * 	multiplication over addition
     */
    @Test
    public void testLeftChild() {
    	Expression exp1 = Expression.parse("x*y+100");
    	assertEquals("expected expression", "(x*y)+100", exp1.toString());
    	
    	Expression exp2 = Expression.add(Expression.parse("x*y"), Expression.parse("100"));
    	assertEquals("expected expression", "(x*y)+100", exp2.toString());
    	assertTrue("expected expression equals", exp1.equals(exp2));
    	assertEquals("expected hash code equals", exp1.hashCode(), exp2.hashCode());
    }
    
    /**
     * cover right child
     * 	one deep level right group
     * 	multiplication over addition
     */
    @Test
    public void testRightChild() {
    	Expression exp1 = Expression.parse("200+x*y");
    	assertEquals("expected expression", "200+(x*y)", exp1.toString());
    	
    	Expression exp2 = Expression.add(Expression.parse("200"), Expression.parse("x*y"));
    	assertEquals("expected expression", "200+(x*y)", exp2.toString());
    	assertTrue("expected expression equals", exp1.equals(exp2));
    	assertEquals("expected hash code equals", exp1.hashCode(), exp2.hashCode());
    }
    
    /**
     * cover left child of the left child
     * 	two deep level left group
     * 	right to left structurally order
     */
    @Test
    public void testTwoDeepLeftGroup() {
    	Expression exp1 = Expression.parse("(x*y)*z+200");
    	assertEquals("expected expression", "((x*y)*z)+200", exp1.toString());
    	
    	Expression exp2 = Expression.add(
    			Expression.multiplication(Expression.parse("x*y"), Expression.parse("z")),
    			Expression.parse("200"));
    	assertEquals("expected expression", "((x*y)*z)+200", exp2.toString());
    	assertTrue("expected expression equals", exp1.equals(exp2));
    	assertEquals("expected hash code equals", exp1.hashCode(), exp2.hashCode());
    	
    	Expression exp3 = Expression.add(
    			Expression.multiplication(Expression.parse("x"), Expression.parse("y*z")), 
    			Expression.parse("200"));
    	assertEquals("expected expression", "(x*(y*z))+200", exp3.toString());
    	assertFalse("expected expression equals", exp3.equals(exp2));
    }
    
    /**
     * cover right child of the left child
     * 	one deep level left group and two deep level right group
     */
    @Test
    public void testOneDeepLeftTwoDeepRigft() {
    	Expression exp1 = Expression.parse("x*y*z+200");
    	assertEquals("expected expression", "(x*(y*z))+200", exp1.toString());
    	
    	Expression exp2 = Expression.add(
    			Expression.multiplication(Expression.parse("x"), Expression.parse("y*z")), 
    			Expression.parse("200"));
    	assertEquals("expected expression", "(x*(y*z))+200", exp2.toString());
    	assertTrue("expected expression equals", exp1.equals(exp2));
    	assertEquals("expected hash code equals", exp1.hashCode(), exp2.hashCode());
    	
    	Expression exp3 = Expression.add(
    			Expression.multiplication(Expression.parse("x*y"), Expression.parse("z")), 
    			Expression.parse("200"));
    	assertEquals("expected expression", "((x*y)*z)+200", exp3.toString());
    	assertFalse("expected expression equals", exp3.equals(exp2));
    }
    
    /**
     * cover left child of the right child
     * 	one deep level right group and two deep level left group
     */
    @Test
    public void testOneDeepRightTwoDeepLeft() {
    	Expression exp1 = Expression.parse("200+(x*y)*z");
    	assertEquals("expected expression", "200+((x*y)*z)", exp1.toString());
    	
    	Expression exp2 = Expression.add(Expression.parse("200"), 
    			Expression.multiplication(Expression.parse("x*y"), Expression.parse("z")));
    	assertEquals("expected expression", "200+((x*y)*z)", exp2.toString());
    	assertTrue("expected expression equals", exp1.equals(exp2));
    	assertEquals("expected hash code equals", exp1.hashCode(), exp2.hashCode());
    	
    	Expression exp3 = Expression.add(Expression.parse("200"), 
    			Expression.multiplication(Expression.parse("x"), Expression.parse("y*z")));
    	assertEquals("expected expression", "200+(x*(y*z))", exp3.toString());
    	assertFalse("expected expression equals", exp3.equals(exp2));
    }
    
    /**
     * cover right child of the right child
     * 	two deep right group
     */
    @Test
    public void testOneDeepRightTwoDeepRight() {
    	Expression exp1 = Expression.parse("200*(x+y*z)");
    	assertEquals("expected expression", "200*(x+(y*z))", exp1.toString());
    	
    	Expression exp2 = Expression.multiplication(Expression.parse("200"), 
    			Expression.add(Expression.parse("x"), Expression.parse("y*z")));
    	assertEquals("expected expression", "200*(x+(y*z))", exp2.toString());
    	assertTrue("expected expression equals", exp1.equals(exp2));
    	assertEquals("expected hash code equals", exp1.hashCode(), exp2.hashCode());
    	
    	Expression exp3 = Expression.multiplication(
    			Expression.parse("200"), Expression.parse("(x+y)*z"));
    	assertEquals("expected expression", "200*((x+y)*z)", exp3.toString());
    	assertFalse("expected expression equals", exp3.equals(exp2));
    }
    
    /**
     * cover more than two level deep
     * 	more than two level deep group
     */
    @Test
    public void testMoreDeepLevel() {
    	Expression exp1 = Expression.parse("x*y*z*200*300*j");
    	assertEquals("expected expression", "x*(y*(z*(200*(300*j))))", exp1.toString());
    	
    	Expression exp2 = Expression.multiplication(Expression.parse("x"), Expression.parse("y*z*200*300*j"));
    	assertEquals("expected expression", "x*(y*(z*(200*(300*j))))", exp2.toString());
    	assertTrue("expected expression equals", exp1.equals(exp2));
    	assertEquals("expected hash code equals", exp1.hashCode(), exp2.hashCode());
    	
    	Expression exp3 = Expression.multiplication(Expression.parse("x*y*z*200"), Expression.parse("300*j"));
    	assertEquals("expected expression", "(x*(y*(z*200)))*(300*j)", exp3.toString());
    	assertFalse("expected expression equals", exp3.equals(exp2));
    }
}

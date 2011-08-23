package org.testobject.matcher;

/**
 * Matches only if equals() returns true.
 * @author felix trepanier
 *
 */
public class Eq implements Matcher
{
	private final Object mExpected;
	
	/**
	 * Constructor for the Eq matcher
	 * 
	 * @param aExpected The expected object
	 */
	public Eq(Object aExpected)
	{
		mExpected = aExpected;
	}
	@Override
	public boolean matches(Object aActual)
	{
		return mExpected.equals(aActual);
	}
}

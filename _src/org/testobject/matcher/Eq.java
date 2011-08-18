package org.testobject.matcher;


public class Eq implements Matcher
{
	private final Object mExpected;
	
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

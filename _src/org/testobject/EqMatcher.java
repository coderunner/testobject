package org.testobject;

public class EqMatcher implements ArgumentMatcher
{
	private final Object mExpected;
	
	public EqMatcher(Object aExpected)
	{
		mExpected = aExpected;
	}
	@Override
	public boolean matches(Object aActual)
	{
		return mExpected.equals(aActual);
	}
}

package org.testobject;

public class AnyMatcher implements ArgumentMatcher
{
	public static AnyMatcher ANY = new AnyMatcher();
	
	@Override
	public boolean matches(Object aActual)
	{
		return true;
	}
}

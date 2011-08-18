package org.testobject.matcher;


public class Any implements Matcher
{
	public static Any ANY = new Any();
	
	@Override
	public boolean matches(Object aActual)
	{
		return true;
	}
}

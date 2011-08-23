package org.testobject.matcher;

/**
 * Matcher that match anything.
 * 
 * @author felix trepanier
 *
 */
public class Any implements Matcher
{
	/**
	 * Single instance for the Any matcher.
	 */
	public static Any ANY = new Any();
	
	private Any()
	{}
	
	@Override
	public boolean matches(Object aActual)
	{
		return true;
	}
}

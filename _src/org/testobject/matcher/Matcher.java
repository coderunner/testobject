package org.testobject.matcher;

/**
 * Interface for matching method parameters.
 * 
 * @author felix trepanier
 *
 */
public interface Matcher
{
	/**
	 * Return true if the actual parameter matches.
	 * 
	 * @param aActual The actual parameter
	 * @return true if the actual parameter matches
	 */
	public boolean matches(Object aActual);
}

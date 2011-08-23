package org.testobject;


/**
 * This interface is used to specify witch return value is returned for a specific method call.
 * Alternatively, an exception can be thrown when the specific method is called.
 * 
 * @author felix trepanier
 *
 * @param <T> The type of the test object
 */
public interface ReturnValueRecorder<T>
{
	/**
	 * Specify the returned value.
	 * 
	 * @param aReturnValue The returned value
	 * @return The recorder for the test object
	 */
	public Recorder<T> andReturn(Object aReturnValue);
		
	/**
	 * Specify an exception that will be thrown.
	 * 
	 * @param aThrowable The throwable to throw
	 * @return The recorder for the test object
	 */
	public Recorder<T> andThrow(Throwable aThrowable);		
}

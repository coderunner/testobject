package org.testobject;

public interface ReturnValueRecorder<T>
{
	public Recorder<T> andReturn(Object aReturnValue);
		
	public Recorder<T> andThrow(Throwable aThowable);		
}

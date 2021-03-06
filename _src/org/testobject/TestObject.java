package org.testobject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testobject.matcher.Matcher;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * This is the entry point for TestObject. This class is used to create test object and recorder
 * for TestObject.
 * 
 * @author felix trepanier
 *
 */
public class TestObject
{		
	private static Map<Class<?>, Object> DEFAULT_PRIMITIVE_RETURN_VALUES = new HashMap<Class<?>, Object>();

    static {
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Void.TYPE, null);
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Boolean.TYPE, Boolean.FALSE);
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Byte.TYPE, Byte.valueOf((byte) 0));
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Short.TYPE, Short.valueOf((short) 0));
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Character.TYPE, Character.valueOf((char) 0));
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Integer.TYPE, Integer.valueOf(0));
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Long.TYPE, Long.valueOf(0));
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Float.TYPE, Float.valueOf(0));
        DEFAULT_PRIMITIVE_RETURN_VALUES.put(Double.TYPE, Double.valueOf(0));
    }
    
    private TestObject()
    {}
    
    /**
     * Create a test object with default behavior (return false, 0, null).
     * 
     * @param <T> The type of the test object
     * @param aClass The class of the test object
     * @return The test object
     */
	@SuppressWarnings("unchecked")
	public static <T> T createTestObject(Class<T> aClass)
	{
		if(aClass.isInterface())
		{
			RecordingProxyHandler<T> handler = new RecordingProxyHandler<T>();
			return (T) Proxy.newProxyInstance(aClass.getClassLoader(), new Class[]{aClass}, handler);
		}
		else
		{
			Enhancer e = new Enhancer();
			e.setSuperclass(aClass);
			e.setInterfaces(new Class[]{InternalReturnValueRecorder.class});
			e.setCallback(new RecordingClassProxyHandler());
			return (T) e.create();
		}		
	}
	
	/**
	 * Create a recorder for the test object. The recorder allows to define returned values
	 * or thrown exception for any methods for any parameters combination.
	 * 
	 * @param <T> The type of the test object
	 * @param aTestObject The test object
	 * @return The recoder for this test object
	 */
	public static <T> Recorder<T> createRecorder(T aTestObject)
	{
		return new Recorder<T>(aTestObject);
	}
	
	public static interface InternalReturnValueRecorder<T>
	{
		public void internal_andReturn(Object aReturnValuet);
			
		public void internal_andThrow(Throwable aThowablet);
		
		public void internal_setMatcherList(List<Matcher> aMatcherList);	
	}
	
	private static class BaseHandler<T> implements InternalReturnValueRecorder<T>
	{
		protected Method mLastMethodCalled;
		protected Object[] mLastArgs;
		protected List<Matcher> mLastMatcherList;
		
		protected Map<Method, Recording> mRecordings = new HashMap<Method, Recording>();
		
		public void internal_andReturn(Object aReturnValue)
		{
			try
			{
				if(mLastMethodCalled == null)
				{
					throw new RuntimeException("TestObject has no recorded method yet.");
				}
				if(!mLastMethodCalled.getReturnType().isPrimitive() &&
						!mLastMethodCalled.getReturnType().isAssignableFrom(aReturnValue.getClass()))
				{
					throw new RuntimeException("Recorded return value has a wrong type. Expected type: "+
							mLastMethodCalled.getReturnType().getName()+", but receiced: "+aReturnValue.getClass().getName());
				}			
				saveRecording(aReturnValue, mLastMatcherList);
			}
			finally
			{
				resetRecordState();
			}
		}
		
		public void internal_andThrow(Throwable aThrowable)
		{
			try
			{
				if(mLastMethodCalled == null)
				{
					throw new RuntimeException("TestObject has no recorded method yet.");
				}
				if(!Exception.class.isAssignableFrom(aThrowable.getClass())
						|| RuntimeException.class.isAssignableFrom(aThrowable.getClass()))
				{
					//this is a unchecked exception
					saveRecording(new ThrowableContainer(aThrowable), mLastMatcherList);
				}
				else
				{
					//this is a checked exception, check if this can be thrown by the method
					for(Class<?> ex : mLastMethodCalled.getExceptionTypes())
					{
						if(ex.isAssignableFrom(aThrowable.getClass()))
						{
							saveRecording(new ThrowableContainer(aThrowable), mLastMatcherList);
							return;
						}
					}
					throw new RuntimeException("Recorded Throwable as a wrong type for method "+mLastMethodCalled.getName()+
							". Received type: " + aThrowable.getClass());
				}
				
			}
			finally
			{
				resetRecordState();
			} 
		}
		
		@Override
		public void internal_setMatcherList(List<Matcher> aMatcherList)
		{
			mLastMatcherList = aMatcherList;
		}

		protected Object executionCall(Method aMethod, Object[] aArgs) throws Throwable
		{
			mLastMethodCalled = aMethod;
			mLastArgs = aArgs;
			Recording recording = mRecordings.get(aMethod);
			if(recording != null && recording.argumentsMatch(aArgs))
			{
				Object returnValue = recording.getReturnValue();
				if(returnValue instanceof ThrowableContainer)
				{
					((ThrowableContainer)returnValue).throwNow();
				}
				return returnValue;
			}			
			else if(aMethod.getReturnType().isPrimitive())
			{
				//can not return null, so return default value for the type
				return DEFAULT_PRIMITIVE_RETURN_VALUES.get(aMethod.getReturnType());
			}
			return null;
		}
		
		private void resetRecordState()
		{
			mLastMethodCalled = null;
			mLastArgs = null;
			mLastMatcherList = null;
		}	
		
		private void saveRecording(Object aReturnValue,	List<Matcher> aMatcherList)
		{
			if(aMatcherList != null && mLastArgs.length != aMatcherList.size())
			{
				throw new RuntimeException("When specifying matchers, all arguments have to specify a matcher.");
			}
			mRecordings.put(mLastMethodCalled, new Recording(mLastArgs, aMatcherList, aReturnValue));
		}
	}
	
	private static class RecordingProxyHandler<T> extends BaseHandler<T> implements InvocationHandler
	{		
		public Object invoke(Object aTarget, Method aMethod, Object[] aArguments) throws Throwable
		{
			return executionCall(aMethod, aArguments);
		}		
	}
	
	private static class RecordingClassProxyHandler<T> extends BaseHandler<T> implements MethodInterceptor
	{
		@SuppressWarnings("unchecked")
		@Override
		public Object intercept(Object aProxy, Method aMethod, Object[] aMethodArgs, MethodProxy aMethodProxy) throws Throwable
		{			
			if(aMethod.getName().equals("internal_andReturn"))
			{
				internal_andReturn(aMethodArgs[0]);
				return null;
			}
			else if(aMethod.getName().equals("internal_andThrow"))
			{
				internal_andThrow((Throwable)aMethodArgs[0]);
				return null;
			}
			else if(aMethod.getName().equals("internal_setMatcherList"))
			{
				internal_setMatcherList((List<Matcher>)aMethodArgs[0]);
				return null;
			}
			else
			{
				return executionCall(aMethod, aMethodArgs);
			}
		}	
	}
	
	private static class ThrowableContainer
	{
		private final Throwable mToThrow;
		
		public ThrowableContainer(Throwable aThrowable)
		{
			mToThrow = aThrowable;
		}
		
		public void throwNow() throws Throwable
		{
			throw mToThrow;
		}
	}
	
	private static class Recording
	{
		private final Object[] mArgs;
		private final Object mReturnedValue;
		private final List<Matcher> mMatcherList;
		
		public Recording(Object[] aArgs, List<Matcher> aMatcherList, Object aReturnValue)
		{
			mArgs = aArgs;
			mMatcherList = aMatcherList;
			mReturnedValue = aReturnValue;
		}
		
		public boolean argumentsMatch(Object[] aActualArgs)
		{
			if(aActualArgs == null)
			{
				//no argument method, always match
				return true;
			}
			if(mMatcherList != null)
			{
				//use matchers
				for(int i=0;i<mArgs.length;i++)
				{
					if(!mMatcherList.get(i).matches(aActualArgs[i]))
					{
						return false;
					}
				}
				return true;
			}
			//no matchers use equality
			if(mArgs != null)
			{
				for(int i=0;i<mArgs.length;i++)
				{
					if((mArgs[i] == null && aActualArgs[i] != null)
							|| (mArgs[i] != null && aActualArgs[i] == null))
					{
						return false;
					}
					if(mArgs[i] != null && !mArgs[i].equals(aActualArgs[i]))
					{
						return false;
					}
				}
				return true;
			}
			return true;
		}
		
		public Object getReturnValue()
		{
			 return mReturnedValue;
		}
	}
}
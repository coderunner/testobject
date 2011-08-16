package org.testobject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

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
	
	public static class Recorder<T>
	{
		private final T mTestObject;
		private final boolean mIsEnhanced;
		private final ReturnValueRecorder<T> mReturnValueRecorder = new ReturnValueRecorder<T>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public Recorder<T> andReturn(Object aReturnValue)
			{
				if(mIsEnhanced)
				{
					((InternalReturnValueRecorder)mTestObject).internal_andReturn(aReturnValue);
				}
				else
				{
					((InternalReturnValueRecorder)Proxy.getInvocationHandler(mTestObject)).internal_andReturn(aReturnValue);	
				}	
				return Recorder.this;
			}

			@SuppressWarnings("unchecked")
			@Override
			public Recorder<T> andThrow(Throwable aThowable)
			{
				if(mIsEnhanced)
				{
					((InternalReturnValueRecorder)mTestObject).internal_andThrow(aThowable);
				}
				else
				{
					((InternalReturnValueRecorder)Proxy.getInvocationHandler(mTestObject)).internal_andThrow(aThowable);	
				}	
				return Recorder.this;
			}
		};
		
		public Recorder(T aTestObject)
		{
			mTestObject = aTestObject;
			mIsEnhanced = Enhancer.isEnhanced(aTestObject.getClass());
		}
		
		public ReturnValueRecorder<T> record(final Object aObject)
		{
			return recordForLastCall();
		}
		
		public ReturnValueRecorder<T> recordForLastCall()
		{
			return mReturnValueRecorder; 
		}

	}
	
	public static interface ReturnValueRecorder<T>
	{
		public Recorder<T> andReturn(Object aReturnValue);
			
		public Recorder<T> andThrow(Throwable aThowable);		
	}
	
	private static interface InternalReturnValueRecorder<T>
	{
		public void internal_andReturn(Object aReturnValue);
			
		public void internal_andThrow(Throwable aThowable);		
	}
	
	private static class BaseHandler<T> implements InternalReturnValueRecorder<T>
	{
		protected Method mLastMethodCalled;
		protected Map<Method, Object> mRecordings = new HashMap<Method, Object>();
		
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
				mRecordings.put(mLastMethodCalled, aReturnValue);
			}
			finally
			{
				mLastMethodCalled = null;
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
					mRecordings.put(mLastMethodCalled, new ThrowableContainer(aThrowable));
				}
				else
				{
					//this is a checked exception, check if this can be thrown by the method
					boolean canBeThrown = false;
					for(Class<?> ex : mLastMethodCalled.getExceptionTypes())
					{
						if(ex.isAssignableFrom(aThrowable.getClass()))
						{
							mRecordings.put(mLastMethodCalled, new ThrowableContainer(aThrowable));
							return;
						}
					}
					throw new RuntimeException("Recorded Throwable as a wrong type for method "+mLastMethodCalled.getName()+
							". Received type: " + aThrowable.getClass());
				}
				
			}
			finally
			{
				mLastMethodCalled = null;
			}
		}	
		
		protected Object executionCall(Method aMethod) throws Throwable
		{
			mLastMethodCalled = aMethod;
			Object returnValue = mRecordings.get(aMethod);
			if(returnValue instanceof ThrowableContainer)
			{
				((ThrowableContainer)returnValue).throwNow();
			}
			else if(returnValue == null && aMethod.getReturnType().isPrimitive())
			{
				//can not return null, so return default value for the type
				return DEFAULT_PRIMITIVE_RETURN_VALUES.get(aMethod.getReturnType());
			}
			return returnValue;
		}
	}
	
	private static class RecordingProxyHandler<T> extends BaseHandler<T> implements InvocationHandler
	{		
		public Object invoke(Object aTarget, Method aMethod, Object[] aArguments) throws Throwable
		{
			return executionCall(aMethod);
		}		
	}
	
	private static class RecordingClassProxyHandler<T> extends BaseHandler<T> implements MethodInterceptor
	{
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
			else
			{
				return executionCall(aMethod);
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
}
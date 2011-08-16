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
	private static Map<Class<?>, Object> defaultPrimitiveReturnValues = new HashMap<Class<?>, Object>();

    static {
        defaultPrimitiveReturnValues.put(Void.TYPE, null);
        defaultPrimitiveReturnValues.put(Boolean.TYPE, Boolean.FALSE);
        defaultPrimitiveReturnValues.put(Byte.TYPE, Byte.valueOf((byte) 0));
        defaultPrimitiveReturnValues.put(Short.TYPE, Short.valueOf((short) 0));
        defaultPrimitiveReturnValues.put(Character.TYPE, Character.valueOf((char) 0));
        defaultPrimitiveReturnValues.put(Integer.TYPE, Integer.valueOf(0));
        defaultPrimitiveReturnValues.put(Long.TYPE, Long.valueOf(0));
        defaultPrimitiveReturnValues.put(Float.TYPE, Float.valueOf(0));
        defaultPrimitiveReturnValues.put(Double.TYPE, Double.valueOf(0));
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
		
		public ReturnValueRecorder<T> record(final Object object)
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
		protected Method lastMethodCalled;
		protected Map<Method, Object> mRecordings = new HashMap<Method, Object>();
		
		public void internal_andReturn(Object returnValue)
		{
			if(lastMethodCalled == null)
			{
				throw new RuntimeException("No method call");
			}
			if(!lastMethodCalled.getReturnType().isPrimitive() &&
			   !lastMethodCalled.getReturnType().isAssignableFrom(returnValue.getClass()))
			{
				throw new RuntimeException("Recorded return value has a wrong type. Expected type: "+
						lastMethodCalled.getReturnType().getName()+", but receiced: "+returnValue.getClass().getName());
			}
			mRecordings.put(lastMethodCalled, returnValue);
			lastMethodCalled = null;
		}
		
		public void internal_andThrow(Throwable throwable)
		{
			if(lastMethodCalled == null)
			{
				throw new RuntimeException("No method call");
			}
			mRecordings.put(lastMethodCalled, new ThrowableContainer(throwable));
			lastMethodCalled = null;
		}	
		
		protected Object executionCall(Method method) throws Throwable
		{
			lastMethodCalled = method;
			Object returnValue = mRecordings.get(method);
			if(returnValue instanceof ThrowableContainer)
			{
				((ThrowableContainer)returnValue).throwNow();
			}
			else if(returnValue == null && method.getReturnType().isPrimitive())
			{
				//can not return null, so return default value for the type
				return defaultPrimitiveReturnValues.get(method.getReturnType());
			}
			return returnValue;
		}
	}
	
	private static class RecordingProxyHandler<T> extends BaseHandler<T> implements InvocationHandler
	{		
		public Object invoke(Object target, Method method, Object[] arguments) throws Throwable
		{
			return executionCall(method);
		}		
	}
	
	private static class RecordingClassProxyHandler<T> extends BaseHandler<T> implements MethodInterceptor
	{
		@Override
		public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable
		{			
			if(arg1.getName().equals("internal_andReturn"))
			{
				internal_andReturn(arg2[0]);
				return null;
			}
			else if(arg1.getName().equals("internal_andThrow"))
			{
				internal_andThrow((Throwable)arg2[0]);
				return null;
			}
			else
			{
				return executionCall(arg1);
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
package org.testobject;

import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;

import org.testobject.TestObject.InternalReturnValueRecorder;
import org.testobject.matcher.Matcher;

public class Recorder<T>
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
	private List<Matcher> mCurrentArgumentMatchers;
	
	Recorder(T aTestObject)
	{
		mTestObject = aTestObject;
		mIsEnhanced = Enhancer.isEnhanced(aTestObject.getClass());
	}
	
	public ReturnValueRecorder<T> record(final Object aObject)
	{
		return recordForLastCall();
	}
	
	@SuppressWarnings("unchecked")
	public ReturnValueRecorder<T> recordForLastCall()
	{
		if(mIsEnhanced)
		{
			((InternalReturnValueRecorder)mTestObject).internal_setMatcherList(mCurrentArgumentMatchers);
		}
		else
		{
			((InternalReturnValueRecorder)Proxy.getInvocationHandler(mTestObject)).internal_setMatcherList(mCurrentArgumentMatchers);	
		}
		mCurrentArgumentMatchers = null;
		return mReturnValueRecorder; 
	}
			
	public boolean matchBoolean(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return false;
	}
	
	public byte matchByte(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return 0;
	}
	
	public char matchChar(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return 0;
	}
	
	public short matchShort(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return 0;
	}
	
	public int matchInt(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return 0;
	}
	
	public long matchLong(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return 0;
	}
	
	public float matchFloat(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return 0;
	}
	
	public double matchDouble(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return 0;
	}
	
	public <A> A matchObject(Matcher aMatcher)
	{
		addArgumentMatcher(aMatcher);
		return null;
	}
	
	private void addArgumentMatcher(Matcher aMatcher)
	{
		if(mCurrentArgumentMatchers == null)
		{
			mCurrentArgumentMatchers = new LinkedList<Matcher>();
		}
		mCurrentArgumentMatchers.add(aMatcher);
	}
}
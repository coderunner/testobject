package org.testobject;

import java.io.IOException;

public class TestClass implements TestInterface
{

	@Override
	public boolean returnBoolean()
	{
		return true;
	}

	@Override
	public byte returnByte()
	{
		return 1;
	}

	@Override
	public char returnChar()
	{
		return 1;
	}

	@Override
	public double returnDouble()
	{
		return 1;
	}

	@Override
	public float returnFloat()
	{
		return 1;
	}

	@Override
	public int returnInt()
	{
		return 1;
	}

	@Override
	public Integer returnInteger()
	{
		return new Integer(1);
	}

	@Override
	public long returnLong()
	{
		return 1;
	}

	@Override
	public void returnNothing() {}

	@Override
	public Object returnObject()
	{
		return new Object();
	}

	@Override
	public short returnShort()
	{
		return 1;
	}

	@Override
	public void throwDeclardeException() throws IOException
	{}

	@Override
	public String methodWithArguments(String aString, int aInt)
	{
		return "";
	}

}

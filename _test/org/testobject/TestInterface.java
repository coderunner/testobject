package org.testobject;

import java.io.IOException;

public interface TestInterface
{
	public void returnNothing();
	public boolean returnBoolean();
	public byte returnByte();
	public char returnChar();
	public int returnInt();
	public short returnShort();	
	public long returnLong();
	public float returnFloat();	
	public double returnDouble();
	
	public void throwDeclardeException() throws IOException;
	public Object returnObject();
	public Integer returnInteger();
}

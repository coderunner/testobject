package org.testobject;

import org.junit.Test;
import static org.junit.Assert.* ;

public class TestTestObject
{
	//TODO: parameters matching for return or throw
	//TODO: param matching using a comparator
	//TODO: need match any type of matching
	//TODO: is more than one exact call that matched, then sequence?
	
	@Test
	public void shouldReturnDefaultPrimitiveValueIfNoRecordingForInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertDefaultPrimitiveTypeReturnValues(testObject);		
	}

	@Test
	public void shouldReturnDefaultPrimitiveValueIfNoRecordingForClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertDefaultPrimitiveTypeReturnValues(testObject);		
	}

	@Test
	public void shouldReturnNullIfNoRecordingForInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);		
		assertDefaultObjectReturnValue(testObject);
	}
	
	@Test
	public void shouldReturnNullIfNoRecordingForClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);		
		assertDefaultObjectReturnValue(testObject);
	}
	
	@Test
	public void shouldReturnTheRecorderReturnValueInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertReturnRecordedValue(testObject);
	}
	
	@Test
	public void shouldReturnTheRecorderReturnValueClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);		
		assertReturnRecordedValue(testObject);
	}
	
	@Test
	public void recordThrowExceptionShouldThrowExceptionInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);		
		assertRecodedExceptionThrown(testObject);
	}
	
	@Test
	public void recordThrowExceptionShouldThrowExceptionClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);		
		assertRecodedExceptionThrown(testObject);
	}
	
	@Test
	public void shouldThrowExceptionIfSettingAReturnValueOfWrongTypeInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertExceptionThrownIfReturnValueHasWrongType(testObject);
	}
	
	@Test
	public void shouldThrowExceptionIfSettingAReturnValueOfWrongTypeClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertExceptionThrownIfReturnValueHasWrongType(testObject);
	}
	
	@Test
	public void shouldBeAbleToSetExceptionOnVoidMethod()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		TestObject.Recorder<TestInterface> recorder = 
			new TestObject.Recorder<TestInterface>(testObject);
		
		testObject.returnNothing();
		recorder.recordForLastCall().andThrow(new RuntimeException());
		
		try
		{
			testObject.returnNothing();
			fail();
		}
		catch(RuntimeException e)
		{}
	}

	private void assertExceptionThrownIfReturnValueHasWrongType(
			TestInterface testObject) {
		TestObject.Recorder<TestInterface> recorder = new TestObject.Recorder<TestInterface>(testObject);
		try
		{
			recorder.record(testObject.toString()).andReturn(new Long(10));
			fail();
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
		}
	}

	private void assertRecodedExceptionThrown(TestInterface testObject)
	{
		TestObject.Recorder<TestInterface> recorder = 
			new TestObject.Recorder<TestInterface>(testObject);
		
		recorder.record(testObject.returnObject()).andThrow(new IllegalArgumentException());
		
		try
		{
			testObject.returnObject();
			fail();
		}
		catch(IllegalArgumentException ex)
		{}
	}
	
	private void assertReturnRecordedValue(TestInterface testObject)
	{
		String returnValue = "returnValue";
		Integer returnInteger = 10;
		
		TestObject.Recorder<TestInterface> recorder = new TestObject.Recorder<TestInterface>(testObject);
		recorder.record(testObject.returnObject()).andReturn(returnValue)
				.record(testObject.returnInteger()).andReturn(returnInteger);
		
		assertEquals(returnValue, testObject.returnObject());
		assertEquals(returnInteger, testObject.returnInteger());
	}

	private void assertDefaultObjectReturnValue(TestInterface testObject)
	{
		assertNull(testObject.returnObject());
		assertNull(testObject.returnInteger());
	}
	
	private void assertDefaultPrimitiveTypeReturnValues(TestInterface testObject)
	{
		testObject.returnNothing();
		assertEquals(false, testObject.returnBoolean());
		assertEquals(0, testObject.returnChar());
		assertEquals(0, testObject.returnByte());
		assertEquals(0, testObject.returnShort());
		assertEquals(0, testObject.returnInt());
		assertEquals(0, testObject.returnLong());
		assertTrue(0 == testObject.returnDouble());
		assertTrue(0 == testObject.returnFloat());
	}
}

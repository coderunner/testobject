package org.testobject;

import java.io.IOException;

import org.junit.Test;
import org.testobject.definition.TestClass;
import org.testobject.definition.TestInterface;
import org.testobject.matcher.Any;
import org.testobject.matcher.Matcher;
import org.testobject.matcher.Eq;

import static org.junit.Assert.* ;

public class TestTestObject
{	
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
	
	@Test(expected=IllegalArgumentException.class)
	public void recordThrowExceptionShouldThrowExceptionInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);		
		assertRecodedExceptionThrown(testObject);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void recordThrowExceptionShouldThrowExceptionClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);		
		assertRecodedExceptionThrown(testObject);
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldThrowExceptionIfSettingAReturnValueOfWrongTypeInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertExceptionThrownIfReturnValueHasWrongType(testObject);
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldThrowExceptionIfSettingAReturnValueOfWrongTypeClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertExceptionThrownIfReturnValueHasWrongType(testObject);
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldBeAbleToSetExceptionOnVoidMethodInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertThrowOnVoidMethod(testObject);
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldBeAbleToSetExceptionOnVoidMethodClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertThrowOnVoidMethod(testObject);
	}
	
	@Test
	public void shouldBeAbleToAlterPrimitiveTypeDefaultValueInterface()
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertCanModifyReturnValueForPrimitiveType(testObject);	
	}
	
	@Test
	public void shouldBeAbleToAlterPrimitiveTypeDefaultValueClass()
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertCanModifyReturnValueForPrimitiveType(testObject);	
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldNotAllowToThrowACheckedExceptionOfWrongTypeInterface() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertDoesNotAllowWrongTypeException(testObject);		
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldNotAllowToThrowACheckedExceptionOfWrongTypeClass() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertDoesNotAllowWrongTypeException(testObject);		
	}
	
	@Test
	public void useArgumentMatchingForRecordingBehaviorInterface() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertRecordingUseArgumentMatching(testObject);
	}

	@Test
	public void useArgumentMatchingForRecordingBehaviorClass() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertRecordingUseArgumentMatching(testObject);
	}
	
	@Test
	public void useLooseArgumentMatchingInterface() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertUseLooseArgumentMatching(testObject);
	}

	@Test
	public void useLooseArgumentMatchingClass() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertUseLooseArgumentMatching(testObject);
	}
	
	@Test
	public void useLooseArgumentMatchingWithEqMatcherInterface() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertEqMatcher(testObject);
	}
	
	@Test
	public void useLooseArgumentMatchingWithEqMatcherClass() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertEqMatcher(testObject);
	}
	
	@Test(expected=RuntimeException.class)
	public void reportInvalidNumberOfMatchersInterface() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertReportWrongNumberOfMatchers(testObject);
	}
	
	@Test(expected=RuntimeException.class)
	public void reportInvalidNumberOfMatchersClass() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertReportWrongNumberOfMatchers(testObject);
	}
	
	@Test
	public void useCustomMatcherInterface() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertUseCustomMatcher(testObject);
	}
	
	@Test
	public void useCustomMatcherClass() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertUseCustomMatcher(testObject);
	}
	
	@Test
	public void nullShouldBeMatchedInterface() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertNullParametersMatched(testObject);
	}
	
	@Test
	public void nullShouldBeMatchedClass() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertNullParametersMatched(testObject);
	}

	@Test(expected=RuntimeException.class)
	public void voidMethodWithParamMatchingInterface() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestInterface.class);
		assertVoidMethodWithParamMatching(testObject);
	}
	
	@Test(expected=RuntimeException.class)
	public void voidMethodWithParamMatchingClass() throws Exception
	{
		TestInterface testObject = TestObject.createTestObject(TestClass.class);
		assertVoidMethodWithParamMatching(testObject);
	}

	private void assertVoidMethodWithParamMatching(TestInterface testObject)
	{
		Recorder<TestInterface> recorder = new Recorder<TestInterface>(testObject);
		
		String arg = "arg";
		testObject.methodWithArguments(arg);
		recorder.recordForLastCall().andThrow(new RuntimeException());
		
		testObject.methodWithArguments("otherstring");
		testObject.methodWithArguments(arg);
	}

	private void assertNullParametersMatched(TestInterface testObject)
	{
		String stringArg = null;
		int intArg = 11;
		String returnedValue = "returnedValue"; 
		Recorder<TestInterface> recorder = new Recorder<TestInterface>(testObject);
		recorder.record(testObject.methodWithArguments(stringArg, intArg)).andReturn(returnedValue);
		
		String actual = testObject.methodWithArguments("string", -1);
		assertNull(actual); 
		
		actual = testObject.methodWithArguments(stringArg, intArg);
		assertEquals(actual, returnedValue);
	}

	private void assertUseCustomMatcher(TestInterface testObject)
	{
		String returnedValue = "returnedValue";
		String stringArg = "stringarg";
		int intArg = -1;
		Recorder<TestInterface> recorder = new Recorder<TestInterface>(testObject);
		recorder.record(testObject.methodWithArguments((String)recorder.matchObject(new Matcher()
		{
			@Override
			public boolean matches(Object aActual)
			{
				return ((String)aActual).startsWith("s");
			}
			
		}), recorder.matchInt(new Matcher()
		{
			@Override
			public boolean matches(Object aActual)
			{
				return ((Integer)aActual).intValue() < 0;
			}
		}))).andReturn(returnedValue);
		
		String actual = testObject.methodWithArguments(stringArg, intArg);
		assertEquals(actual, returnedValue);
	}
	
	private void assertEqMatcher(TestInterface testObject)
	{
		String returnedValue = "returnedValue";
		String stringArg = "stringarg";
		int intArg = -1;
		Recorder<TestInterface> recorder = TestObject.createRecorder(testObject);
		recorder.record(testObject.methodWithArguments((String) recorder.matchObject(new Eq(stringArg)), recorder.matchInt(new Eq(intArg))))
			.andReturn(returnedValue);
		
		String actual = testObject.methodWithArguments(stringArg, intArg);
		assertEquals(actual, returnedValue);
	}

	private void assertReportWrongNumberOfMatchers(TestInterface testObject)
	{
		Recorder<TestInterface> recorder = TestObject.createRecorder(testObject);

		recorder.record(testObject.methodWithArguments((String)recorder.matchObject(Any.ANY), 10))
			.andReturn("value");
	}
	
	private void assertUseLooseArgumentMatching(TestInterface testObject)
	{
		String returnedValue = "returnedValue"; 
		Recorder<TestInterface> recorder = TestObject.createRecorder(testObject);
		recorder.record(testObject.methodWithArguments((String)recorder.matchObject(Any.ANY), recorder.matchInt(Any.ANY)))
			.andReturn(returnedValue);
		
		String actual = testObject.methodWithArguments("otherArg", -1);;
		assertEquals(actual, returnedValue);
	}
	
	private void assertRecordingUseArgumentMatching(TestInterface testObject)
	{
		String stringArg = "arg";
		int intArg = 11;
		String returnedValue = "returnedValue"; 
		Recorder<TestInterface> recorder = TestObject.createRecorder(testObject);
		recorder.record(testObject.methodWithArguments(stringArg, intArg)).andReturn(returnedValue);
		
		String actual = testObject.methodWithArguments("otherArg", -1);
		assertNull(actual); 
		
		actual = testObject.methodWithArguments(stringArg, intArg);
		assertEquals(actual, returnedValue);
	}
	
	

	private void assertDoesNotAllowWrongTypeException(TestInterface testObject)
			throws IOException
	{
		Recorder<TestInterface> recorder = TestObject.createRecorder(testObject);
		
		testObject.throwDeclardeException();
		recorder.recordForLastCall().andThrow(new Exception());
	}

	private void assertCanModifyReturnValueForPrimitiveType(
			TestInterface testObject)
	{
		Recorder<TestInterface> recorder = TestObject.createRecorder(testObject);
		
		boolean expectedBoolean = true;
		byte expectedByte = 9;
		char expectedChar = 10;
		short expectedShort = 11;
		int expectedInt = 12;
		long expectedLong = 13;
		float expectedFloat = 14.2f;
		double expectedDouble = 15.5d;
		recorder.record(testObject.returnBoolean()).andReturn(expectedBoolean)
				.record(testObject.returnByte()).andReturn(expectedByte)
				.record(testObject.returnChar()).andReturn(expectedChar)
				.record(testObject.returnShort()).andReturn(expectedShort)
				.record(testObject.returnInt()).andReturn(expectedInt)
				.record(testObject.returnLong()).andReturn(expectedLong)
				.record(testObject.returnFloat()).andReturn(expectedFloat)
				.record(testObject.returnDouble()).andReturn(expectedDouble);
		
		assertEquals(expectedBoolean, testObject.returnBoolean());
		assertEquals(expectedByte, testObject.returnByte());
		assertEquals(expectedChar, testObject.returnChar());
		assertEquals(expectedShort, testObject.returnShort());
		assertEquals(expectedInt, testObject.returnInt());
		assertEquals(expectedLong, testObject.returnLong());
		assertTrue(expectedFloat == testObject.returnFloat());
		assertTrue(expectedDouble == testObject.returnDouble());
	}

	private void assertThrowOnVoidMethod(TestInterface testObject)
	{
		Recorder<TestInterface> recorder = 
			TestObject.createRecorder(testObject);
		
		testObject.returnNothing();
		recorder.recordForLastCall().andThrow(new RuntimeException());

		testObject.returnNothing();
	}

	private void assertExceptionThrownIfReturnValueHasWrongType(
			TestInterface testObject)
	{
		Recorder<TestInterface> recorder = TestObject.createRecorder(testObject);
		recorder.record(testObject.toString()).andReturn(new Long(10));
	}

	private void assertRecodedExceptionThrown(TestInterface testObject)
	{
		Recorder<TestInterface> recorder = 
			TestObject.createRecorder(testObject);
		
		recorder.record(testObject.returnObject()).andThrow(new IllegalArgumentException());
		testObject.returnObject();
	}
	
	private void assertReturnRecordedValue(TestInterface testObject)
	{
		String returnValue = "returnValue";
		Integer returnInteger = 10;
		
		Recorder<TestInterface> recorder = TestObject.createRecorder(testObject);
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

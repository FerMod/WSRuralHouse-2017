package test;

import static org.junit.Assume.assumeNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import domain.observer.ObservedValue;
import test.data.PersonTest;

class ObservableValueTest {

	static Optional<File> tempFile;
	boolean eventInvoked;

	@BeforeEach
	void beforeEach() {
		initTempFile("SerializedObjectTest");
		eventInvoked = false;
	}

	private void initTempFile(String fileName) {
		File file = null;
		try {
			file = File.createTempFile(fileName, ".tmp");
			file.deleteOnExit();
			tempFile = Optional.ofNullable(file);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(tempFile.isPresent()) {
				tempFile.get().delete();
			}
		}
	}

	@DisplayName("Test Event - Method Invocation")
	@ParameterizedTest
	@CsvSource({"30, 10", "-2, 10", "11, 10"})
	void testEventMethodInvocation(int expected, int value, TestInfo testInfo) {

		try {
			ObservedValue<Integer> observedValue = new ObservedValue<>(10);
			observedValue.registerListener((oldVAlue, newValue) -> {
				eventInvoked = true;
				assertAll("EventValues",
					() -> assertEquals(oldVAlue.get(), (Integer)value, () -> "New value missmatch in event invocation."),
					() -> assertEquals(newValue.get(), (Integer)expected, () -> "Old value missmatch in event invocation.")
				);
			});

			observedValue.set(expected);

			assertTrue(eventInvoked, () -> "Event method not invoked.");
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getClass().getSimpleName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Boolean")
	@ParameterizedTest
	@CsvSource({"true", "false"})	
	void testSerialization(boolean expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<Boolean> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Boolean> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Boolean>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Byte")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(byte expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<Byte> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Byte> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Byte>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, (byte)value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Char")
	@ParameterizedTest
	@CsvSource({"A", "T"})		
	void testSerialization(char expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<Character> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Character> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Character>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, (char)value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Short")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(short expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<Short> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Short> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Short>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, (short)value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Int")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(int expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<Integer> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Integer> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Integer>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, (int)value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Long")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(long expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<Long> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Long> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Long>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, (long)value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Float")
	@ParameterizedTest
	@CsvSource({"65f"})		
	void testSerialization(float expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<Float> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Float> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Float>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, (float)value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Double")
	@ParameterizedTest
	@CsvSource({"65.55"})	
	void testSerialization(double expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<Double> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Double> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Double>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, (double)value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - String")
	@ParameterizedTest
	@CsvSource({"'Test of string', 'Another test'"})		
	void testSerialization(String expected) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		ObservedValue<String> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<String> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<String>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Object")
	@ParameterizedTest
	@CsvSource({"Paco, 44", "Lola, 41"})
	void testSerialization(String name, int age) {

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		PersonTest expectedTestClass = new PersonTest(name, age);
		ObservedValue<PersonTest> observedValue = new ObservedValue<PersonTest>(expectedTestClass);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<PersonTest> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<PersonTest>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}
		
		assertEquals(expectedTestClass, value.get(), "" + observedValue.get().hashCode() + " " + value.get().hashCode());				
	}

	private <T> void serialiceToFile(File file, T value) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
				objectOutputStream.writeObject(value);	
				objectOutputStream.flush();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}
	}

	//		try{
	//
	//
	//			temp.deleteOnExit();
	//			System.out.println("Temp file : " + temp.getAbsolutePath());
	//
	//		}catch(IOException e){
	//
	//			e.printStackTrace();
	//
	//		}
	//		ObservedValue<String> test = new ObservedValue<String>("test cosa");
	//		try {
	//			FileOutputStream fileOutputStream = new FileOutputStream(tempFile.get());
	//			try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
	//				objectOutputStream.writeObject(test);
	//				objectOutputStream.flush();
	//			}
	//			System.out.println(test.get());
	//
	//			FileInputStream fileInputStream = new FileInputStream("D:\\Users\\Ferran Tudela\\Desktop\\TestSerializacion.txt");
	//			ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream);
	//			ObservedValue<String> test2 = (ObservedValue<String>) objectInputStream.readObject();
	//			objectInputStream.close(); 
	//			System.out.println(test2.get());
	//
	//		} catch (Exception e) {
	//			// TODO: handle exception
	//		}
	//		test.addListener(new ValueChangeListener() {
	//
	//			@Override
	//			public <T> void onValueChanged(T oldValue, T newValue) {
	//				System.out.println("He cambiado" +oldValue + "ahora" + newValue);
	//
	//			}
	//		});
	//
	//
	//		test.set("HOLAAAAAAAAAAAAAAAAAa");
	//	}
}

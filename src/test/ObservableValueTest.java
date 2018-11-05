package test;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import domain.observer.ObservedValue;

class ObservableValueTest {

	static Optional<File> tempFile;

	@BeforeEach
	void beforeEach() {
		File file = null;
		try {
			file = File.createTempFile("SerializedObjectTest", ".tmp");
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

		assertEquals(expected, (boolean)value.get());

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

		assertEquals(expected, (String)value.get());

	}
	
	@Disabled
	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Object")
	@Test	
	void testSerialization() {

		TestClass expectedTestClass = new TestClass("Test of class");
		ObservedValue<TestClass> expected = new ObservedValue<TestClass>(expectedTestClass);

		assumeTrue(tempFile.isPresent());
		File file = tempFile.get();

		serialiceToFile(file, expected);
		assumeTrue(file.length() > 0);

		ObservedValue<TestClass> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<TestClass>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expectedTestClass, value.get());				

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

	class TestClass implements Serializable {

		String value = "Test of class";

		public TestClass(String value) {
			this.value = value;
		}

		String getValue() {
			return value;	
		}

		private static final long serialVersionUID = -8858049414160214232L;

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
	//		test.addListener(new ValueChangedListener() {
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

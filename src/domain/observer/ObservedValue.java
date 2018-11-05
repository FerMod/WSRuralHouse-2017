package domain.observer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ObservedValue<T> implements Serializable {

	public static void main(String[] args) {
		ObservedValue<String> test = new ObservedValue<String>("test cosa");
		try {
		FileOutputStream fileOutputStream = new FileOutputStream("D:\\Users\\Ferran Tudela\\Desktop\\TestSerializacion.txt");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(test);
		objectOutputStream.flush();
		objectOutputStream.close();
		System.out.println(test.get());
		
		FileInputStream fileInputStream = new FileInputStream("D:\\Users\\Ferran Tudela\\Desktop\\TestSerializacion.txt");
		ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream);
		ObservedValue<String> test2 = (ObservedValue<String>) objectInputStream.readObject();
		objectInputStream.close(); 
		System.out.println(test2.get());
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		test.addListener(new ValueChangedListener() {
			
			@Override
			public <T> void onValueChanged(T oldValue, T newValue) {
				System.out.println("He cambiado" +oldValue + "ahora" + newValue);
				
			}
		});
		

		test.set("HOLAAAAAAAAAAAAAAAAAa");
	}

	private transient List<ValueChangedListener> eventListeners;
	private transient Optional<T> value;

	public ObservedValue() {
		this(null);
	}

	public ObservedValue(T value) {
		eventListeners = new ArrayList<ValueChangedListener>();
		this.value = Optional.ofNullable(value);
	}

	public void addListener(ValueChangedListener listener) {
		eventListeners.add(listener);
	}

	public void set(T value) {		
		setOptional(Optional.ofNullable(value));
	}

	public void setOptional(Optional<T> value) {		
		for (ValueChangedListener listener : eventListeners) {
			listener.onValueChanged(this.value, value);
		}
		this.value = value;
	}

	public T get() {
		return value.orElse(null);

	}

	public Optional<T> getOptional() {
		return value;

	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(value.orElse(null));
	}

	/**
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @see Serializable
	 */
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.value = Optional.ofNullable((T) in.readObject());
	}


	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 3442976866444351124L;

}

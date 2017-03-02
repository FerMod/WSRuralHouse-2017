package exceptions;

/** 
 * Thrown when is attempted to add an existing entity to the database.
 */
public class DuplicatedEntityException extends Exception {

	private static final long serialVersionUID = 2564825147116821092L;

	private Entity entInfo;

	/**
	 * Constructs a {@code DuplicatedEntityException} with a default message.
	 */
	public DuplicatedEntityException() {
		super("Attempted to create an entity that already exists.");
	}

	/**
	 * Constructs a {@code DuplicatedEntityException} with the specified detail message.
	 *
	 * @param   s   the detail message.
	 * 
	 */
	public DuplicatedEntityException(String s) {
		super(s);
	}

	public Entity getEntity() {
		return entInfo;
	}

	public Entity addEntityInfo(String entityName, String entityValue) {
		return entInfo = new Entity(entityName, entityValue);
	}

	public class Entity extends DuplicatedEntityException {

		private static final long serialVersionUID = -3164649154662306727L;

		private String name;
		private String value;

		public Entity(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "[" + name + ": " + value + "]";
		}

	}

}

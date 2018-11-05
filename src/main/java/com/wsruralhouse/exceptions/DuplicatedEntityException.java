package com.wsruralhouse.exceptions;

/** 
 * Thrown when is attempted to add an existing entity to the database.
 */
public class DuplicatedEntityException extends Exception {

	private static final long serialVersionUID = 2564825147116821092L;

	private Error error;

	public enum Error {
		DUPLICATED_ENTITY(0, "The entity already exist."),
		DUPLICATED_USERNAME(1, "The username is already in use by annother account."),
		DUPLICATED_EMAIL(2, "The email adress is already in use by annother account.");

		private final int code;
		private final String description;

		private Error(int code, String description) {
			this.code = code;
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public int getCode() {
			return code;
		}

		@Override
		public String toString() {
			return code + ": " + description;
		}

	}

	/**
	 * Constructs a {@code DuplicatedEntityException} with a default message.
	 */
	public DuplicatedEntityException() {
		super("Attempted to create an entity that already exists.");
		this.error = Error.DUPLICATED_ENTITY;
	}

	/**
	 * Constructs a {@code DuplicatedEntityException} with the specified detail message.
	 *
	 * @param   s   the detail message.
	 * 
	 */
	public DuplicatedEntityException(String s) {
		super(s);
		this.error = Error.DUPLICATED_ENTITY;
	}

	/**
	 * Constructs a {@code DuplicatedEntityException} with the specified error code.
	 *
	 * @param   error   the error type.
	 * 
	 */
	public DuplicatedEntityException(Error error) {
		super(error.getDescription());
		this.error = error;
	}

	/**
	 * Return the error that has been thrown.
	 * @return the error type
	 */
	public Error getError() {
		return error;
	}

}

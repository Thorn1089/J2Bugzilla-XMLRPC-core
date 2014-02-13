package com.j2bugzilla.xmlrpc.core;

/**
 * The {@link BugzillaTransportException} is an unchecked exception that signals
 * an underlying error with the communications with the Bugzilla server.
 * 
 * @author Tom
 *
 */
public class BugzillaTransportException extends RuntimeException {
	/**
	 * Generated SUID.
	 */
	private static final long serialVersionUID = 5218998907447668998L;

	/**
	 * Creates a new {@link BugzillaTransportException} with the given message and cause.
	 * @param message A {@code String} describing the problem.
	 * @param cause A {@link Throwable} leading to this exception.
	 */
	public BugzillaTransportException(String message, Throwable cause) {
		super(message, cause);
	}

}

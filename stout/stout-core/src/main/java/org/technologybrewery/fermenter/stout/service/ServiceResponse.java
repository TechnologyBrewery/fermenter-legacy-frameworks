package org.technologybrewery.fermenter.stout.service;

import java.io.Serializable;

import org.technologybrewery.fermenter.stout.messages.Messages;

/**
 * Base class for service responses. Allows the ability to associate messages at
 * the service level.
 */
public abstract class ServiceResponse implements Serializable {

	private static final long serialVersionUID = -2752831251132027520L;

	protected Messages messages;

	protected ServiceResponse() {

	}

	protected ServiceResponse(Messages messages) {
		this.messages = messages;
	}

	/**
	 * Returns messages for this response.
	 * 
	 * @return messages
	 */
	public final Messages getMessages() {
		if (messages == null) {
			messages = new Messages();
		}
		return messages;
	}

	/**
	 * Indicates whether errors exist within the messages on this response (vice
	 * information messages).
	 * 
	 * @return true is has error messages
	 */
	public boolean hasErrors() {
		return getMessages().hasErrors();
	}

}

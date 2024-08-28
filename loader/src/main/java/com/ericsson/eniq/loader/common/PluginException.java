package com.ericsson.eniq.loader.common;

/**
 * PluginException an exception class for the plugins to use
 */
public class PluginException extends EngineBaseException {

	private final String message;

	public PluginException(final Throwable nestedException) {
		super();
		
		this.message = nestedException.getMessage();
		this.nestedException = nestedException;
	}

	public PluginException(final String message) {
		super(message);
		
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
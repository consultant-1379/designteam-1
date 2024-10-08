package com.ericsson.eniq.loader.common;

import com.ericsson.eniq.loader.sql.TransferActionBase;

/**
 * This class constructs different types of EngineExeptions.
 * EngineExeptionHandler is built to parse the information from the Exceptions
 * to show it user friendly.
 * 
 * original author Pekka Kaarela, modified to Dagger Engine project Jukka
 * Jaaheimo
 * 
 * @author $Author: raatikainen $
 * @since JDK1.1
 */

public class RemoveDataException extends EngineBaseException {

	/**
	 * Constructor with error message, non internationalised information and
	 * nested exception
	 * 
	 * @param message
	 *          error message
	 * @param params
	 *          parameters e.g. filename, path
	 * @param nestedException
	 *          Nested exception
	 * @param TransferActionBase
	 *          trActionBase
	 * @param String
	 *          methodName
	 */
	public RemoveDataException(final String message, final String[] params, final Throwable nestedException,
			final TransferActionBase trActionBase, final String methodName) {

		super(message);
		this.trActionBase = trActionBase;
		this.nestedException = nestedException;
		this.errorMessage = message;
		this.params = params;
		this.methodName = methodName;
		this.errorType = EngineConstants.ERR_TYPE_DEFINITION;

	}

	/**
	 * Constructor with error message, non internationalized information and
	 * nested exception
	 * 
	 * @param message
	 *          error message
	 * @param nestedException
	 *          Nested exception
	 * @param TransferActionBase
	 *          trActionBase
	 * @param String
	 *          methodName
	 */
	public RemoveDataException(final String message, final Throwable nestedException, final TransferActionBase trActionBase,
			final String methodName) {

		super(message);
		this.trActionBase = trActionBase;
		this.nestedException = nestedException;
		this.errorMessage = message;
		this.methodName = methodName;
		this.errorType = EngineConstants.ERR_TYPE_DEFINITION;

	}
}

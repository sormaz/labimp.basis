package edu.ohiou.mfgresearch.labimp.basis;
/**
 * class to throw exception when ImpXMlHanlder stack is not in sync with XML tags in XML file
 * @author sormaz
 *
 */
public class ImpHandlerException extends Exception {

	public ImpHandlerException() {
		// TODO Auto-generated constructor stub
	}

	public ImpHandlerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ImpHandlerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ImpHandlerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}

package com.function.ftp;

public class FtpException extends Exception {

	private static final long serialVersionUID = -4716854862747401246L;

	public FtpException(String error) {
		super(error);
	}

	public FtpException(Throwable e) {
		super(e);
	}

	public FtpException(String error, Throwable e) {
		super(error, e);
	}

}

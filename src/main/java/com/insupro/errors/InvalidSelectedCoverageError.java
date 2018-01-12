package com.insupro.errors;

public class InvalidSelectedCoverageError extends Exception {

	public InvalidSelectedCoverageError(float lowerLimit, float upperLimit) {
		super("Coverage should be in range [" + lowerLimit + "," + upperLimit + "]" );
	}
	
}

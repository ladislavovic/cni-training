package com.cross_ni.training.helper;

import com.google.common.base.Throwables;

public class Examples {

	/**
	 * Throws an {@link AssertionError} if the given lambda does not throw an exception or if
	 * the root cause of the exception is not the same type or subtype of the given class.
	 *
	 * @param expectedType lambda must throw an exception which root cause has this type. Can be also subtype.
	 * @param r lambda to run
	 */
	protected <T extends Throwable> T assertThrowsRootCause(Class<T> expectedType, Executable r) {
		return assertThrownExceptionCore(expectedType, true, r);
	}

	/**
	 * Throws an {@link AssertionError} if the given lambda does not throw an exception or if
	 * the exception is not the same type or subtype of the given class.
	 *
	 * @param expectedType lambda must throw an exception of this type. Can be also subtype.
	 * @param r lambda to run
	 */
	protected <T extends Throwable> T assertThrows(Class<T> expectedType, Executable r) {
		return assertThrownExceptionCore(expectedType, false, r);
	}

	private <T extends Throwable> T assertThrownExceptionCore(
			Class<T> clazz,
			boolean rootCause,
			Executable r) {
		try {
			r.run();
			throw new AssertionError("No exception was thrown.");
		} catch (Throwable e) {
			Throwable toCheck = rootCause ? Throwables.getRootCause(e) : e;
			if (!clazz.isAssignableFrom(toCheck.getClass())) {
				throw new AssertionError(String.format("An exception of type <%s> should be thrown but <%s> was thrown.",
																							 clazz.getName(), toCheck.getClass().getName()));
			}
			return (T) toCheck;
		}
	}
	
}

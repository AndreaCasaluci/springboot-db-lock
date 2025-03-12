package org.andrea.springbootdblock.exception.custom;

public class OptimisticLockFailureException extends RuntimeException {
    public OptimisticLockFailureException(String message) {
        super(message);
    }
}

package com.library.demo.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String notFoundUserMessageUsername) {
        super(notFoundUserMessageUsername);
    }
}

package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.error;

import org.springframework.http.HttpStatus;

/**
 * TODO.
 *
 * @param exceptionClass Exception type
 * @param status Response status
 */
record ExceptionRule(Class<?> exceptionClass, HttpStatus status){}
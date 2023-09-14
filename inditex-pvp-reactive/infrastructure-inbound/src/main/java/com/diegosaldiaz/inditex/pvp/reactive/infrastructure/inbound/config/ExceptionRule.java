package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.config;

import org.springframework.http.HttpStatus;

/**
 * ExceptionRule.
 *
 * @param exceptionClass Exception type
 * @param status Response status
 */
record ExceptionRule(Class<?> exceptionClass, HttpStatus status){}
package com.finalproject.backend;

/**
 * Jackson 3.x (Spring Boot 4.x) no longer has WRITE_DATES_AS_TIMESTAMPS
 * as a SerializationFeature.  LocalDateTime is serialised as ISO-8601
 * string by default — no extra configuration required.
 *
 * This file is intentionally left empty; kept as a reference note.
 */
public class JacksonConfig {
}
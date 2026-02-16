package org.javaguru.blacklist.dto;

public record ValidationError(
        String errorCode,
        String description
) {}
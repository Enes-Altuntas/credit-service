package org.colendi.infrastructure.controller.handler;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {

    private final String code;

    private final String message;

    private final List<String> fieldErrors;
}

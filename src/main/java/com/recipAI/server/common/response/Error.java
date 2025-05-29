package com.recipAI.server.common.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Error {
    private String field;
    private String message;
}

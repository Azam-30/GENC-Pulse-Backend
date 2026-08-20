package com.gencpulse.commit.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String status;

    private String message;

    private String timestamp;
}
package com.example.bechef.dto;

import com.example.bechef.status.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private ResponseStatus status; // 응답 상태를 나타내는 필드
    private String message; // 응답 메시지를 나타내는 필드
    private T data; // 응답 데이터를 나타내는 제네릭 타입 필드
}

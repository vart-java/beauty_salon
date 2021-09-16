package com.artuhin.sproject.model.dto.generic;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApiResponseWrapper<T> {

    private T payload;
    private ErrorDto errorDTO;

    public ApiResponseWrapper(T payload){
        this.payload = payload;
    }

    public ApiResponseWrapper(ErrorDto errorDTO){
        this.errorDTO = errorDTO;
    }
}

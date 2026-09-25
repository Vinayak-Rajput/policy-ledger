package com.hdfclife.ledger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;

    private String error;

    private String messsage;

    private Map<String, String> fields;

}

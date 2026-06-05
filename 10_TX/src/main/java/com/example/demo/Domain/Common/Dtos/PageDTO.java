package com.example.demo.Domain.Common.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

@AllArgsConstructor
public class PageDTO {
    private Integer pageNo;
    private Integer amount;
    private String keyword;
    private String type;

}

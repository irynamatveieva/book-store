package com.example.bookstore.dto.category;

import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    private String name;
    private String description;
}

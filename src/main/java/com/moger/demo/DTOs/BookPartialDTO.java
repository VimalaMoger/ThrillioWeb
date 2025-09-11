package com.moger.demo.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookPartialDTO {

    private String title;

    private String image_url;

    private int publicationYear;
}

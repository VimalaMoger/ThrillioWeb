package com.moger.demo.DTOs;

import com.moger.demo.dataConstants.BookGenre;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
public class BookDTO {

    private long id;

    private String title;

    private String image_url;

    private int publicationYear;

    private BookGenre genre;

    private double amazonRating;

    private List<String> publisherNames;

    private List<String> authorNames;

}

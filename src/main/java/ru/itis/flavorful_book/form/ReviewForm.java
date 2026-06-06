package ru.itis.flavorful_book.form;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class ReviewForm {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    @Size(max = 1000)
    private String comment;
}

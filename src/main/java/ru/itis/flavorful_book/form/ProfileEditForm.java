package ru.itis.flavorful_book.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileEditForm {

    @NotBlank
    @Size(min = 3, max = 40)
    private String username;

    private String avatarUrl;
}

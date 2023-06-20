package com.ust.onlineBookStore.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record PostRequestDto(
                            @NotNull(message = "Book isbn cannot be null")
                            @NotEmpty(message = "Book isbn cannot be empty")
                            @NotBlank(message = "Book isbn cannot be blank")
                            String isbn,
                             @NotNull(message = "Book title cannot be null")
                             @NotEmpty(message = "Book title cannot be empty")
                             @NotBlank(message = "Book title cannot be blank")
                             String title,
                             String seriesName, String author, Integer lexile,
                             Integer pageCount, Integer minAge, Integer maxAge,
                             String[] categories, String summary, String coverArtUrl,
                             String authorFirstName, String authorLastName, Integer copyright,
                             String publishedWorkId, String binding, String language,
                             double rating
) {
}

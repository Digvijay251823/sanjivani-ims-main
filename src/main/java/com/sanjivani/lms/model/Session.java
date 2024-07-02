package com.sanjivani.lms.model;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Pattern(regexp = "^[A-Z0-9]{3,10}$",
            message = "code must be of 3 to 10 length with no special characters")
    private String code;

    @NonNull
    private String name;

    private String description;

    @NonNull
    private String courseCode;

    private Integer durationInMinutes;
    private String  videoUrl;
    private String  presentationUrl;
    private String notesUrl;
    private String createdBy;
    @Setter(AccessLevel.NONE)
    private Date created;
    @Setter(AccessLevel.NONE)
    private Date modified;

}

package com.sanjivani.lms.model;

import java.util.Date;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Pattern(regexp = "^[A-Z0-9]{2,10}$",
            message = "code must be of 2 to 10 length with no special characters")
    private String code;

    @NonNull
    private String name;

    private String description;
    private String createdBy;
    @Setter(AccessLevel.NONE)
    private Date created;
    @Setter(AccessLevel.NONE)
    private Date modified;
}

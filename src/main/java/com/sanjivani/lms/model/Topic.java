package com.sanjivani.lms.model;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    private String name;
    private String description;
    private String createdBy;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}

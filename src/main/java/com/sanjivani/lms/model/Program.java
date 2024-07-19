package com.sanjivani.lms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

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
public class Program {
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    private String name;

    private String description;
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Long incharge = 0L;
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Long preacher = 0L;
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Long mentor = 0L;
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Long coordinator = 0L;
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Long sadhanaForm = 0L;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    @NonNull
    private String audienceType = "ALL";

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    @NonNull
    private String type = "TEMPLE";

    @NonNull
    private String location;

    private String createdBy;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}

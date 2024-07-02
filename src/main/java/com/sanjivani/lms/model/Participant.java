package com.sanjivani.lms.model;

import com.sanjivani.lms.enums.Gender;
import com.sanjivani.lms.enums.ParticipantStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits length with no special characters")
    private String waNumber;

    @NonNull
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits length with no special characters")
    private String contactNumber;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Invalid email")
    private String email;

    @Builder.Default
    @Min(value = 0, message = "The value must be positive")
    @NonNull
    private Integer japaRounds = 0;

    private List<Topic> interestedTopics;

    private Integer age;
    private Gender gender;
    private String address;
    private String city;
    private String maritalStatus;
    private String education;
    private String occupation;
    private String reference;
    private String notes;
    private ParticipantStatus status;
    private Integer numberOfChildren;

    private String createdBy;
    @Setter(AccessLevel.NONE)
    private Date created;
    @Setter(AccessLevel.NONE)
    private Date modified;

}

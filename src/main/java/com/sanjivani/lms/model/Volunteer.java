package com.sanjivani.lms.model;

import com.sanjivani.lms.enums.Gender;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Volunteer {

    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private String initiatedName;

    @NonNull
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits length with no special characters")
    private String waNumber;

    @NonNull
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits length with no special characters")
    private String contactNumber;

    @NonNull
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Invalid email")
    private String email;
    
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=<>?|{}()])(?=\\S+$).{8,20}$",
            message = "Password must contain at least one digit, one uppercase letter, one lowercase letter, " +
                    "one special character and at least 8 characters long and maximum of 20 characters long.")
    private String password;

    private Integer age;
    private Gender gender;
    private String address;
    private String serviceInterests;
    private String currentServices;
    private String createdBy;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}

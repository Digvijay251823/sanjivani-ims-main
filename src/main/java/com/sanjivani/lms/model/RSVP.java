package com.sanjivani.lms.model;

import com.sanjivani.lms.enums.RSVPOption;
import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RSVP {
    @NonNull
    private Long scheduledSessionId;
    @NonNull
    private Long participantId;
    @NonNull
    private Long levelId;
    @NonNull
    private Long programId;
    @NonNull
    private String scheduledSessionName;
    @NonNull
    private RSVPOption rsvp;
    @Setter(AccessLevel.NONE)
    private Date created;
    @Setter(AccessLevel.NONE)
    private Date modified;
}

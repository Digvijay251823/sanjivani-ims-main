package com.sanjivani.lms.model;

import java.util.Date;

import com.sanjivani.lms.enums.RSVPOption;

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
    
    private Long membersComming;
    @NonNull
    private RSVPOption rsvp;
    @Setter(AccessLevel.NONE)
    private Date created;
    @Setter(AccessLevel.NONE)
    private Date modified;
}

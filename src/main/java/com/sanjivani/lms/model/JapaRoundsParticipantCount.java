package com.sanjivani.lms.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JapaRoundsParticipantCount {
    @NonNull
    private String japaRounds;

    @NonNull
    private Integer participantCount;
}

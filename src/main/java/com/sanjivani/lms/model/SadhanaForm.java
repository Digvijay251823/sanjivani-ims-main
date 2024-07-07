package com.sanjivani.lms.model;
import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SadhanaForm {
    private Long id;

    @NonNull
    private Long programId;

    //Sadhana
    private Boolean numberOfRounds;
    private Boolean first8RoundsCompletedTime;
    private Boolean next8RoundsCompletedTime;
    private Boolean wakeUpTime;
    private Boolean sleepTime;
    private Boolean prabhupadaBookReading;
    private Boolean nonPrabhupadaBookReading;
    private Boolean prabhupadaClassHearing;
    private Boolean guruClassHearing;
    private Boolean otherClassHearing;
    private Boolean speaker;
    private Boolean attendedArti;
    private Boolean mobileInternetUsage;
    private Boolean topic;
    private Boolean visibleSadhana;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Setter(AccessLevel.NONE)
    private Date modified;
}

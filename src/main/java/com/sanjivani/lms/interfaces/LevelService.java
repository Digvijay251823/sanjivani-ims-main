package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.Level;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LevelService {
    Page<Level> getLevels(Pageable pageable);
    void save(Level level) throws IllegalArgumentException;

    Level getLevelById(Long id);
    Page<Level> getLevelsByProgram(String programName, Pageable pageable);
    Page<Level> getLevelsByPreacher1(Long preacher1, Pageable pageable);
    Page<Level> getLevelByPreacher2(Long preacher2, Pageable pageable);
    Page<Level> getLevelsByMentor(Long Mentor, Pageable pageable);
    Page<Level> getLevelsByCoordinator(Long coordinator, Pageable pageable);
    Page<Level> getLevelsByStatus(@NonNull String status, Pageable pageable);
    Page<Level> getLevelsByAcceptingNewParticipants(@NonNull Boolean acceptingNewParticipants, Pageable pageable);

}

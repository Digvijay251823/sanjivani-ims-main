package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.Program;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProgramService {
    Page<Program> getPrograms(Pageable pageable);
    void save(@NonNull Program program) throws IllegalArgumentException;

    void updateSadhanaForm(@NonNull Long id, @NonNull Long sadhanaFormId) throws IllegalArgumentException;

    Program getProgramById(@NonNull Long id);
    Program getProgramByName(@NonNull String name);

    Page<Program> getProgramsByIncharge(Long incharge, Pageable pageable);
    Page<Program> getProgramsByPreacher(Long preacher, Pageable pageable);
    Page<Program> getProgramsByMentor(Long Mentor, Pageable pageable);
    Page<Program> getProgramsByCoordinator(Long coordinator, Pageable pageable);
    Page<Program> getProgramsByLocation(String location, Pageable pageable);

}

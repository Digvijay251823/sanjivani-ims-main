package com.sanjivani.lms.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sanjivani.lms.model.SadhanaForm;

import lombok.NonNull;

public interface SadhanaFormService {
    Long save(SadhanaForm sadhanaForm) throws IllegalArgumentException;
    void update(SadhanaForm sadhanaForm) throws IllegalArgumentException;
    @NonNull
    Page<SadhanaForm> getSadhanaForm(@NonNull Pageable page);
    SadhanaForm getSadhanaFormById(@NonNull Long id);
}

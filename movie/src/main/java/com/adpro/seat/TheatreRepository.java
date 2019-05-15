package com.adpro.seat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheatreRepository extends JpaRepository<Theatre, Integer> {
    List<Theatre> findAllByOrderByDescriptionDesc();
}

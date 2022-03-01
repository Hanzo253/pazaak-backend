package com.capstone.pazaak.repository;

import com.capstone.pazaak.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    // find match by the user's id
    List<Match> findByUserId(Long userId);

    // find match by the user's id and match's id
    Match findByIdAndUserId(Long matchId, Long id);
}

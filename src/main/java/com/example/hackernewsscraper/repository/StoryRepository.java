package com.example.hackernewsscraper.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hackernewsscraper.model.Story;

public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findByTimestampAfter(LocalDateTime timestamp);

    Optional<Story> findByTitle(String title);
}

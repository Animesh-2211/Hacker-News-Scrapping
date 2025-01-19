package com.example.hackernewsscraper.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hackernewsscraper.model.Story;
import com.example.hackernewsscraper.repository.StoryRepository;

@RestController
@RequestMapping("/api")
public class StoryController {

    @Autowired
    private StoryRepository storyRepository;

    @GetMapping("/stories")
    public List<Story> getStories() {
        return storyRepository.findByTimestampAfter(LocalDateTime.now().minusMinutes(5));
    }
}
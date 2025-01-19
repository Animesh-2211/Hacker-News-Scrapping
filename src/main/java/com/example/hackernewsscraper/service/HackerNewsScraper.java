package com.example.hackernewsscraper.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.hackernewsscraper.model.Story;
import com.example.hackernewsscraper.repository.StoryRepository;

@Service
public class HackerNewsScraper {
    private static final Logger logger = LoggerFactory.getLogger(HackerNewsScraper.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private StoryRepository storyRepository;

    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void scrape() {
        logger.info("Scraping Hacker News...");
        try {
            // Step 1: Connect to Hacker News
            Document doc = Jsoup.connect("https://news.ycombinator.com/")
                    // .userAgent("Mozilla/5.0") // Mimic a browser
                    .timeout(10000) // 10-second timeout
                    .get();
            logger.info("Successfully connected to Hacker News.");

            // Step 2: Extract stories
            Elements stories = doc.select("span.titleline > a");
            logger.info("Found {} stories on the page.", stories.size());

            if (stories.isEmpty()) {
                logger.warn("No stories found on the page. Check the HTML structure or website changes.");
                return;
            }

            // Step 3: Process each story
            for (Element story : stories) {
                String title = story.text();
                String url = story.attr("href");
                logger.info("Processing story: Title = {}, URL = {}", title, url);

                // Step 4: Check if the story already exists in the database
                Optional<Story> existingStory = storyRepository.findByTitle(title);
                if (existingStory.isEmpty()) {
                    // Step 5: Save new story to the database
                    Story newStory = new Story();
                    newStory.setTitle(title);
                    newStory.setUrl(url);
                    newStory.setTimestamp(LocalDateTime.now());
                    storyRepository.save(newStory);

                    logger.info("Saved new story to the database: Title = {}", title);

                    // Step 6: Broadcast new story via WebSocket
                    broadcastNewStory(newStory);
                } else {
                    logger.info("Story already exists in the database: Title = {}", title);
                }
            }
        } catch (IOException e) {
            logger.error("Error scraping Hacker News: {}", e.getMessage(), e);
        }
    }

    private void broadcastNewStory(Story story) {
        logger.info("Broadcasting new story: Title = {}", story.getTitle());
        template.convertAndSend("/topic/newStories", story);
    }
}
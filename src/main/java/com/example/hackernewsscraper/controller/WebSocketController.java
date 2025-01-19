package com.example.hackernewsscraper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.hackernewsscraper.model.Story;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/newStory")
    @SendTo("/topic/newStories")
    public Story broadcastNewStory(Story story) {
        return story;
    }

}

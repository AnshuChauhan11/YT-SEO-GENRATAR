package com.YtTool.dto;

import lombok.Data;

import java.util.List;

@Data
public class Snippet {
    private String title;
    private String description;
    private String channelTitle;
    private String publishedAt;
    private Thumbnails thumbnails;

    private List<String> tags;
}


package com.YtTool.dto;

import lombok.Data;

import java.util.List;

@Data
public class VideoApiResponse {
    private List<VideoItem> items;
}


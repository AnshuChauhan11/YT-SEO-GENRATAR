package com.YtTool.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetails {
    private String id;
    private String title;
    private String channelTitle;
    private String description;
    private String publishedAt;
    private String thumbnailUrl;
    private List<String> tags;
}

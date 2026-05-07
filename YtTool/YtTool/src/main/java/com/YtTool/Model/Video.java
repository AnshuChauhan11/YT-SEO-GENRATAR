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
public class Video {
    private String id;

    // use canonical property names expected by templates/controllers
    private String channelTitle;
    private String title;

    private List<String> tags;

    // helper used by the Thymeleaf template to copy tags as a single string
    public String getTagsAsString() {
        if (tags == null || tags.isEmpty()) return "";
        return String.join(",", tags);
    }
}

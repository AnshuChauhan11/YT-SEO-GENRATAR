package com.YtTool.dto;

import lombok.Data;

@Data
public class Thumbnails {
    private Thumbnail maxres;
    private Thumbnail high;
    private Thumbnail medium;

    private Thumbnail _default;

    public String getBestThumbnailUrl(){
        if (maxres != null && maxres.getUrl() != null) return maxres.getUrl();
        if (high != null && high.getUrl() != null) return high.getUrl();
        if (medium != null && medium.getUrl() != null) return medium.getUrl();
        return _default != null && _default.getUrl() != null ? _default.getUrl() : "";
    }
}


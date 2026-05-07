package com.YtTool.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchApiResponse {
    private List<SearchItem> items;
}


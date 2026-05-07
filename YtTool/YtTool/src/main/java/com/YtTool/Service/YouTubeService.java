package com.YtTool.Service;

import com.YtTool.Model.SearchVideo;
import com.YtTool.Model.Video;
import com.YtTool.Model.VideoDetails;
import com.YtTool.dto.SearchApiResponse;
import com.YtTool.dto.SearchItem;
import com.YtTool.dto.Snippet;
import com.YtTool.dto.VideoApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final WebClient.Builder webClientBuilder;

    @Value("${youtube.api.key}")
    private String apikey;

    @Value("${youtube.api.base.url}")
    private String baseUrl;

    @Value("${youtube.api.max.related.videos}")
    private int maxRelatedVideos;

    public SearchVideo searchVideos(String videoTitle) {

        List<String> videoIds=searchForVideoIds(videoTitle);

        if (videoIds.isEmpty()){
            return SearchVideo.builder()
                    .primaryVideo(null)
                    .relatedVideo(null)
                    .build() ;
        }

        String primaryVideoId=videoIds.get(0);

        List<String> relatedVideoId=videoIds.subList(1,Math.min(videoIds.size(),maxRelatedVideos));

        // store in video class
        Video primaryVideo=getVideoById(primaryVideoId);

        List<Video> relatedVideo=new ArrayList<>();

        for(String id:relatedVideoId){
            Video video=getVideoById(id);

            if (video!=null){
                relatedVideo.add(video);
            }
        }

        return  SearchVideo.builder()
                .primaryVideo(primaryVideo)
                .relatedVideo(relatedVideo)
                .build();
    }

    // method of get video by id
    private Video getVideoById(String videoId) {
        VideoApiResponse response=webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part","snippet")
                        .queryParam("id",videoId)
                        .queryParam("key",apikey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response==null || response.getItems()==null){
            return  null;
        }
        Snippet snippet = response.getItems().get(0).getSnippet();

        return Video.builder()
                .id(videoId)
                .channelTitle(snippet.getChannelTitle())
                .title(snippet.getTitle())
                .tags(snippet.getTags() == null ? Collections.emptyList() : snippet.getTags())
                .build();
    }

    private List<String> searchForVideoIds(String videoTitle) {
        SearchApiResponse response=webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part","snippet")
                        .queryParam("q",videoTitle)
                        .queryParam("type","video")
                        .queryParam("maxResults", maxRelatedVideos)
                        .queryParam("key",apikey)
                        .build())
                .retrieve()
                .bodyToMono(SearchApiResponse.class)
                .block();
        
    if (response==null || response.getItems()==null){
        return Collections.emptyList();
    }

    List<String> videoIds=new ArrayList<>();
    for (SearchItem item: response.getItems()){
        if (item != null && item.getId() != null && item.getId().getVideoId() != null) {
            videoIds.add(item.getId().getVideoId());
        }
    }


        return videoIds;
    }

    public VideoDetails getVideoDetails(String videoId) {
        VideoApiResponse response=webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part","snippet")
                        .queryParam("id",videoId)
                        .queryParam("key",apikey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response==null || response.getItems()==null || response.getItems().isEmpty()){
            return  null;
        }

        Snippet snippet = response.getItems().get(0).getSnippet();

        return VideoDetails.builder()
                .id(videoId)
                .title(snippet.getTitle())
                .channelTitle(snippet.getChannelTitle())
                .description(snippet.getDescription())
                .publishedAt(snippet.getPublishedAt())
                .thumbnailUrl(snippet.getThumbnails() == null ? "" : snippet.getThumbnails().getBestThumbnailUrl())
                .tags(snippet.getTags()==null ? Collections.emptyList() : snippet.getTags())
                .build();
    }



}

package com.YtTool.Controller;

import com.YtTool.Model.VideoDetails;
import com.YtTool.Service.ThumbnailService;
import com.YtTool.Service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class YouTubeVideoController {

    private  final YouTubeService youTubeService;
    private  final ThumbnailService service;

    @GetMapping("youtube/video-details")
    public  String showVideoFrom(){
        return "fragments/video-details";
    }

    @PostMapping("youtube/video-details")
    public String fetchVideoDetails(@RequestParam String videoUrlOrId, Model model){
        String videoId=service.extratVideoId(videoUrlOrId);

            if (videoId==null) {
                model.addAttribute("error", "Invalid YouTube URL");
                return "fragments/video-details";
            }

            VideoDetails details=youTubeService.getVideoDetails(videoId);

            if (details==null){
                model.addAttribute("error","Unable to fetch video details. Please check the video URL or ID.");
                return "fragments/video-details";
            }

            model.addAttribute("videoDetails",details);
            return "fragments/video-details";
    }

}

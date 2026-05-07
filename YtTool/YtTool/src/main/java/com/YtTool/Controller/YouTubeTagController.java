package com.YtTool.Controller;

import com.YtTool.Model.SearchVideo;
import com.YtTool.Service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/youtube")
public class YouTubeTagController {

    @Value("${youtube.api.key}")
    private  String apiKey;

    @Autowired
    private YouTubeService youTubeService ;


    private boolean isApiKeyConfigured(){
        if (apiKey==null || apiKey.isEmpty()){
            return  false;
        }
        return true;

        // THIS IS ONE LINE SYNTAX OF ABOVE CODE
//        return  apiKey!=null && !apiKey.isEmpty() ;
    }

    @PostMapping("/search")
    public String videoTag(@RequestParam ("videoTitle") String videoTitle, Model model){

        // CHECK API ARE CONFIGURED OR NOT
        if (!isApiKeyConfigured()){
            model.addAttribute("error","Api Key is Not Configured ! ");

            return  "fragments/home";
        }



        if (videoTitle==null || videoTitle.isEmpty()) {
            model.addAttribute("error", "Video Tittle is Required");

            return "fragments/home";
        }


        try {
            SearchVideo result=youTubeService.searchVideos(videoTitle);
            model.addAttribute("primaryVideo", result.getPrimaryVideo());
            model.addAttribute("relatedVideos", result.getRelatedVideo());

            // Build a single string of all tags (primary + related) to support the "Copy All" button
            StringBuilder allTags = new StringBuilder();
            if (result.getPrimaryVideo() != null && result.getPrimaryVideo().getTags() != null) {
                result.getPrimaryVideo().getTags().forEach(t -> {
                    if (allTags.length() > 0) allTags.append(",");
                    allTags.append(t);
                });
            }
            if (result.getRelatedVideo() != null) {
                for (var v : result.getRelatedVideo()) {
                    if (v != null && v.getTags() != null) {
                        for (var t : v.getTags()) {
                            if (allTags.length() > 0) allTags.append(",");
                            allTags.append(t);
                        }
                    }
                }
            }
            model.addAttribute("allTagsAsString", allTags.toString());
            return "fragments/home";
        }catch (Exception e){
           model.addAttribute("error",e.getMessage());
           return "fragments/home";
        }



    }
}

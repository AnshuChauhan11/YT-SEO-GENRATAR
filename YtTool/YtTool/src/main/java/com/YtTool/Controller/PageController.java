package com.YtTool.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/","home"})
    public String home(){
        return "fragments/home";
    }

    @GetMapping("/video-details")
    public String videoDetails(){
        return "fragments/video-details";
    }

}

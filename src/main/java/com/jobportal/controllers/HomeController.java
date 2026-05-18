package com.jobportal.controllers;

import com.jobportal.entity.JobPostActivity;
import com.jobportal.services.JobPostActivityService;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final JobPostActivityService jobPostActivityService;

    public HomeController(JobPostActivityService jobPostActivityService) {
        this.jobPostActivityService = jobPostActivityService;
    }

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(value = "job", required = false) String job,
                       @RequestParam(value = "location", required = false) String location,
                       @RequestParam(value = "category", required = false) String category) {
        List<JobPostActivity> jobPost;
        if (StringUtils.hasText(job) || StringUtils.hasText(location) || StringUtils.hasText(category)) {
            List<String> typeList = (StringUtils.hasText(category) && !category.equalsIgnoreCase("all")) 
                    ? Arrays.asList(category) 
                    : Arrays.asList("Part-Time", "Full-Time", "Freelance", "InternShip", "Internship");
            
            jobPost = jobPostActivityService.search(job, location,
                    typeList,
                    Arrays.asList("Remote-Only", "Office-Only", "Partial-Remote"),
                    null);
        } else {
            jobPost = jobPostActivityService.getAll();
        }

        model.addAttribute("job", job);
        model.addAttribute("location", location);
        model.addAttribute("category", category);
        model.addAttribute("jobPost", jobPost);
        return "index";
    }
}

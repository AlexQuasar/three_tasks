package com.alexquasar.threeTasks.thirdTask.web.input;

import com.alexquasar.threeTasks.thirdTask.service.UrlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/urlController")
public class UrlRestController {

    private UrlService urlService;

    public UrlRestController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/getDuplicates")
    public List<String> getDuplicates() {
        return urlService.getDuplicatesUrls();
    }
}

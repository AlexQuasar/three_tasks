package com.alexquasar.threeTasks.thirdTask.web.input;

import com.alexquasar.threeTasks.thirdTask.entity.Url;
import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicates;
import com.alexquasar.threeTasks.thirdTask.service.UrlService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/urlController")
public class UrlRestController {

    private UrlService urlService;

    public UrlRestController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("addUrl")
    public String addUrl(@RequestBody String link) {
        urlService.addUrl(link);
        return "added";
    }

    @PostMapping("addUrls")
    public String addUrls(@RequestBody List<String> links) {
        urlService.addUrls(links);
        return "added";
    }

    @GetMapping("/getDuplicates")
    public List<UrlDuplicates> getDuplicates() {
        return urlService.getDuplicatesUrls();
    }
}

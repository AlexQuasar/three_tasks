package com.alexquasar.threeTasks.thirdTask.web.input;

import com.alexquasar.threeTasks.thirdTask.entity.UrlDuplicate;
import com.alexquasar.threeTasks.thirdTask.exception.ServiceException;
import com.alexquasar.threeTasks.thirdTask.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/urlController")
public class UrlRestController {

    private UrlService urlService;

    public UrlRestController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/addUrl")
    public String addUrl(@RequestBody String link) {
        urlService.addUrl(link);
        return "added";
    }

    @PostMapping("/addUrls")
    public String addUrls(@RequestBody List<String> links) {
        urlService.addUrls(links);
        return "added";
    }

    @GetMapping("/getDuplicates")
    public List<UrlDuplicate> getDuplicates() {
        return urlService.getDuplicatesUrls();
    }

    @GetMapping("/getDuplicatesLinks")
    public Set<String> getDuplicatesLinks( @RequestBody List<String> links) {
        try {
            return urlService.getDuplicatesLinks(links);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }
}

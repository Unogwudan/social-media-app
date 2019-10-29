package com.unogwudan.socialmedia.restcontrollers;

import com.unogwudan.socialmedia.models.Chapter;
import com.unogwudan.socialmedia.services.dao.ChapterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chapters")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping
    public Flux<Chapter> getChapters() {
        return chapterService.getAllChapters();
    }
}

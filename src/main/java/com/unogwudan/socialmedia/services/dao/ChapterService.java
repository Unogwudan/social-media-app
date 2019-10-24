package com.unogwudan.socialmedia.services.dao;

import com.unogwudan.socialmedia.models.Chapter;
import com.unogwudan.socialmedia.repositories.ChapterRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;


    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public Flux<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }
}

package com.unogwudan.socialmedia.configs;

import com.unogwudan.socialmedia.models.Chapter;
import com.unogwudan.socialmedia.repositories.ChapterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner init(ChapterRepository chapterRepository) {
        return args -> Flux.just(
                new Chapter("Chapter 1"),
                new Chapter("Chapter 1"),
                new Chapter("Chapter 1"),
                new Chapter("Chapter 1"),
                new Chapter("Chapter 1"))
                .flatMap(chapterRepository::save)
                .subscribe(System.out::println);
    }
}

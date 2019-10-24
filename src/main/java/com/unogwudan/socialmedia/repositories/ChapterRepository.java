package com.unogwudan.socialmedia.repositories;

import com.unogwudan.socialmedia.models.Chapter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository extends ReactiveCrudRepository<Chapter, String> {

}

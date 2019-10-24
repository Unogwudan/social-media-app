package com.unogwudan.socialmedia.controllers;

import com.unogwudan.socialmedia.models.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/images")
public class ImageController {

    Logger logger = LogManager.getLogger(ImageController.class);

    @GetMapping
    public Flux<Image> getImages() {
        return Flux.just(
                new Image("1", "image1.jpg"),
                new Image("2", "image2.jpg"),
                new Image("3", "image3.jpg"));
    }

    @PostMapping
    public Mono<Void> saveImages(@RequestBody Flux<Image> images) {
        return images.map(image -> {
            logger.info("We will save " + image.name + " to reactive database soon!");
            return image;
        }).then();
    }
}

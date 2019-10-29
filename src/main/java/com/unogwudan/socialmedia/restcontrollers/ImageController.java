package com.unogwudan.socialmedia.restcontrollers;

import com.unogwudan.socialmedia.models.Image;
import com.unogwudan.socialmedia.services.dao.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping(value = "/images")
public class ImageController {

    Logger logger = LogManager.getLogger(ImageController.class);
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value="/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> getImage(@PathVariable String filename) {
        return imageService.findOneImage(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok().contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    }catch (IOException e) {
                        e.printStackTrace();
                        return ResponseEntity.badRequest().body("File not found!!!");
                    }
                });
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> createImage(@RequestPart(name = "file") Flux<FilePart> files) {
        return imageService.createImage(files).then(Mono.just(new ResponseEntity<>("Saved successfully", HttpStatus.CREATED)));
    }

    @DeleteMapping(value = "/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> deleteImage(@PathVariable String filename) {
        return imageService.deleteImage(filename).then(Mono.just(new ResponseEntity<>("Deleted successfully", HttpStatus.OK)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Image> getAllImages() {
        return imageService.getAllImages();
    }
}

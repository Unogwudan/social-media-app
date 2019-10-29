package com.unogwudan.socialmedia.mvccontrollers;

import com.unogwudan.socialmedia.services.dao.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
public class ImageMVCController {
    private static final String BASE_PATH = "/images/image/";
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;

    public ImageMVCController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = BASE_PATH + FILENAME, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> getImage(@PathVariable String filename) {
        return imageService.findOneImage(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok()
                                .contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return ResponseEntity.badRequest()
                                .body("Couldn't find " + filename + "==> " + e.getMessage());
                    }
                });
    }

    @PostMapping(value = BASE_PATH)
    public Mono<String> createImage(@RequestPart(value = "file") Flux<FilePart> files) {
        return imageService.createImage(files).then(Mono.just("redirect:/"));
    }

    @DeleteMapping(value = BASE_PATH + FILENAME)
    public Mono<String> deleteImage(@PathVariable String filename) {
        return imageService.deleteImage(filename).then(Mono.just("redirect:/"));
    }

    @GetMapping("/")
    public Mono<String> getAllImages(Model model) {
        model.addAttribute("images", imageService.getAllImages());
        return Mono.just("index");
    }
}

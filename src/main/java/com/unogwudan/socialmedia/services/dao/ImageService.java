package com.unogwudan.socialmedia.services.dao;

import com.unogwudan.socialmedia.models.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {
    private static String UPLOAD_ROOT = "/home/unogwudan/social-media-uploads/";
    private final ResourceLoader resourceLoader;

    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

   @Bean
    CommandLineRunner setUp() {
        return (args -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test File", new FileWriter(UPLOAD_ROOT + "test-image.jpg"));
            FileCopyUtils.copy("Test File 2", new FileWriter(UPLOAD_ROOT + "test-image2.jpg"));
        });
   }

   public Flux<Image> getAllImages() {
       try {
           return Flux.fromIterable(Files.newDirectoryStream(Paths.get(UPLOAD_ROOT)))
                   .map(path -> new Image(String.valueOf(path.hashCode()), path.getFileName().toString()));
       } catch (IOException e) {
           e.printStackTrace();
           return Flux.empty();
       }
   }

   public Mono<Resource> findOneImage(String filename) {
        return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + UPLOAD_ROOT + filename));
   }

   public Mono<?> createImage(Flux<FilePart> files) {
        return files.flatMap(file -> file.transferTo(Paths.get(UPLOAD_ROOT, file.filename()).toFile())).then();
   }

   public Mono<Void> deleteImage(String filename) {
        return Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

   }
}

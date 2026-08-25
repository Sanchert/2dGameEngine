package org.example.keenmarksmanfx;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ImageLoader {
    public static Image loadImageFromPath(String pathToFile) {
        Path path = Paths.get(pathToFile);
        try {
            byte[] bytes = Files.readAllBytes(path);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return new Image(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

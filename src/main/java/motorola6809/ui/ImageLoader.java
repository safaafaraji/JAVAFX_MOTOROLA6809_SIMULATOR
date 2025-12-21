package motorola6809.ui;

import javafx.scene.image.Image;
import java.io.InputStream;

public class ImageLoader {
    
    public static Image loadImage(String resourcePath) {
        try {
            // Essayer depuis le classpath
            InputStream is = ImageLoader.class.getResourceAsStream(resourcePath);
            if (is != null) {
                return new Image(is);
            }
            
            // Essayer depuis le système de fichiers
            is = ImageLoader.class.getResourceAsStream("/" + resourcePath);
            if (is != null) {
                return new Image(is);
            }
            
            // Essayer chemin relatif
            if (!resourcePath.startsWith("/")) {
                is = ImageLoader.class.getResourceAsStream("/" + resourcePath);
                if (is != null) {
                    return new Image(is);
                }
            }
            
            System.err.println("Image non trouvée: " + resourcePath);
            return null;
            
        } catch (Exception e) {
            System.err.println("Erreur chargement image " + resourcePath + ": " + e.getMessage());
            return null;
        }
    }
}
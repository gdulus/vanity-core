package vanity.image.utils

import org.springframework.web.multipart.MultipartFile

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

final class ImageWrapper {

    private final MultipartFile imageFile

    ImageWrapper(MultipartFile image) {
        this.imageFile = image
    }

    public boolean isEmpty(){
        return imageFile.isEmpty()
    }

    public String getName(){
        return imageFile.getOriginalFilename()
    }

    public Dimension getDimension(){
        if (isEmpty()){
            return Dimension.NULL_DIMENSION
        }

        BufferedImage image = ImageIO.read(imageFile.inputStream)
        return new Dimension(image.width, image.height)
    }

    public static final class Dimension {

        public static final Dimension NULL_DIMENSION = new Dimension(0, 0)

        final int width

        final int height

        Dimension(int width, int height) {
            this.width = width
            this.height = height
        }
    }
}

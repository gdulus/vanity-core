package vanity.media

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import vanity.celebrity.Celebrity

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageService {

    static transactional = false

    @Value('${files.images.dir}')
    private String imagesDir

    @Value('${images.vip.subdir}')
    private String celebritySubDir

    @Value('${images.width.max}')
    private Integer imageMaxWidth

    @Value('${images.height.max}')
    private Integer imageMaxHeight

    public String store(final Celebrity celebrity, final MultipartFile file) {
        if (!isSupportedContentType(file.contentType)) {
            throw new ImageInvalidFormatException(file.contentType)
        }

        BufferedImage image = ImageIO.read(file.getInputStream())

        if (!isSupportedSize(image)) {
            throw new ImageInvalidSizeException(image.width, image.height, imageMaxWidth, imageMaxHeight)
        }

        String fileExtension = getFileExtension(file)
        String fileName = getCelebrityFileName(celebrity.id, fileExtension)
        String fullPath = "${celebrityImagesDir}/${fileName}"
        File out = new File(fullPath)
        ImageIO.write(image, fileExtension, out)
        return fileName
    }

    private String getCelebrityImagesDir() {
        return "${imagesDir}/${celebritySubDir}"
    }

    private String getCelebrityFileName(final Long celebrityId, final String fileExtension) {
        return "${celebrityId}-${UUID.randomUUID()}.${fileExtension}"
    }

    private String getFileExtension(final MultipartFile file) {
        return contentTypeMapping[file.contentType]
    }

    private boolean isSupportedContentType(final String contentType) {
        return contentTypeMapping[contentType]
    }

    private boolean isSupportedSize(final BufferedImage image) {
        image.width <= imageMaxWidth && image.height <= imageMaxWidth
    }

    private Map<String, String> getContentTypeMapping() {
        return ['image/png': 'png', 'image/jpg': 'jpg', 'image/jpeg': 'jpg', 'image/pjpeg': 'jpg']
    }

}

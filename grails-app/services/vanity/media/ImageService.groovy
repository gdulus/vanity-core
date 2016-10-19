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

    public void validateImage(final Integer width, final Integer height, final String contentType) {
        if (width > imageMaxWidth || height > imageMaxWidth) {
            throw new ImageSavingException(width, height, imageMaxWidth, imageMaxHeight)
        }

        if (!isSupportedContentType(contentType)) {
            throw new ImageSavingException(contentType)
        }
    }

    public String store(final Celebrity celebrity, final MultipartFile file) {
        BufferedImage image = ImageIO.read(file.getInputStream())
        validateImage(image.getWidth(), image.getHeight(), file.contentType)
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

    private Map<String, String> getContentTypeMapping() {
        return ['image/png': 'png', 'image/jpg': 'jpg', 'image/jpeg': 'jpg', 'image/pjpeg': 'jpg']
    }

}

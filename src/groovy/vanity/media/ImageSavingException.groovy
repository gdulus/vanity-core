package vanity.media

class ImageSavingException extends RuntimeException {

    ImageSavingException(final Integer width, final Integer height, final Integer maxWidth, final Integer maxHeight) {
        super("Invalid image dimension: width ${width} (max: ${maxWidth}), height: ${height} (max: ${maxHeight})")
    }

    ImageSavingException(final String contentType) {
        super("Unsuported content type: ${contentType}")
    }
}

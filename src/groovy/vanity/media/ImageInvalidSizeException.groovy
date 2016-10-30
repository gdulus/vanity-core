package vanity.media

class ImageInvalidSizeException extends ImageSavingException {

    ImageInvalidSizeException(final Integer width, final Integer height, final Integer maxWidth, final Integer maxHeight) {
        super("Invalid image dimension: width ${width} (max: ${maxWidth}), height: ${height} (max: ${maxHeight})")
    }
}

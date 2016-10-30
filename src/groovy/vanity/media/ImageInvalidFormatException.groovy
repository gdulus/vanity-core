package vanity.media

class ImageInvalidFormatException extends ImageSavingException {

    ImageInvalidFormatException(final String contentType) {
        super("Unsuported content type: ${contentType}")
    }
}

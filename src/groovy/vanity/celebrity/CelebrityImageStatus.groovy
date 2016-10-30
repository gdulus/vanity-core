package vanity.celebrity

class CelebrityImageStatus {

    public static final Integer UPLOADED = 1

    public static final Integer REVIEWED = 2

    public static final Integer DELETED = 3

    public static boolean isSupported(final Integer status) {
        return [UPLOADED, REVIEWED, DELETED].contains(status)
    }

}

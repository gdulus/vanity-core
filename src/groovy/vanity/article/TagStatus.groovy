package vanity.article

public enum TagStatus {

    PUBLISHED,

    TO_BE_REVIEWED,

    PROMOTED,

    SPAM

    public static final List<TagStatus> OPEN_STATUSES = [PUBLISHED, PROMOTED].asImmutable()

    public static final List<TagStatus> CLOSED_STATUSES = [TO_BE_REVIEWED, SPAM].asImmutable()

}


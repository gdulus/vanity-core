package vanity.article

public enum Status {

    PUBLISHED,
    TO_BE_REVIEWED,
    PROMOTED

    public static final List<Status> OPEN_STATUSES = [PUBLISHED, PROMOTED].asImmutable()

    public static final List<Status> CLOSED_STATUSES = [TO_BE_REVIEWED].asImmutable()
}


package vanity.article

import groovy.transform.Immutable

@Immutable
public final class Status {

    public enum Tag {

        PUBLISHED,

        TO_BE_REVIEWED,

        PROMOTED

        public static final List<Status> OPEN_STATUSES = [PUBLISHED, PROMOTED].asImmutable()

        public static final List<Status> CLOSED_STATUSES = [TO_BE_REVIEWED].asImmutable()
    }

}


package vanity.user

final class Authority {

    public static final String ROLE_PORTAL_USER = 'ROLE_PORTAL_USER'

    public static final String ROLE_ADMIN = 'ROLE_ADMIN'

    public static final String ROLE_REVIEWER = 'ROLE_REVIEWER'

    public static final Set<String> ALL = [ROLE_ADMIN, ROLE_REVIEWER, ROLE_PORTAL_USER].asImmutable() as Set

    private Authority() {
    }
}

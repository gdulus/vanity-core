package vanity.user

class User {

    String username

    String password

    Profile profile

    boolean enabled = true

    boolean accountExpired

    boolean accountLocked

    boolean passwordExpired

    static constraints = {
        username blank: false, nullable: false, unique: true
        password blank: false, nullable: false
        profile nullable: true
    }

    static mapping = {
        version false
        table 'vanity_user'
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }
}

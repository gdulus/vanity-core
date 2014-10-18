package vanity.user

class User {

    String username

    String password

    boolean enabled = true

    boolean accountExpired

    boolean accountLocked

    boolean passwordExpired

    static constraints = {
        username blank: false, unique: true
        password blank: false
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

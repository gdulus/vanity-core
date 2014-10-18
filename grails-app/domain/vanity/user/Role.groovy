package vanity.user

class Role {

    String authority

    static mapping = {
        version false
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
    }
}

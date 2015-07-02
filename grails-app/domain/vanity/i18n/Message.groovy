package vanity.i18n

class Message {

    String code

    Locale locale

    String text

    static constraints = {
        code(nullable: false, blank: false)
        locale(nullable: false, blank: false)
        text(nullable: false, blank: false)
    }

    static mapping = {
        text type: "text"
    }
}


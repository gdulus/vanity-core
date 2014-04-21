package vanity.article

class ContentSource {

    Target target

    Integer priority = 0

    Boolean disabled = false

    static constraints = {
        target(nullable:false, unique: true)
        priority(nullable:false)
    }

    static mapping = {
        version false
        target index: 'content_source_target_idx'
    }

    public static enum Target {

        PUDELEK('http://www.pudelek.pl/'),
        FAKT('http://www.fakt.pl/')

        final String address

        Target(String address) {
            this.address = address
        }

        @Override
        public String toString() {
            return this.name()
        }
    }

}

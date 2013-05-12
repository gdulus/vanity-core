package vanity.article

class ContentSource {

    Target target

    Integer priority = 0

    static constraints = {
        target(nullable:false, unique: true)
        priority(nullable:false)
    }

    public static enum Target {

        PUDELEK('http://www.pudelek.pl/')

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

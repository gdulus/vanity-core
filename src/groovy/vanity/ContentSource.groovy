package vanity

public enum ContentSource {

    PUDELEK('www.pudelek.pl')

    final String address

    ContentSource(String address) {
        this.address = address
    }
}
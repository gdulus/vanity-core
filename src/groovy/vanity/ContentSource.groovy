package vanity

public enum ContentSource {

    PUDELEK('http://www.pudelek.pl/')

    final String address

    ContentSource(String address) {
        this.address = address
    }

    public String getTriggerName(){
        return this.name()
    }

    @Override
    public String toString() {
        return this.name()
    }
}
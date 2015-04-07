package vanity.celebrity

class Job implements Comparable<Job> {

    String name

    Date dateCreated

    Date lastUpdated

    static constraints = {
        name(nullable: false, blank: false, maxSize: 250, unique: true)
    }

    @Override
    int compareTo(Job o) {
        return name.compareTo(o.name)
    }
}

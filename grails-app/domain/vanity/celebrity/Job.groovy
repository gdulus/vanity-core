package vanity.celebrity

import vanity.i18n.TranslationKeyAware

class Job implements Comparable<Job>, TranslationKeyAware {

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

    @Override
    String getTranslationKey() {
        return name.toLowerCase().replaceAll(' ', '_')
    }
}

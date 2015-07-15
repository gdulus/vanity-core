package vanity.location

import vanity.i18n.TranslationKeyAware

class Country implements Comparable<Country>, TranslationKeyAware {

    String isoCode

    String name

    Date dateCreated

    Date lastUpdated

    static constraints = {
        name(nullable: false, blank: false, maxSize: 250)
        isoCode(nullable: false, blank: false, maxSize: 10, unique: true)
    }

    @Override
    int compareTo(Country o) {
        return isoCode.compareTo(o.isoCode)
    }

    @Override
    String getTranslationKey() {
        return isoCode
    }
}

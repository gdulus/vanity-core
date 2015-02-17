package vanity.user

import groovy.util.logging.Slf4j

@Slf4j
enum Gender {

    MAN,
    WOMAN

    public static Gender parseStr(final String value) {
        if (!value) {
            return null
        }

        try {
            return Gender.valueOf(value)
        } catch (Throwable exp) {
            log.error("Can't parse value = '${value}'", exp)
            return null
        }
    }

}

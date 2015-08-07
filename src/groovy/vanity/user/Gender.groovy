package vanity.user

import groovy.util.logging.Slf4j
import org.springframework.context.MessageSourceResolvable

@Slf4j
enum Gender implements MessageSourceResolvable {

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

    @Override
    String[] getCodes() {
        ["${getClass().name}.${name()}"] as String[]
    }

    @Override
    Object[] getArguments() {
        return new Object[0]
    }

    @Override
    String getDefaultMessage() {
        return name()
    }

}

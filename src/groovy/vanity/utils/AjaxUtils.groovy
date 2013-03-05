package vanity.utils

import grails.converters.JSON
import org.springframework.validation.Errors
import org.springframework.validation.FieldError

class AjaxUtils {

    public static class Const {

        private static final Map<String, String> SUCCESS_RESPONSE_BASE = [status:'success']

        private static final Map<String, String> ERROR_RESPONSE_BASE = [status:'error']

        public static final JSON SUCCESS_RESPONSE = SUCCESS_RESPONSE_BASE as JSON

        public static final JSON ERROR_RESPONSE = ERROR_RESPONSE_BASE as JSON

    }

    public static JSON renderErrors(final Errors errors){
        final Map<String, String> serializedErrors = errors.fieldErrors.inject([:], {Map<String, String> map, FieldError fieldError ->
            map[fieldError.field] = fieldError.code
            return map
        })

        return ([:] + Const.ERROR_RESPONSE_BASE + [errors:serializedErrors]) as JSON
    }

}

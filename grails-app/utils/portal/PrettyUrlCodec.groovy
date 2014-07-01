package portal

class PrettyUrlCodec {

    private static final Map<String, String> REPLACEMENT_MAP = [
        'ą': 'a',
        'ę': 'e',
        'ł': 'l',
        'ś': 's',
        'ń': 'n',
        'ć': 'c',
        'ż': 'z',
        'ź': 'z',
        'ó': 'o',
        '&': ' and ',
        '\'': '',
        '"': '',
        '\\(': '',
        '\\)': '',
        '\\[': '',
        '\\]': '',
        '!': '',
        '\\?': '',
        ',': '',
        '\\.': '',
        ':': '',
        ' ': '-'
    ]

    static encode = { String str ->
        if (!str) {
            return str
        }

        String result = str.toLowerCase()

        REPLACEMENT_MAP.each { item ->
            result = result.replaceAll(item.key, item.value)
        }

        return result.encodeAsURL()
    }
}

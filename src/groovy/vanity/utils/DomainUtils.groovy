package vanity.utils

final class DomainUtils {

    public static String generateHash(final Class<?> clazz, final String uniquePart){
        return "${clazz.name}!@f#gA${uniquePart}".encodeAsMD5()
    }

}

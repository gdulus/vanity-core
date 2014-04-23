package vanity.utils

final class InstanceConfigurationUtils {

    public static final String EXTERNAL_CONFIG_LOCATION = 'config.external.location'

    public static String getProperty(final String propertyName, final Boolean failOnNotExists = false) {
        String value = System.getProperty(propertyName)

        if (failOnNotExists && !value) {
            throw new IllegalStateException("Property -D${propertyName} is not set")
        }

        return value
    }

}

package vanity.utils

import groovy.util.logging.Slf4j

final class ConfigUtils {

    public static List<String> externalConfig(final def grails, final Closure worker) {
        String externalConfigPath = InstanceConfigurationUtils.getValue(InstanceConfigurationUtils.EXTERNAL_CONFIG_LOCATION, true)
        Collector collector = new Collector(externalConfigPath)
        collector.with worker
        grails.config.locations = collector.files
    }

    public static <T> T $as(final def configObject, final Class<T> target) {
        return configObject.asType(target)
    }

    @Slf4j
    private static class Collector {

        final List<String> files = []

        final String userHome

        Collector(String userHome) {
            this.userHome = userHome
        }

        public void file(final String fileName) {
            String configFilePath = "${userHome}/${fileName}.groovy"
            // validate existence of config file
            if (!(new File(configFilePath).exists())) {
                throw new IllegalArgumentException("Cant find config file: ${configFilePath}")
            }
            // accumulate path as external file
            log.info('Loading external file: {}', configFilePath)
            files << "file:${configFilePath}"
        }
    }

}

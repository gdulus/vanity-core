package vanity.utils

import grails.util.Environment
import groovy.util.logging.Slf4j

final class ConfigUtils {

    public static List<String> externalConfig(final File userHome, final Closure worker) {
        return externalConfig(userHome.path, worker)
    }

    public static List<String> externalConfig(final String userHome, final Closure worker) {
        Collector collector = new Collector(userHome)
        collector.with worker
        return collector.files
    }

    public static <T> T $as(final def configObject, final Class<T> target) {
        return configObject.asType(target)
    }

    @Slf4j
    private static class Collector {

        private static final String RELATIVE_PATH = '.vanity'

        final List<String> files = []

        final String userHome

        Collector(String userHome) {
            this.userHome = userHome
        }

        public void file(final String fileName) {
            String configFilePath = "${userHome}/${RELATIVE_PATH}/${Environment.current}/${fileName}.groovy"
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

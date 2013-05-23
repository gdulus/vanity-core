package vanity.utils

import grails.util.Environment
import groovy.util.logging.Log4j

@Log4j
@Singleton
class ConfigUtils {

    private static final String RELATIVE_PATH = '.vanity'

    public List<String> externalConfig(final def grails, final File userHome, final Closure dataCollector){
        // prepare list in paths to external config will be located
        List<String> files = []
        // enhance setter class to support "file" method
        dataCollector.metaClass.file = {final String fileName ->
            // build path to the file
            String configFilePath = "${userHome}/${RELATIVE_PATH}/${Environment.current}/${fileName}"
            // validate existence of config file
            if (!(new File(configFilePath).exists())){
                throw new IllegalArgumentException("Cant find config file: ${configFilePath}")
            }
            // accumulate path as external file
            log.info("Loading external file: ${configFilePath}")
            files << "file:${configFilePath}"
        }
        // evaluate data collector
        dataCollector.call()
        // assign collected files as external resources
        grails.config.locations = files
    }

}

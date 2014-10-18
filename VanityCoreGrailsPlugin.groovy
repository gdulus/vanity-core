class VanityCoreGrailsPlugin {

    def version = "0.1"

    def grailsVersion = "2.2 > *"

    def dependsOn = [
        hibernate: "3.6.10.10"
    ]

    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Vanity Core Plugin"

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }

    def onShutdown = { event ->
    }
}

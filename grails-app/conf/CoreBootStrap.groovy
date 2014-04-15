import vanity.utils.BootstrapUtils

class CoreBootStrap {

    BootstrapUtils bootstrapUtils

    def init = { servletContext ->
        bootstrapUtils.init()
    }

    def destroy = {
    }
}

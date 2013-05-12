package vanity.utils

import vanity.article.ContentSource

final class BootstrapUtils {

    private BootstrapUtils(){
    }

    public static void init(){
        initContentSource();
    }

    private static void initContentSource(){
        ContentSource.Target.values().each {
            if (!ContentSource.findByTarget(it)){
                new ContentSource(target: it).save(flush:true)
            }
        }
    }
}

package LabD

import static org.ops4j.pax.exam.CoreOptions.bundle

class Utils {

    static def allBundles(dirName) {
        def bundles = []
        new File(dirName).eachFileMatch(~/.*\.jar/) { file ->
            bundles << bundle("file:${file.absolutePath}")
        }
        return bundles
    }

}

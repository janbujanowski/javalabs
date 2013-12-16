package pk.labs.LabB

import java.net.URLDecoder
import java.util.jar.JarEntry
import java.util.jar.JarInputStream

class Utils {

    static def getClassesInJar(String jarName) {
        def classes = []
        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName))
            JarEntry jarEntry
            while (true) {
                jarEntry = jarFile.getNextJarEntry()
                if (jarEntry == null) break
                if (jarEntry.name.endsWith(".class"))
                    classes << Class.forName(jarEntry.getName().replaceAll("/", "\\.") - '.class')

            }
        }
        catch( Exception e) {
            e.printStackTrace ()
        }
        return classes
    }

    static def getClassesInPackage(Class<?> clazz) {
        getClassesInJar getJarFile(clazz)
    }

    static def getClassesInPackage(String className) {
        getClassesInPackage Class.forName(className)
    }

    static String getJarFile(Class<?> clazz) {
        URLDecoder.decode clazz.protectionDomain.codeSource.location.file
    }

    static String getJarFile(String className) {
        getJarFile Class.forName(className)
    }
}

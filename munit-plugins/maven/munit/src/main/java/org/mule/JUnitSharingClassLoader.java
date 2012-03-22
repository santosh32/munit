package org.mule;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 3/22/12
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
/**
 * Loads JUnit from another class loader, but otherwise delegate everything to the parent.
 *
 * @author Kohsuke Kawaguchi
 */
final class JUnitSharingClassLoader extends ClassLoader {
    private final ClassLoader junitLoader;
    public JUnitSharingClassLoader(ClassLoader parent, ClassLoader junitLoader) {
        super(parent);
        this.junitLoader = junitLoader;
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name.startsWith("junit.") || name.startsWith("org.junit"))
            return junitLoader.loadClass(name);
        return super.loadClass(name, resolve);
    }
}
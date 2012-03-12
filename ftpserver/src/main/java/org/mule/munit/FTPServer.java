package org.mule.munit;

import java.io.File;
import java.io.FilenameFilter;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 3/9/12
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FTPServer {

    abstract void initialize(int port);
    abstract void start();
    abstract void stop();

    public void containsFiles(String file, String path) {
        assertTrue(containsFilteredFiles(file, path).length > 0);
    }

    private File[] containsFilteredFiles(final String fileName, String path) {
        return new File(path).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                return name.startsWith(fileName);
            }
        });
    }

    public void remove(String path)
    {
        new File(path).delete();
    }
}

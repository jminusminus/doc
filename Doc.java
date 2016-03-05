//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

import github.com.ricallinson.http.util.Map;
import github.com.ricallinson.http.util.MapItem;
import github.com.ricallinson.http.util.LinkedListMap;

public class Doc {
    public static void main(String[] args) {

    }

    // The current JMM working directory.
    public final String workingDir = System.getenv("JMMPATH");

    // Map of all the classes parsed.
    protected Map docClasses = new LinkedListMap();

    public String getDoc(String classPath) {
        MapItem dc = this.docClasses.get(classPath);
        if (dc == null) {
            return "";
        }
        return ((DocClass)dc.value()).toString();
    }

    // Parse all .java files found and add them to this.docClasses.
    public void parseFiles(String[] files) {
        for (String file : files) {
            DocClass dc = this.parseFile(file);
            this.docClasses.put(dc.classPath(), dc);
        }
    }

    // Parse a .java file and return a DocClass instance.
    public DocClass parseFile(String file) {
        return new DocClass(file);
    }

    // Return all .java files in the current workspace.
    public String[] getJavaFiles() {
        if (this.workingDir.length() == 0) {
            return new String[0];
        }
        return this.walk(this.workingDir).split(" ");
    }

    // Walk the given directory and find all .java files.
    protected String walk(String dir) {
        java.io.File root = new java.io.File(dir);
        java.io.File[] list = root.listFiles();
        if (list == null) {
            return "";
        }
        String file = "";
        String files = "";
        for (java.io.File item : list) {
            try {
                file = item.getCanonicalPath();
            } catch (Exception e) {
                System.out.println(e);
            }
            if (item.isDirectory()) {
                files += this.walk(file) + " ";
            } else {
                if (file.endsWith(".java") && !file.endsWith("_test.java")) {
                    files += file + " ";
                }
            }
        }
        return files.trim();
    }
}

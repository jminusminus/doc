//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

import github.com.ricallinson.http.io.BufferedInputStreamReader;
import github.com.ricallinson.http.io.InputStreamMock;

public class Doc {
    public static void main(String[] args) {
        // Get the path from the args.
        // If the path is a dir;
        // Walk all files recusively found in the path.
        // If the path is file;
        // Parse the file and return Markdown.
    }

    public final String workingDir = System.getenv("JMMPATH");

    public void parseFiles(String[] files) {
        for (String file : files) {
            DocClass dc = this.parseFile(file);
            if (dc != null && dc.className != null) {
                dc.toString();
            }
        }
    }

    public DocClass parseFile(String file) {
        byte[] data = new byte[0];
        try {
            data = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file));
        } catch (Exception e) {
            return null;
        }
        BufferedInputStreamReader reader = new BufferedInputStreamReader(new InputStreamMock(data));
        return new DocClass(file, reader);
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
                files += this.walk(file);
            } else {
                if (file.endsWith(".java") && !file.endsWith("_test.java")) {
                    files += file + " ";
                }
            }
        }
        return files.trim();
    }
}

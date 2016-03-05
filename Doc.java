//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

import github.com.ricallinson.http.Server;

// Jmm documentation tool.
public class Doc {

    // Print the documentation for a given class path file.
    //
    // ### Usage
    //
    // `jmmdoc class_path`
    //
    // ```
    // > jmmdoc github.com.ricallinson.jmmdoc.Doc
    // ```
    public static void main(String[] args) {
        Doc doc = new Doc();
        if (args.length == 1) {
            System.out.println("");
            System.out.println(doc.getDoc(args[0]).trim());
            System.out.println("");
        }
        doc.startServer(8080);
    }

    // The current JMM working directory.
    protected final String workingDir = System.getenv("JMMPATH");

    // Return an instance of Doc.
    public Doc() {
        
    }

    // Start HTTP server for docs.
    public void startServer(int port) {
        Server s = Server.createServer((req, res) -> {
            String classPath = req.url.substring(1).replace("/", ".");
            System.out.println(classPath);
            if (classPath.length() > 0) {
                res.end(this.getDoc(classPath));
                return;
            }
            res.end(this.toString());
        });
        s.listen(port);
    }

    // Returns markdown for the given class path.
    public String getDoc(String classPath) {
        DocClass dc = new DocClass(this.classPathFilePath(classPath));
        if (dc == null) {
            return "";
        }
        return dc.toString();
    }

    public String classPathFilePath(String classPath) {
        String path = this.workingDir + "/src/" + classPath.replace(".", "/") + ".java";
        System.out.println(path);
        return path;
    }

    public String toString() {
        String list = "";
        for (String file : this.getJavaFiles()) {
            list += file.substring(this.workingDir.length() + 5, file.length() - 5).replace("/", ".") + "\r\n";
        }
        return list;
    }

    // Return all .java files in the current workspace.
    protected String[] getJavaFiles() {
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

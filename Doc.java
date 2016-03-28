//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

// Package description.
package github.com.ricallinson.jmmdoc;

import github.com.ricallinson.jmmmarkdown.Markdown;
import github.com.ricallinson.http.Server;

// Class description.
public class Doc {

    // Public method description with inline markdown.
    // 
    // Print the documentation for a given class path file.
    //
    // ### Usage
    //
    // `jmmdoc [class_path]`
    //
    // ```
    // $ jmmdoc github.com.ricallinson.jmmdoc.Doc
    // ```
    public static void main(String[] args) {
        Doc doc = new Doc();
        if (args.length == 1) {
            System.out.println("");
            System.out.println(doc.getDoc(args[0]).trim());
            System.out.println("");
            return;
        }
        doc.startServer(8080);
    }

    // The current JMM working directory.
    protected final String workingDir = System.getenv("JMMPATH");
    
    // Line Feed.
    protected static final byte LF = (byte)10;

    // Carrage return, line feed.
    protected static final String CRLF = "\r\n";

    // Public method description.
    // Start HTTP server for docs.
    public void startServer(int port) {
        Server s = Server.createServer((req, res) -> {
            String classPath = req.url.substring(1).replace("/", ".");
            String md;
            if (classPath.length() > 0) {
                md = this.getDoc(classPath);
            } else {
                md = this.toString();
            }
            res.setHeader("Content", "text/html");
            res.end("<html><head>" + this.getCss() + "</head><body>" + Markdown.parse(md).toString() + "</body></html>");
        });
        s.listen(port);
        System.out.println("Document server started on port 8080");
        System.out.println("Serving documentation from " + this.workingDir);
    }

    // Public method description.
    // Returns the documentation for the given class path.
    public String getDoc(String classPath) {
        DocClass dc = new DocClass(this.classPathFilePath(classPath));
        if (dc == null) {
            return "";
        }
        return dc.toString();
    }

    // Public method description.
    // Returns a JMM class path for the given Java class path.
    public String classPathFilePath(String classPath) {
        String path = this.workingDir + "/src/" + classPath.replace(".", "/") + ".java";
        return path;
    }

    // Public method description.
    // Returns markdown.
    public String toString() {
        String list = "";
        for (String file : this.getJavaFiles()) {
            if (file.length() > this.workingDir.length() + 10) {
                String classPath = file.substring(this.workingDir.length() + 5, file.length() - 5).replace("/", ".");
                list += "* [/" + classPath + "](" + classPath + ")" + this.CRLF;
            }
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

    protected String getCss() {
        byte[] data = new byte[0];
        try {
            data = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("./resources/main.css"));
        } catch (Exception e) {
            return "";
        }
        return "<style>" + new String(data) + "</style>";
    }
}

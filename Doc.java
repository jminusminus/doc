//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

// [![Build Status](https://travis-ci.org/jminusminus/doc.svg?branch=master)](https://travis-ci.org/jminusminus/doc)
// ## Stability: 0 - Unstable
// This package contains utilities for generating Jmm documentation and serving it via HTTP.
package github.com.jminusminus.doc;

import github.com.ricallinson.jmmmarkdown.Markdown;
import github.com.ricallinson.http.Server;

// This class can be programmatically used to generate Jmm documentation from a Jmm packages source code.
public class Doc {

    // ## Command Line Usage
    //
    // Prints the Markdown for a given Jmm class.
    // `doc [jmm_classpath]`
    //
    // ```
    // $ doc github.com.jminusminus.doc.Doc
    // // Prints the Markdown for class "github.com.jminusminus.doc.Doc" to stdout.
    // ```
    // 
    // Starts a web server with all documentation for the current Jmm workspace.
    // `doc`
    //
    // ```java
    // $ doc
    // Server started on port 8080, using 4 cores and 8 threads...
    // Document server started at http://localhost:8080/
    // Serving documentation from /Users/allinson/Java/jmmworkspace 
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

    // Start a HTTP server on the given port to serve the documentation for the current Jmm workspace.
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
            // Replace this with handlebars.
            res.end("<html><head>" + this.getCss() + "</head><body>" + this.getMenu() + "<div class=\"content\">" + Markdown.parse(md).toString() + "</div></body></html>");
        });
        s.listen(port);
        System.out.println("Document server started at http://localhost:8080/");
        System.out.println("Serving documentation from " + this.workingDir);
    }

    // Returns Markdown documentation for the given Jmm class path in the current Jmm workspace.
    public String getDoc(String classPath) {
        DocClass dc = new DocClass(this.classPathFilePath(classPath));
        if (dc == null) {
            return "";
        }
        return dc.toString();
    }

    // Returns Markdown with links to all the classes found in the current Jmm workspace.
    public String toString() {
        String list = "";
        for (String file : this.getJavaFiles()) {
            if (file.length() > this.workingDir.length() + 10) {
                String classPath = file.substring(this.workingDir.length() + 5, file.length() - 5).replace("/", ".");
                list += "* [" + classPath + "](/" + classPath + ")" + this.CRLF;
            }
        }
        return "## Package List\n" + list;
    }

    // Returns a Jmm class path for the given Java package name.
    protected String classPathFilePath(String classPath) {
        String path = this.workingDir + "/src/" + classPath.replace(".", "/") + ".java";
        return path;
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

    // TODO: don't use a file.
    protected String getMenu() {
        byte[] data = new byte[0];
        try {
            data = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("./resources/menu.html"));
        } catch (Exception e) {
            return "";
        }
        return new String(data);
    }

    // TODO: don't use a file.
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

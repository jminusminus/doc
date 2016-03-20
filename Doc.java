//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

import github.com.ricallinson.http.Server;

// Jmm documentation tool.
public class Doc {

    // Line Feed.
    protected static final byte LF = (byte)10;

    // 
    protected static final String CRLF = "\r\n";

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
            System.out.println(doc.getDoc(args[0], false).trim());
            System.out.println("");
            return;
        }
        doc.startServer(8080);
    }

    // The current JMM working directory.
    protected final String workingDir = System.getenv("JMMPATH");

    // Start HTTP server for docs.
    public void startServer(int port) {
        Server s = Server.createServer((req, res) -> {
            String classPath = req.url.substring(1).replace("/", ".");
            String html;
            if (classPath.length() > 0) {
                html = this.getDoc(classPath, true);
            } else {
                html = this.toHtml();
            }
            res.setHeader("Content", "text/html");
            res.end("<html><body>" + html + "</body></html>");
        });
        s.listen(port);
        System.out.println("Document server started on port 8080");
    }

    // Returns the documentation for the given class path.
    public String getDoc(String classPath, boolean asHtml) {
        DocClass dc = new DocClass(this.classPathFilePath(classPath));
        if (dc == null) {
            return "";
        }
        if (asHtml) {
            return dc.toHtml();
        }
        return dc.toString();
    }

    // Returns a JMM class path for the given Java class path.
    public String classPathFilePath(String classPath) {
        String path = this.workingDir + "/src/" + classPath.replace(".", "/") + ".java";
        return path;
    }

    // Returns markdown.
    public String toString() {
        String list = "";
        for (String file : this.getJavaFiles()) {
            list += file.substring(this.workingDir.length() + 5, file.length() - 5).replace("/", ".") + this.CRLF;
        }
        return list;
    }

    // Returns HTML.
    public String toHtml() {
        String list = "<ul>";
        String link;
        for (String file : this.getJavaFiles()) {
            link = file.substring(this.workingDir.length() + 5, file.length() - 5).replace("/", ".");
            list += "<li><a href=\"" + link + "\">" + link + "</a></li>";
        }
        return list + "</ul>";
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

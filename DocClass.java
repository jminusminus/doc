//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.jminusminus.doc;

import github.com.ricallinson.http.io.BufferedInputStreamReader;
import github.com.ricallinson.http.io.InputStreamMock;

// Container for class documentation.
// ```
// new DocClass();
// ```
public class DocClass {

    // The source file.
    public String file = "";

    // The package name.
    public String packageName = "";

    // The description for the package.
    public String packageDesc = "";

    // The description for main.
    public String mainDesc = ""; 

    // The class name.
    public String className = "";

    // The description for the class.
    public String classDesc = "";

    // The class attributes.
    public DocAttribute[] attributes = new DocAttribute[0];

    // The class methods.
    public DocMethod[] methods = new DocMethod[0];

    DocClass(String file) {
        this.file = file;
        this.parse();
    }

    public String classPath() {
        return this.packageName + "." + this.className;
    }

    // Returns Markdown.
    public String toString() {
        String md = "# " + this.className + Doc.CRLF;
        if (this.mainDesc.length() > 0) {
            md += this.mainDesc + Doc.CRLF;
        }
        md += "#### " + this.packageName + Doc.CRLF;
        if (this.packageDesc.length() > 0) {
            md += this.packageDesc + Doc.CRLF;
        }
        if (this.classDesc.length() > 0) {
            md += this.classDesc + Doc.CRLF;
        }
        md += "```" + Doc.CRLF + "import " + this.classPath() + ";" + Doc.CRLF + "```" + Doc.CRLF;
        for (DocAttribute i : this.attributes) {
            md += i.toString();
        }
        for (DocMethod i : this.methods) {
            md += i.toString();
        }
        return md;
    }

    // Parse a .java file and return markdown documentation.
    protected boolean parse() {
        byte[] data = new byte[0];
        try {
            data = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(this.file));
        } catch (Exception e) {
            return false;
        }
        BufferedInputStreamReader reader = new BufferedInputStreamReader(new InputStreamMock(data));
        byte[] buf = reader.readTo(Doc.LF);
        String line;
        String comment = "";
        while (buf.length > 0) {
            line = new String(buf).trim();
            if (line.isEmpty() == false) {
                comment = this.parseLine(line, comment);
            } else {
                comment = "";
            }
            // Read the next line into the buffer.
            buf = reader.readTo(Doc.LF);
        }
        return true;
    }

    protected String parseLine(String line, String comment) {
        if (line.startsWith("//")) {
            // Build up the comment.
            comment += this.removeCommentSpace(line.substring(2)) + Doc.CRLF;
        } else if (line.startsWith("protected")) {
            comment = "";
        } else if (line.startsWith("public")) {
            comment = this.parseLinePublic(line, comment);
        } else if (line.startsWith("package") && this.packageName.length() == 0) {
            this.packageName = line.substring(7, line.length() - 1).trim();
            this.packageDesc = comment;
            comment = "";
        }
        return comment;
    }

    protected String parseLinePublic(String line, String comment) {
        if (line.startsWith("public") && line.endsWith(";")) {
            // Add attribute.
            DocAttribute a = new DocAttribute();
            a.name = line.substring(6, line.length() - 1).trim();
            a.desc = comment;
            this.addAttr(a);
            comment = "";
        } else if (line.startsWith("public class") && line.endsWith("{")) {
            // Add class.
            this.classDesc = comment;
            this.className = line.substring(12, line.length() - 1).trim();
            comment = "";
        } else if (line.startsWith("public static void main(")) {
            // Add main method description.
            this.mainDesc = comment;
            comment = "";
        } else if (line.startsWith("public") && line.endsWith("{")) {
            // Add method.
            DocMethod m = new DocMethod();
            m.name = line.substring(6, line.length() - 1).trim();
            m.desc = comment;
            this.addMethod(m);
            comment = "";
        }
        return comment;
    }

    // Is there is a space before the coment text starts [// abc]?
    // Or, are there spaces in comment code [//     some code]?
    protected String removeCommentSpace(String comment) {
        if (comment.length() == 0) {
            return "";
        }
        int index = 0;
        // Count the numer of spaces before a comment starts.
        char token = comment.charAt(index);
        while (token == ' ') {
            index++;
            token = comment.charAt(index);
        }
        if (index == 1 || index == 5) {
            return comment.substring(1);
        }
        return comment;
    }

    protected void addAttr(DocAttribute a) {
        DocAttribute[] n = new DocAttribute[this.attributes.length + 1];
        System.arraycopy(this.attributes, 0, n, 0, this.attributes.length);
        n[this.attributes.length] = a;
        this.attributes = n;
    }

    protected void addMethod(DocMethod m) {
        DocMethod[] n = new DocMethod[this.methods.length + 1];
        System.arraycopy(this.methods, 0, n, 0, this.methods.length);
        n[this.methods.length] = m;
        this.methods = n;
    }
}

//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

import github.com.ricallinson.http.io.BufferedInputStreamReader;
import github.com.ricallinson.http.io.InputStreamMock;

// Container for class documentation.
// ```
// new DocClass();
// ```
public class DocClass {

    // The source file.
    public String file;

    // The package name.
    public String packageName;

    // The description for the package.
    public String packageDesc;

    // The class name.
    public String className;

    // The description for the class.
    public String classDesc;

    // The class attributes.
    public DocAttribute[] attributes = new DocAttribute[0];

    // The class methods.
    public DocMethod[] methods = new DocMethod[0];

    // Line Feed.
    protected static final byte LF = (byte)10;

    // 
    protected static final String CRLF = "\r\n";

    DocClass(String file) {
        this.file = file;
        this.parse();
    }

    public String classPath() {
        return this.packageName + "." + this.className;
    }

    // Prints to stdout.
    public String toString() {
        String md = "" +
            "# " + this.className + this.CRLF +
            "#### " + this.packageName + this.CRLF +
            this.packageDesc + this.CRLF + this.CRLF +
            "```" + this.CRLF + "import " + this.classPath() + ";" + this.CRLF + "```" + this.CRLF +
            this.classDesc + this.CRLF;
        for (DocAttribute i : this.attributes) {
            md += "## " + i.name + this.CRLF + i.desc + this.CRLF;
        }
        for (DocMethod i : this.methods) {
            md += "## " + i.name + this.CRLF + i.desc + this.CRLF;
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
        byte[] buf = reader.readTo(this.LF);
        String line;
        String comment = "";
        while (buf.length > 0) {
            line = new String(buf).trim();
            comment = this.parseLine(line, comment);
            // Read the next line into the buffer.
            buf = reader.readTo(this.LF);
        }
        return true;
    }

    protected String parseLine(String line, String comment) {
        if (line.startsWith("//")) {
            // Build up the comment.
            comment += line.substring(2).trim() + "\r\n";
        } else if (line.startsWith("protected")) {
            comment = "";
        } else if (line.startsWith("public")) {
            comment = this.parseLinePublic(line, comment);
        } else if (line.startsWith("package") && this.packageName == null) {
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

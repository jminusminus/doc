//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

import github.com.ricallinson.http.io.BufferedInputStreamReader;

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

    // The source byte stream.
    protected BufferedInputStreamReader reader;

    // Line Feed.
    protected static final byte LF = (byte)10;

    DocClass(String file, BufferedInputStreamReader reader) {
        this.file = file;
        this.reader = reader;
        this.parse();
    }

    public String toString() {
        System.out.println(this.packageDesc);
        System.out.println("    package " + this.packageName + "\r\n");
        System.out.println(this.classDesc);
        System.out.println("    " + this.className + "\r\n");
        for (DocAttribute i : this.attributes) {
            System.out.println(i.desc);
            System.out.println("    " + i.name + "\r\n");
        }
        for (DocMethod i : this.methods) {
            System.out.println(i.desc);
            System.out.println("    " + i.name + "\r\n");
        }
        return "";
    }

    // Parse a .java file and return markdown documentation.
    protected void parse() {
        byte[] buf = this.reader.readTo(this.LF);
        String line;
        String comment = "";
        while (buf.length > 0) {
            line = new String(buf).trim();
            comment = this.parseLine(line, comment);
            // Read the next line into the buffer.
            buf = this.reader.readTo(this.LF);
        }
    }

    protected String parseLine(String line, String comment) {
        if (line.startsWith("//")) {
            // Build up the comment.
            comment += line.substring(2).trim();
        } else if (line.startsWith("public")) {
            comment = this.parseLinePublic(line, comment);
        } else if (line.startsWith("package") && this.packageName == null) {
            this.packageName = line.substring(7);
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

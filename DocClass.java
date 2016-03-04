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
    public String package;

    // The description for the package.
    public String packageDesc;

    // The class name.
    public String className;

    // The description for the class.
    public String classDesc;

    // The class attributes.
    public DocAttribute[] attributes;

    // The class methods.
    public DocMethod[] methods;

    // The source byte stream.
    protected BufferedInputStreamReader reader;

    // Line Feed.
    protected static final byte LF = (byte)10;

    DocClass(String file, BufferedInputStreamReader reader) {
        this.file = file;
        this.reader = reader;
        this.parse();
        System.out.println(this.file);
    }

    // Parse a .java file and return markdown documentation.
    protected void parse() {
        byte[] buf = this.reader.readTo(this.LF);
        String line;
        String comment = "";
        while (buf.length > 0) {
            line = new String(buf).trim();
            if (line.startsWith("//")) {
                // Build up the comment.
                comment += line.substring(2).trim();
            } else if (line.startsWith("public") && line.endsWith(";")) {
                // Add attribute.
                DocAttribute a = new DocAttribute();
                a.attribute = line;
                a.description = comment;
                comment = "";
            } else if (line.startsWith("public") && line.endsWith("{")) {
                // Add method.
                DocMethod m = new DocMethod();
                m.method = line;
                m.description = comment;
                comment = "";
            } else if (line.startsWith("package") && this.package.length() == 0) {
                this.packageDesc = comment;
                this.package = line.substring(7);
                comment = "";
            }
            // Read the next line into the buffer.
            buf = this.reader.readTo(this.LF);
        }
    }
}

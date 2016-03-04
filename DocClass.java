//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

import github.com.ricallinson.http.io.BufferedInputStreamReader;

public class DocClass {

    protected String file;

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
        while (buf.length > 0) {
            // Read the next line into the buffer.
            buf = this.reader.readTo(this.LF);
        }
    }
}

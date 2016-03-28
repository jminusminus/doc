//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

public class DocAttribute {

	// The attribute.
    public String name;

	// The description for the attribute.
    public String desc;

    // Returns Markdown.
    public String toString() {
    	return "## " + this.name + Doc.CRLF + this.desc + Doc.CRLF;
    }
}

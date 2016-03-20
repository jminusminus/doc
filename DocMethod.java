//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

public class DocMethod {

	// The method signature.
    public String name;

	// The description for the method.
    public String desc;

    public String toString() {
    	return "## " + this.name + Doc.CRLF + this.desc + Doc.CRLF;
    }

    // Returns HTML.
    public String toHtml() {
    	return "<h2>" + this.name + "</h2><p>" + this.desc + "</p>";
    }
}

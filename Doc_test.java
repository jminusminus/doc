//
// Copyright 2016, Yahoo Inc.
// Copyrights licensed under the New BSD License.
// See the accompanying LICENSE file for terms.
//

package github.com.ricallinson.jmmdoc;

import github.com.jminusminus.simplebdd.Test;

public class Doc_test extends Test {
    public static void main(String[] args) {
        Doc_test test = new Doc_test();
        test.run();
    }

    // This is a comment for an attribute.
    public int num = 0;

    public void test_new_Doc() {
        this.should("return an instance of Doc");
        Doc doc = new Doc();
        this.assertEqual("github.com.ricallinson.jmmdoc.Doc", doc.getClass().getName());
    }

    // public void test_getDoc() {
    //     this.should("parse one file and return markdown");
    //     Doc doc = new Doc();
    //     DocClass dc = doc.parseFile("./Doc.java");
    //     this.assertEqual(true, dc.toString().contains("github.com.ricallinson.jmmdoc.Doc"));
    // }
}

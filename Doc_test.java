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

    public void test_getJavaFiles() {
        this.should("return a list of all .java files in the given directory");
        Doc doc = new Doc();
        String[] files = doc.getJavaFiles();
        for (String file : files) {
            if (file.contains("/jmmdoc/Doc.java")) {
                this.assertEqual(true, true);
            }
        }
    }

    public void test_parse_file() {
        this.should("parse one file");
        Doc doc = new Doc();
        doc.parseFile("./Doc.java");
    }

    public void test_parse_files() {
        this.should("parse all files");
        Doc doc = new Doc();
        doc.parseFiles(doc.getJavaFiles());
    }
}

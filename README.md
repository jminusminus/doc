
# Doc
#### Package: github.com.jminusminus.doc
[![Build Status](https://travis-ci.org/jminusminus/doc.svg?branch=master)](https://travis-ci.org/jminusminus/doc)
## Stability: 0 - Unstable
This package contains utilities for generating Jmm documentation and serving it via HTTP.

## Install
```
jmm get github.com/jminusminus/doc
```
## Command Line Usage

Prints the Markdown for a given Jmm class.
`doc [jmm_classpath]`

```
$ doc github.com.jminusminus.doc.Doc
// Prints the Markdown for class "github.com.jminusminus.doc.Doc" to stdout.
```

Starts a web server with all documentation for the current Jmm workspace.
`doc`

```java
$ doc
Server started on port 8080, using 4 cores and 8 threads...
Document server started at http://localhost:8080/
Serving documentation from /Users/allinson/Java/jmmworkspace
```

## github.com.jminusminus.doc.Doc
This class can be programmatically used to generate Jmm documentation from a Jmm packages source code.

```
import github.com.jminusminus.doc.Doc;
```
## void startServer(int port)
Start a HTTP server on the given port to serve the documentation for the current Jmm workspace.

## String getDoc(String classPath)
Returns Markdown documentation for the given Jmm class path in the current Jmm workspace.

## String toString()
Returns Markdown with links to all the classes found in the current Jmm workspace.


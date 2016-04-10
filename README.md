
# Doc
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

#### github.com.jminusminus.doc
[![Build Status](https://travis-ci.org/jminusminus/doc.svg?branch=master)](https://travis-ci.org/jminusminus/doc)
## Stability: 0 - Unstable
This package contains utilities for generating Jmm documentation and serving it via HTTP.

Class description.

```
import github.com.jminusminus.doc.Doc;
```
## void startServer(int port)
Public method description.
Start HTTP server for docs.

## String getDoc(String classPath)
Public method description.
Returns the documentation for the given class path.

## String classPathFilePath(String classPath)
Public method description.
Returns a JMM class path for the given Java class path.

## String toString()
Public method description.
Returns markdown.


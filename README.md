
# Doc
Jmm main() method description with inline markdown.

Print the documentation for a given class path file.

### Usage

`jmmdoc [class_path]`

```
$ jmmdoc github.com.jminusminus.doc.Doc
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


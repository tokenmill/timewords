# timewords

[![Clojars Project](https://img.shields.io/clojars/v/lt.tokenmill/timewords.svg)](https://clojars.org/lt.tokenmill/timewords)

Library to parse date string to date.

# Usage
 
## Clojure

Add a dependency to your `project.clj`:

```clojure
[lt.tokenmill/timewords "0.1.3"]
```

```clojure
(require '[timewords.core :refer [parse]])
=> nil
(parse "2001-01-01")
=> #inst"2001-01-01T00:00:00.000-00:00"
```

## Java

As of now the JAR is stored in Clojars, therefore maven is not going to find the artifact.
You should add the repository information to your `pom.xml`:
```xml
<repositories>
    <repository>
        <id>clojars.org</id>
        <url>http://clojars.org/repo</url>
    </repository>
</repositories>

```

Add a maven dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>lt.tokenmill</groupId>
    <artifactId>timewords</artifactId>
    <version>0.1.3</version>
</dependency>
```

```java
import lt.tokenmill.timewords.Timewords;

public static void main(String[] args) {
    Timewords timewords = new Timewords();
    Date date = timewords.parse("2001-01-01");
}
```
Note that `timewords` depends on `org.clojure/clojure` which must be provided.

# TODO

TODO: relative dates e.g. "yesterday", are not handled.

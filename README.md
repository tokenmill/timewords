# timewords
Library to parse date string to date.

TODO: relative dates e.g. "yesterday", are not handled.

# Usage
 
## Clojure

Add a dependency to your `project.clj`:

```clojure
[lt.tokenmill/timewords "0.1.0"]
```

```clojure
(require [timewords.core :refer [parse]])
(parse "2001-01-01")
```

## Java

Add a maven dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>lt.tokenmill</groupId>
    <artifactId>timewords</artifactId>
    <version>0.1.0</version>
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


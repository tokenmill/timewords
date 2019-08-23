# timewords

[![Clojars Project](https://img.shields.io/clojars/v/lt.tokenmill/timewords.svg)](https://clojars.org/lt.tokenmill/timewords)

Library to parse a date string to java.util.Date object. For example:

* "2 weeks from now" -> 2018-11-29T09:52:23.000-00:00
* "28th February 2019" -> 2019-02-28T00:00:00.000-00:00

When the library cannot parse the input string it returns *`nil`*.

More formally, from four types of temporal expressions: *time*, *duration*, *interval*, and *set*; only one type is of interest: *time*. Also, *time* type can be divided into two subtypes: fuzzy (e.g. last Sunday) and absolute (1st of January, 2019). To parse a fuzzy time string a *reference time* (i.e. a `java.util.Date object`) is required. By default, reference time is ``now``. 

The library is designed to support multiple languages. Currently two languages are supported: English and Lithuanian. Default language is English.

# Usage
 
## Clojure

Add a dependency to your
* *project.clj* - `[lt.tokenmill/timewords "0.5.0"]`
* *deps.edn* - `lt.tokenmill/timewords {:mvn/version "0.5.0"}`

```clojure
(require '[timewords.core :refer [parse]])
=> nil
(parse "2001-01-01")
=> #inst"2001-01-01T00:00:00.000-00:00"
(timewords.core/parse "now")
=> #inst"2016-12-13T09:52:02.000-00:00"
(timewords.core/parse "2 weeks ago")
=> #inst"2016-11-29T09:52:23.000-00:00"
(timewords.core/parse "2 weeks from now")
=> #inst"2016-12-29T09:54:23.000-00:00"
(timewords.core/parse "last monday")
=> #inst"2016-12-12T09:54:23.000-00:00"
(timewords.core/parse "last june")
=> #inst"2016-06-12T09:54:23.000-00:00"
(timewords.core/parse "last spring")
=> #inst"2016-05-12T09:54:23.000-00:00"

(timewords.core/parse "29th February 2016")
=> #inst"2016-02-29T00:00:00.000-00:00"
(timewords.core/parse "29th February 2017")
=> #inst"2017-02-01T00:00:00.000-00:00"
(timewords.core/parse "Sunday, 1st January 2017")
=> #inst"2017-01-01T00:00:00.000-00:00"

(timewords.core/parse "2016 m. gruodžio 22 d. 11:10" nil "lt")
=> #inst"2016-12-22T11:10:00.000-00:00"
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
    <version>0.4.0</version>
</dependency>
```

```java
import lt.tokenmill.timewords.Timewords;

public static void main(String[] args) {
    Timewords timewords = new Timewords();
    Date d1 = timewords.parse("2001-01-01");
    Date d2 = timewords.parse("2001-01-01", new Date());
    Date d3 = timewords.parse("2001-01-01", new Date(), "en");
}
```
Note that `timewords` depends on `org.clojure/clojure` which must be provided.

# Notes

Relative dates that can be understood as a time period, e.g. `last December` are rounded to the beginning of the period, e.g. `last December` translates to `2016-12-01T00:00:00Z`.

Timewords of the form `in monthname` is interpreted as if it refers to the past, i.e. `in December` means `last December`.

Timewords of the form `this monthname` is interpreted as if it refers to the future, i.e. `in December` means `next December`.

Timeword which is only a name of a weekday, e.g. `Monday`, is interpreted as if it refers to the past, i.e. `Monday` means the same as `last Monday`.

Timeword of the form `next weekday` means the first day in the future which which weekday is the one mentioned, e.g. `next Monday` means the first Monday to come. If today is Monday and we are parsing `next Monday` then it means a date after 7 days. 

Timeword of a form `this weekday`, e.g. `this Monday`, is interpreted as if it refers to the future, i.e. `this Monday` means the same as `next Monday`.

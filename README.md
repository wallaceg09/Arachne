[Praeses Business Technologies](https://praesesbt.com/)

# Arachne

Arachne is a Kotlin library whose goal is to make interfacing with SPARQL
endpoints via the  [Jena ARQ
library](https://jena.apache.org/documentation/query/) in JVM  languages less of
a headache.

Currently, embedding SPARQL queries into a JVM project that uses Jena ARQ is not
easily maintainable. The two most common methods are to load the queries from
external files, which introduces some non-trivial overhead, or to use Jena ARQ's
ParameterizedSparqlString construct, which can get messy and difficult to
maintain. Instead, we propose Arachne: a library that allows you to statically
build SPARQL queries that are embedded at compile-time using a much more natural
interface than ParameterizedSparqlStrings.

## Vision

Please see our [vision document](VISION.md) for insight into our vision, goals,
and roadmap for this project.

## Status

Please note, this project is in early development. It is not feature complete
and it might have bugs.

## Why Kotlin?

Kotlin was chosen as the implementation language of choice for its extremely
powerful builder pattern construct. With Kotlin's Builder Pattern, one can
develop a Kotlin API whose usage and syntax is extremely similar to native
SPARQL queries. Similar concepts have already been proven with Kotlin's HTML
builder project.

Kotlin also provides a much cleaner syntax than Java, provides more robust
safety nets, and is currently not bogged down by artifacts that have sprung up
from decades of strict backwards-compatibility. Couple this with its seamless
integration with Java and you have an ideal candidate for implementation.

## Building

This library is built using gradle, which is packaged with this library's source
code. Building is as simple as executing `./gradlew build` on \*Nix systems or
`gradlew.bat build` on Windows systems.

## Contributing

Please see [CONTRIBUTING](CONTRIBUTING.md) for more information.

## License

Arachne is released under the [Apache 2.0 license](LICENSE).

This software is provided on an "AS IS" basis without warranty. Please see
section 7 of the License for more information.

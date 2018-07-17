# Arachne Octovision

## Prologue

Arachne was born as a 48-hour shipit project hosted by Praeses, LLC. At that 
time, our company had been using Semantic Web technologies for a few years, and 
we were beginning to see some stresses to interfacing with the technology using
JVM languages. So our team decided to come together and see what we could 
produce in 48 hours that would make interfacing with Semantic Web technologies 
via SPARQL simpler and less of a headache in the JVM echosphere.

Our approach was two-pronged. We had two areas we wanted to tackle in our 
48-hour shipit contest: embedding SPARQL queries in JVM projects using a JVM 
language, and developing an Object Mapper between POJOs and RDF constructs. 
These two capabilities were developed in parallel between two subgroups during 
the contest.

## Design Goals

### SPARQL Query Interface

For this functionality, our development is driven by the following design goals:

* Must be intuitive to use, such that anyone that knows SPARQL can easily 
utilize this library
* Must look as close to native SPARQL as possible
* Must guide users to developing proper queries
* Must do all it can to prevent the possibility of malformed queries
* Must be statically typed
* Must weed out as many error vectors as possible
* Must facilitate query segment reuse between query generations

These goals stem from use cases that we have in our own projects that utilize 
Semantic Web technologies.

## Roadmap

With the project's history and design goals out of the way, let's talk about the 
future of this project.

### SPARQL Query Interface

The long-term features we would like to provide are as follows:

1. A SPARQL-like API for writing SPARQL queries in Kotlin for embedding in JVM 
applications, using Jena ARQ
2. A utility program that can generate this Kotlin code from normal native 
SPARQL query files
3. Maven and gradle plugins that wrap said utility program for automated 
generation
4. Target other SPARQL interfaces besides Jena ARQ, or even roll our own

### Object Mapper

For now the Object Mapper functionality is not a priority. We may revisit this 
functionality at a later date, however our current focus is to develop the 
SPARQL query interface first.
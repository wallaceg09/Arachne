package com.praeses.semantic.sparql.dsl

import org.apache.jena.shared.PrefixMapping
import org.apache.jena.sparql.path.PathParser

fun path(str: String) = path(str, PrefixMapping.Standard)
fun path(str: String, prefixMapping: PrefixMapping) = PathParser.parse(str, prefixMapping)
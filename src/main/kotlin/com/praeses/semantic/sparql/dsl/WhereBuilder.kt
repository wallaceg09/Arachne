package com.praeses.semantic.sparql.dsl

import org.apache.jena.graph.Node
import org.apache.jena.graph.Triple
import org.apache.jena.query.Query
import org.apache.jena.sparql.syntax.Element
import org.apache.jena.sparql.syntax.ElementGroup
import org.apache.jena.sparql.syntax.ElementTriplesBlock

/**
 * Where clause builder.
 */
class WhereBuilder {
    private val elements: MutableList<Element> = mutableListOf()

    fun pattern(subject: Node, predicate: Node, obj: Node) {
        val triplesBlock = ElementTriplesBlock()
        triplesBlock.addTriple(Triple(subject, predicate, obj))

        elements.add(triplesBlock)
    }

    internal fun build(query: Query) {
        val mainElementGroup = ElementGroup()
        query.queryPattern = mainElementGroup

        elements.forEach {
            mainElementGroup.addElement(it)
        }
    }
}
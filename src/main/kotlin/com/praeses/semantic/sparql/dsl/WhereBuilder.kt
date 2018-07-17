package com.praeses.semantic.sparql.dsl

import org.apache.jena.query.Query
import org.apache.jena.sparql.syntax.ElementGroup

/**
 * Where clause builder.
 */
class WhereBuilder : PatternBuilder() {
    override fun build(query: Query) {
        val mainElementGroup = ElementGroup()
        query.queryPattern = mainElementGroup

        elements.forEach {
            mainElementGroup.addElement(it)
        }
    }
}
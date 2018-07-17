package com.praeses.semantic.sparql.dsl

import org.apache.jena.query.Query
import org.apache.jena.sparql.syntax.ElementGroup
import org.apache.jena.sparql.syntax.ElementOptional

/**
 * Created by Glen on 9/3/2017.
 */
class OptionalBuilder : PatternBuilder() {
    override fun build(query: Query) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getElement(): ElementOptional {
        val group = ElementGroup()

        elements.forEach { group.addElement(it) }

        return ElementOptional(group)
    }
}
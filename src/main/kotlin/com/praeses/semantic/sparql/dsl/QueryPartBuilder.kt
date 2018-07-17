package com.praeses.semantic.sparql.dsl

import org.apache.jena.query.Query

/**
 * Created by Glen on 9/1/2017.
 */
interface QueryPartBuilder {
    fun build(query: Query)
}
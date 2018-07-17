package com.praeses.semantic.sparql.dsl

import org.apache.jena.sparql.core.Var

/**
 * Created by Glen on 8/31/2017.
 */
class QueryBuilder {
    fun select(vararg vars: Var) = SelectBuilder(*vars)
}
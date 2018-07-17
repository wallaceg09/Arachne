package com.praeses.semantic.sparql.dsl

import org.apache.jena.query.Query
import org.apache.jena.sparql.core.Var

/**
 * Created by Glen on 9/1/2017.
 */
class GroupByBuilder : QueryPartBuilder {
    // FIXME: This isn't actually correct. Look at Query#groupVars...
    // FIXME: Doesn't handle grouping by AVG(x) etc...
    val vars: MutableList<Var> = mutableListOf()

    fun variable(variable: Var) {
        vars.add(variable)
    }

    fun variable(variable: String) {
        vars.add(Var.alloc(variable))
    }

    override fun build(query: Query) {
        vars.forEach { query.addGroupBy(it) }
    }
}
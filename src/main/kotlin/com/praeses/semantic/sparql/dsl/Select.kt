package com.praeses.semantic.sparql.dsl

import org.apache.jena.query.Query
import org.apache.jena.sparql.core.Var

/**
 * Entrypoint for building a Select query.
 */
// TODO: Document...
class Select(dsl: Select.() -> Unit) {
    private val resultVars: MutableList<Var>

    constructor(vararg vars: Var, dsl: Select.() -> Unit) : this(dsl) {
        this.resultVars.addAll(vars)
    }

    init {
        this.dsl()
        this.resultVars = mutableListOf()
    }

    internal fun build() : Query {
        val query = Query()
        query.setQuerySelectType()

        buildSelectClause(query)

        return query
    }

    fun where() {

    }

    fun orderBy() {

    }

    fun groupBy() {

    }

    fun limit() {

    }

    fun offset() {

    }

    fun distinct() {

    }

    fun reduced() {

    }

    private fun buildSelectClause(query: Query) {
        if(resultVars.size > 0) {
            resultVars.forEach {
                query.addResultVar(it)
            }
        } else {
            query.isQueryResultStar = true
        }
    }
}

fun main(args: Array<String>) {
    val a = Var.alloc("a")

    val query = Select(a) {

    }.build()

    println(query)
}
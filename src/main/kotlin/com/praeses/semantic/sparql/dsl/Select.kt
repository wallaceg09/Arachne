package com.praeses.semantic.sparql.dsl

import org.apache.jena.query.Query
import org.apache.jena.sparql.core.Var

/**
 * Entrypoint for building a Select query.
 */
// TODO: Document...
class Select(vararg vars: Var) {
    private val resultVars: MutableList<Var> = mutableListOf()

    private val whereClause: Where = Where()

    internal fun build() : Query {
        val query = Query()
        query.setQuerySelectType()

        buildSelectClause(query)

        whereClause.build(query)

        return query
    }

    fun where(dsl: Where.() -> Unit) : Select {
        whereClause.dsl()

        return this
    }

    fun orderBy(): Select {
        return this
    }

    fun groupBy(): Select {
        return this
    }

    fun limit(): Select {
        return this
    }

    fun offset(): Select {
        return this
    }

    fun distinct(): Select {
        return this
    }

    fun reduced(): Select {
        return this
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
    val b = Var.alloc("b")
    val c = Var.alloc("c")

    val query =
            Select(a)
            .where {
                pattern(a, b, c)
            }
            .distinct()
            .build()

    println(query)
}
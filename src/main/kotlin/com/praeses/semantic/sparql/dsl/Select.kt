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

    private val orderByBuilder: OrderByBuilder = OrderByBuilder()

    private var distinct: Boolean = false

    private var reduced: Boolean = false

    private var limit: Long = Query.NOLIMIT

    private var offset: Long = 0

    fun where(dsl: Where.() -> Unit): Select {
        whereClause.dsl()

        return this
    }

    fun orderBy(dsl: OrderByBuilder.() -> Unit): Select {
        orderByBuilder.dsl()
        return this
    }

    fun groupBy(): Select {
        return this
    }

    fun limit(value: Long): Select {
        this.limit = value
        return this
    }

    fun offset(value: Long): Select {
        this.offset = value
        return this
    }

    fun distinct(): Select {
        this.distinct = true
        this.reduced = false

        return this
    }

    fun reduced(): Select {
        this.reduced = true
        this.distinct = false
        return this
    }

    internal fun build(): Query {
        val query = Query()
        query.setQuerySelectType()

        buildSelectClause(query)
        buildLimit(query)
        buildOffset(query)
        buildDistinct(query)
        buildReduced(query)

        whereClause.build(query)
        orderByBuilder.build(query)

        return query
    }

    private fun buildSelectClause(query: Query) {
        if (resultVars.size > 0) {
            resultVars.forEach {
                query.addResultVar(it)
            }
        } else {
            query.isQueryResultStar = true
        }
    }

    private fun buildLimit(query: Query) {
        if (limit > 0) {
            query.limit = limit
        }
    }

    private fun buildOffset(query: Query) {
        if (offset > 0) {
            query.offset = offset
        }
    }

    private fun buildDistinct(query: Query) {
        if (distinct) {
            query.isDistinct = true
        }
    }

    private fun buildReduced(query: Query) {
        if (reduced) {
            query.isReduced = true
        }
    }
}

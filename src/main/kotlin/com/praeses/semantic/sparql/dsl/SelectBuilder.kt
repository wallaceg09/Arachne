package com.praeses.semantic.sparql.dsl

import org.apache.jena.query.Query
import org.apache.jena.sparql.core.Var

/**
 * Entrypoint for building a Select query.
 */
// TODO: Document...
class SelectBuilder(vararg vars: Var) {
    private val resultVars: MutableList<Var> = mutableListOf()

    private val whereBuilder: WhereBuilder = WhereBuilder()

    private val orderByBuilder: OrderByBuilder = OrderByBuilder()

    private val groupByBuilder: GroupByBuilder = GroupByBuilder()

    private var distinct: Boolean = false

    private var reduced: Boolean = false

    private var limit: Long = Query.NOLIMIT

    private var offset: Long = 0

    init {
        resultVars.addAll(vars)
    }

    fun where(dsl: WhereBuilder.() -> Unit): SelectBuilder {
        whereBuilder.dsl()

        return this
    }

    fun orderBy(dsl: OrderByBuilder.() -> Unit): SelectBuilder {
        orderByBuilder.dsl()
        return this
    }

    fun groupBy(dsl: GroupByBuilder.() -> Unit): SelectBuilder {
        groupByBuilder.dsl()
        return this
    }

    fun limit(value: Long): SelectBuilder {
        this.limit = value
        return this
    }

    fun offset(value: Long): SelectBuilder {
        this.offset = value
        return this
    }

    fun distinct(): SelectBuilder {
        this.distinct = true
        this.reduced = false

        return this
    }

    fun reduced(): SelectBuilder {
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

        whereBuilder.build(query)
        orderByBuilder.build(query)
        groupByBuilder.build(query)

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

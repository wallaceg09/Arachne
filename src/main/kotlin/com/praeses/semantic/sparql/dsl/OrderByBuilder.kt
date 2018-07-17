package com.praeses.semantic.sparql.dsl

import org.apache.jena.graph.Node
import org.apache.jena.query.Query
import org.apache.jena.query.SortCondition
import org.apache.jena.sparql.core.Var
import org.apache.jena.sparql.expr.Expr

/**
 * Created by Glen on 8/31/2017.
 */
class OrderByBuilder : QueryPartBuilder {
    private val conditions: MutableList<SortCondition> = mutableListOf()

    fun expression(expr: Expr, direction: Int = Query.ORDER_DEFAULT) {
        conditions.add(SortCondition(expr, direction))
    }

    fun variable(variable: Var, direction: Int = Query.ORDER_DEFAULT) {
        node(variable, direction)
    }
    fun variable(variableString: String, direction: Int = Query.ORDER_DEFAULT) {
        node(Var.alloc(variableString), direction)
    }

    fun node(node: Node, direction: Int = Query.ORDER_DEFAULT) {
        conditions.add(SortCondition(node, direction))
    }

    fun condition(condition: SortCondition) {
        conditions.add(condition)
    }

    override fun build(query: Query) {
        conditions.forEach { query.addOrderBy(it) }
    }
}
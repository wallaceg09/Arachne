package com.praeses.semantic.sparql.dsl

import org.apache.jena.graph.Node
import org.apache.jena.rdf.model.RDFNode
import org.apache.jena.rdf.model.ResourceFactory
import org.apache.jena.sparql.expr.E_Coalesce
import org.apache.jena.sparql.expr.Expr
import org.apache.jena.sparql.expr.ExprList
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode

/**
 * Created by Glen on 9/9/2017.
 */
class CoalesceBuilder internal constructor(lambda: CoalesceBuilder.() -> Unit) {
    private val expressions: MutableList<Expr> = mutableListOf()

    fun add(expr: Expr) {
        expressions.add(expr)
    }

    fun add(node: Node) {
        add(NodeValueNode(node))
    }

    fun add(rdfNode: RDFNode) {
        add(rdfNode.asNode())
    }

    fun add(any: Any) {
        add(ResourceFactory.createTypedLiteral(any))
    }

    fun build() = E_Coalesce(ExprList(expressions))

    init {
        lambda.invoke(this)
    }
}
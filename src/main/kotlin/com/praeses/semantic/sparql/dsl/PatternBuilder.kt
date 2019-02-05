package com.praeses.semantic.sparql.dsl

import org.apache.jena.graph.Node
import org.apache.jena.graph.Triple
import org.apache.jena.rdf.model.Literal
import org.apache.jena.rdf.model.RDFNode
import org.apache.jena.rdf.model.ResourceFactory
import org.apache.jena.sparql.core.TriplePath
import org.apache.jena.sparql.core.Var
import org.apache.jena.sparql.expr.*
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode
import org.apache.jena.sparql.path.Path
import org.apache.jena.sparql.syntax.*

/**
 * Created by Glen on 9/3/2017.
 */
abstract class PatternBuilder : QueryPartBuilder {
    protected val elements: MutableList<Element> = mutableListOf()

    fun pattern(subject: Node, predicate: Node, obj: Node) {
        pattern(Triple(subject, predicate, obj))
    }

    fun pattern(subject: Node, path: Path, obj: Node) {
        val triplePath = TriplePath(subject, path, obj)
        val elementPath = ElementPathBlock().apply { addTriple(triplePath) }
        pattern(elementPath)
    }

    fun pattern(triple: Triple) {
        val triplesBlock = ElementTriplesBlock()
        triplesBlock.addTriple(triple)

        pattern(triplesBlock)
    }

    private fun pattern(element: Element) {
        elements.add(element)
    }

    fun optional(dsl: OptionalBuilder.() -> Unit) {
        val optional = OptionalBuilder()
        optional.dsl()
        elements.add(optional.getElement())
    }

    fun filter(expr: Expr) {
        elements.add(ElementFilter(expr))
    }

    /*
     * *************************************************************
     * Various Extension Functions to make writing queries simpler.
     * *************************************************************
     */

    // Logical functions

    infix fun Expr.and(other: Expr) = E_LogicalAnd(this, other)
    infix fun Expr.and(other: Node) = this and NodeValueNode(other)
    infix fun Expr.and(other: RDFNode) = this and other.asNode()
    infix fun Expr.and(other: Any) = this and createTypedLiteral(other)

    infix fun Node.and(other: Expr) = NodeValueNode(this) and other
    infix fun Node.and(other: Node) = this and NodeValueNode(other)
    infix fun Node.and(other: RDFNode) = this and other.asNode()
    infix fun Node.and(other: Any) = this and createTypedLiteral(other)

    infix fun RDFNode.and(other: Expr) = this.asNode() and other
    infix fun RDFNode.and(other: Node) = this and NodeValueNode(other)
    infix fun RDFNode.and(other: RDFNode) = this and other.asNode()
    infix fun RDFNode.and(other: Any) = this and createTypedLiteral(other)

    infix fun Any.and(other: Expr) = createTypedLiteral(this) and other
    infix fun Any.and(other: Node) = this and NodeValueNode(other)
    infix fun Any.and(other: RDFNode) = this and other.asNode()
    infix fun Any.and(other: Any) = this and createTypedLiteral(other)

    infix fun Expr.or(other: Expr) = E_LogicalOr(this, other)
    infix fun Expr.or(other: Node) = this or NodeValueNode(other)
    infix fun Expr.or(other: RDFNode) = this or other.asNode()
    infix fun Expr.or(other: Any) = this or createTypedLiteral(other)

    infix fun Node.or(other: Expr) = NodeValueNode(this) or other
    infix fun Node.or(other: Node) = this or NodeValueNode(other)
    infix fun Node.or(other: RDFNode) = this or other.asNode()
    infix fun Node.or(other: Any) = this or createTypedLiteral(other)

    infix fun RDFNode.or(other: Expr) = this.asNode() or other
    infix fun RDFNode.or(other: Node) = this or NodeValueNode(other)
    infix fun RDFNode.or(other: RDFNode) = this or other.asNode()
    infix fun RDFNode.or(other: Any) = this or createTypedLiteral(other)

    infix fun Any.or(other: Expr) = createTypedLiteral(this) or other
    infix fun Any.or(other: Node) = this or NodeValueNode(other)
    infix fun Any.or(other: RDFNode) = this or other.asNode()
    infix fun Any.or(other: Any) = this or createTypedLiteral(other)

    operator fun Expr.not() = E_LogicalNot(this)
    operator fun Node.not() = !NodeValueNode(this)
    operator fun RDFNode.not() = !this.asNode()
    operator fun Any.not() = !createTypedLiteral(this)

    // Mathematical operators

    operator fun Expr.plus(other: Expr) = E_Add(this, other)
    operator fun Expr.plus(other: Node) = this + NodeValueNode(other)
    operator fun Expr.plus(other: Literal) = this + other.asNode()
    operator fun Expr.plus(other: Number) = this + createTypedLiteral(other)

    operator fun Node.plus(other: Node) = NodeValueNode(this) + NodeValueNode(other)
    operator fun Node.plus(other: Expr) = NodeValueNode(this) + other
    operator fun Node.plus(other: Literal) = NodeValueNode(this) + other.asNode()
    operator fun Node.plus(other: Number) = this + createTypedLiteral(other)

    operator fun Literal.plus(other: Literal) = this.asNode() + other.asNode()
    operator fun Literal.plus(other: Expr) = this.asNode() + other
    operator fun Literal.plus(other: Node) = this.asNode() + other
    operator fun Literal.plus(other: Number) = this + createTypedLiteral(other)

    operator fun Number.plus(other: Expr) = createTypedLiteral(this) + other
    operator fun Number.plus(other: Literal) = createTypedLiteral(this) + other
    operator fun Number.plus(other: Node) = createTypedLiteral(this) + other

    operator fun Expr.minus(other: Expr) = E_Subtract(this, other)
    operator fun Expr.minus(other: Node) = this - NodeValueNode(other)
    operator fun Expr.minus(other: Literal) = this - other.asNode()
    operator fun Expr.minus(other: Number) = this - createTypedLiteral(other)

    operator fun Node.minus(other: Node) = NodeValueNode(this) - NodeValueNode(other)
    operator fun Node.minus(other: Expr) = NodeValueNode(this) - other
    operator fun Node.minus(other: Literal) = NodeValueNode(this) - other
    operator fun Node.minus(other: Number) = this - createTypedLiteral(other)

    operator fun Literal.minus(other: Literal) = this.asNode() - other
    operator fun Literal.minus(other: Expr) = this.asNode() - other
    operator fun Literal.minus(other: Node) = this.asNode() - other
    operator fun Literal.minus(other: Number) = this - createTypedLiteral(other)

    operator fun Number.minus(other: Expr) = createTypedLiteral(this) - other
    operator fun Number.minus(other: Literal) = createTypedLiteral(this) - other
    operator fun Number.minus(other: Node) = createTypedLiteral(this) - other

    operator fun Expr.times(other: Expr) = E_Multiply(this, other)
    operator fun Expr.times(other: Node) = this * NodeValueNode(other)
    operator fun Expr.times(other: Literal) = this * other.asNode()
    operator fun Expr.times(other: Number) = this * createTypedLiteral(other)

    operator fun Node.times(other: Node) = NodeValueNode(this) * NodeValueNode(other)
    operator fun Node.times(other: Expr) = NodeValueNode(this) * other
    operator fun Node.times(other: Literal) = NodeValueNode(this) * other
    operator fun Node.times(other: Number) = this * createTypedLiteral(other)

    operator fun Literal.times(other: Literal) = this.asNode() * other
    operator fun Literal.times(other: Expr) = this.asNode() * other
    operator fun Literal.times(other: Node) = this.asNode() * other
    operator fun Literal.times(other: Number) = this * createTypedLiteral(other)

    operator fun Number.times(other: Expr) = createTypedLiteral(this) * other
    operator fun Number.times(other: Literal) = createTypedLiteral(this) * other
    operator fun Number.times(other: Node) = createTypedLiteral(this) * other

    operator fun Expr.div(other: Expr) = E_Divide(this, other)
    operator fun Expr.div(other: Node) = this / NodeValueNode(other)
    operator fun Expr.div(other: Literal) = this / other.asNode()
    operator fun Expr.div(other: Number) = this / createTypedLiteral(other)

    operator fun Node.div(other: Node) = NodeValueNode(this) / NodeValueNode(other)
    operator fun Node.div(other: Expr) = NodeValueNode(this) / other
    operator fun Node.div(other: Literal) = NodeValueNode(this) / other.asNode()
    operator fun Node.div(other: Number) = this / createTypedLiteral(other)

    operator fun Literal.div(other: Literal) = this.asNode() / other
    operator fun Literal.div(other: Expr) = this.asNode() / other
    operator fun Literal.div(other: Node) = this.asNode() / other
    operator fun Literal.div(other: Number) = this / createTypedLiteral(other)

    operator fun Number.div(other: Expr) = createTypedLiteral(this) / other
    operator fun Number.div(other: Literal) = createTypedLiteral(this) / other
    operator fun Number.div(other: Node) = createTypedLiteral(this) / other

    operator fun Expr.unaryPlus() = E_UnaryPlus(this)
    operator fun Node.unaryPlus() = +NodeValueNode(this)
    operator fun Literal.unaryPlus() = +this.asNode()
    operator fun Number.unaryPlus() = +createTypedLiteral(this)

    operator fun Expr.unaryMinus() = E_UnaryPlus(this)
    operator fun Node.unaryMinus() = -NodeValueNode(this)
    operator fun Literal.unaryMinus() = -this.asNode()
    operator fun Number.unaryMinus() = -createTypedLiteral(this)

    fun abs(expr: Expr) = E_NumAbs(expr)
    fun abs(node: Node) = abs(NodeValueNode(node))
    fun abs(literal: Literal) = abs(literal.asNode())
    fun abs(number: Number) = abs(createTypedLiteral(number))

    fun ceiling(expr: Expr) = E_NumCeiling(expr)
    fun ceiling(node: Node) = ceiling(NodeValueNode(node))
    fun ceiling(literal: Literal) = ceiling(literal.asNode())
    fun ceiling(number: Number) = ceiling(createTypedLiteral(number))

    fun floor(expr: Expr) = E_NumFloor(expr)
    fun floor(node: Node) = floor(NodeValueNode(node))
    fun floor(literal: Literal) = floor(literal.asNode())
    fun floor(number: Number) = floor(createTypedLiteral(number))

    fun round(expr: Expr) = E_NumRound(expr)
    fun round(node: Node) = round(NodeValueNode(node))
    fun round(literal: Literal) = round(literal.asNode())
    fun round(number: Number) = round(createTypedLiteral(number))

    private fun createTypedLiteral(any: Any) = ResourceFactory.createTypedLiteral(any)

    // Comparison functions

    infix fun Expr.equal_to(other: Expr) = E_Equals(this, other)
    infix fun Expr.equal_to(other: Node) = this equal_to NodeValueNode(other)
    infix fun Expr.equal_to(other: RDFNode) = this equal_to NodeValueNode(other.asNode())
    infix fun Expr.equal_to(other: Any) = this equal_to createTypedLiteral(other)

    infix fun Node.equal_to(other: Node) = NodeValueNode(this) equal_to NodeValueNode(other)
    infix fun Node.equal_to(other: Expr) = NodeValueNode(this) equal_to other
    infix fun Node.equal_to(other: RDFNode) = this equal_to NodeValueNode(other.asNode())
    infix fun Node.equal_to(other: Any) = this equal_to createTypedLiteral(other)

    infix fun RDFNode.equal_to(other: Expr) = NodeValueNode(this.asNode()) equal_to other
    infix fun RDFNode.equal_to(other: Node) = NodeValueNode(this.asNode()) equal_to other
    infix fun RDFNode.equal_to(other: RDFNode) = NodeValueNode(this.asNode()) equal_to NodeValueNode(other.asNode())
    infix fun RDFNode.equal_to(other: Any) = this equal_to createTypedLiteral(other)

    infix fun Any.equal_to(other: Any) = createTypedLiteral(this) equal_to createTypedLiteral(other)
    infix fun Any.equal_to(other: Expr) = createTypedLiteral(this) equal_to other
    infix fun Any.equal_to(other: Node) = createTypedLiteral(this) equal_to other
    infix fun Any.equal_to(other: RDFNode) = createTypedLiteral(this) equal_to other

    infix fun Expr.not_equal_to(other: Expr) = E_NotEquals(this, other)
    infix fun Expr.not_equal_to(other: Node) = this not_equal_to NodeValueNode(other)
    infix fun Expr.not_equal_to(other: RDFNode) = this not_equal_to NodeValueNode(other.asNode())
    infix fun Expr.not_equal_to(other: Any) = this not_equal_to createTypedLiteral(other)

    infix fun Node.not_equal_to(other: Node) = NodeValueNode(this) not_equal_to NodeValueNode(other)
    infix fun Node.not_equal_to(other: Expr) = NodeValueNode(this) not_equal_to other
    infix fun Node.not_equal_to(other: RDFNode) = this not_equal_to NodeValueNode(other.asNode())
    infix fun Node.not_equal_to(other: Any) = this not_equal_to createTypedLiteral(other)

    infix fun RDFNode.not_equal_to(other: Expr) = NodeValueNode(this.asNode()) not_equal_to other
    infix fun RDFNode.not_equal_to(other: Node) = NodeValueNode(this.asNode()) not_equal_to other
    infix fun RDFNode.not_equal_to(other: RDFNode) = NodeValueNode(this.asNode()) not_equal_to NodeValueNode(other.asNode())
    infix fun RDFNode.not_equal_to(other: Any) = this not_equal_to createTypedLiteral(other)

    infix fun Any.not_equal_to(other: Any) = createTypedLiteral(this) not_equal_to createTypedLiteral(other)
    infix fun Any.not_equal_to(other: Expr) = createTypedLiteral(this) not_equal_to other
    infix fun Any.not_equal_to(other: Node) = createTypedLiteral(this) not_equal_to other
    infix fun Any.not_equal_to(other: RDFNode) = createTypedLiteral(this) not_equal_to other

    infix fun Expr.greater_than(other: Expr) = E_GreaterThan(this, other)
    infix fun Expr.greater_than(other: Node) = this greater_than NodeValueNode(other)
    infix fun Expr.greater_than(other: RDFNode) = this greater_than NodeValueNode(other.asNode())
    infix fun Expr.greater_than(other: Any) = this greater_than createTypedLiteral(other)

    infix fun Node.greater_than(other: Node) = NodeValueNode(this) greater_than NodeValueNode(other)
    infix fun Node.greater_than(other: Expr) = NodeValueNode(this) greater_than other
    infix fun Node.greater_than(other: RDFNode) = this greater_than NodeValueNode(other.asNode())
    infix fun Node.greater_than(other: Any) = this greater_than createTypedLiteral(other)

    infix fun RDFNode.greater_than(other: Expr) = NodeValueNode(this.asNode()) greater_than other
    infix fun RDFNode.greater_than(other: Node) = NodeValueNode(this.asNode()) greater_than other
    infix fun RDFNode.greater_than(other: RDFNode) = NodeValueNode(this.asNode()) greater_than NodeValueNode(other.asNode())
    infix fun RDFNode.greater_than(other: Any) = this greater_than createTypedLiteral(other)

    infix fun Any.greater_than(other: Any) = createTypedLiteral(this) greater_than createTypedLiteral(other)
    infix fun Any.greater_than(other: Expr) = createTypedLiteral(this) greater_than other
    infix fun Any.greater_than(other: Node) = createTypedLiteral(this) greater_than other
    infix fun Any.greater_than(other: RDFNode) = createTypedLiteral(this) greater_than other

    infix fun Expr.greater_than_equal(other: Expr) = E_GreaterThanOrEqual(this, other)
    infix fun Expr.greater_than_equal(other: Node) = this greater_than_equal NodeValueNode(other)
    infix fun Expr.greater_than_equal(other: RDFNode) = this greater_than_equal NodeValueNode(other.asNode())
    infix fun Expr.greater_than_equal(other: Any) = this greater_than_equal createTypedLiteral(other)

    infix fun Node.greater_than_equal(other: Node) = NodeValueNode(this) greater_than_equal NodeValueNode(other)
    infix fun Node.greater_than_equal(other: Expr) = NodeValueNode(this) greater_than_equal other
    infix fun Node.greater_than_equal(other: RDFNode) = this greater_than NodeValueNode(other.asNode())
    infix fun Node.greater_than_equal(other: Any) = this greater_than_equal createTypedLiteral(other)

    infix fun RDFNode.greater_than_equal(other: Expr) = NodeValueNode(this.asNode()) greater_than_equal other
    infix fun RDFNode.greater_than_equal(other: Node) = NodeValueNode(this.asNode()) greater_than_equal other
    infix fun RDFNode.greater_than_equal(other: RDFNode) = NodeValueNode(this.asNode()) greater_than_equal NodeValueNode(other.asNode())
    infix fun RDFNode.greater_than_equal(other: Any) = this greater_than_equal createTypedLiteral(other)

    infix fun Any.greater_than_equal(other: Any) = createTypedLiteral(this) greater_than_equal createTypedLiteral(other)
    infix fun Any.greater_than_equal(other: Expr) = createTypedLiteral(this) greater_than_equal other
    infix fun Any.greater_than_equal(other: Node) = createTypedLiteral(this) greater_than_equal other
    infix fun Any.greater_than_equal(other: RDFNode) = createTypedLiteral(this) greater_than_equal other

    infix fun Expr.less_than(other: Expr) = E_LessThan(this, other)
    infix fun Expr.less_than(other: Node) = this less_than NodeValueNode(other)
    infix fun Expr.less_than(other: RDFNode) = this less_than NodeValueNode(other.asNode())
    infix fun Expr.less_than(other: Any) = this less_than createTypedLiteral(other)

    infix fun Node.less_than(other: Node) = NodeValueNode(this) less_than NodeValueNode(other)
    infix fun Node.less_than(other: Expr) = NodeValueNode(this) less_than other
    infix fun Node.less_than(other: RDFNode) = this less_than NodeValueNode(other.asNode())
    infix fun Node.less_than(other: Any) = this less_than createTypedLiteral(other)

    infix fun RDFNode.less_than(other: Expr) = NodeValueNode(this.asNode()) less_than other
    infix fun RDFNode.less_than(other: Node) = NodeValueNode(this.asNode()) less_than other
    infix fun RDFNode.less_than(other: RDFNode) = NodeValueNode(this.asNode()) less_than NodeValueNode(other.asNode())
    infix fun RDFNode.less_than(other: Any) = this less_than createTypedLiteral(other)

    infix fun Any.less_than(other: Any) = createTypedLiteral(this) less_than createTypedLiteral(other)
    infix fun Any.less_than(other: Expr) = createTypedLiteral(this) less_than other
    infix fun Any.less_than(other: Node) = createTypedLiteral(this) less_than other
    infix fun Any.less_than(other: RDFNode) = createTypedLiteral(this) less_than other

    infix fun Expr.less_than_equal(other: Expr) = E_LessThanOrEqual(this, other)
    infix fun Expr.less_than_equal(other: Node) = this less_than_equal NodeValueNode(other)
    infix fun Expr.less_than_equal(other: RDFNode) = this less_than_equal NodeValueNode(other.asNode())
    infix fun Expr.less_than_equal(other: Any) = this less_than_equal createTypedLiteral(other)

    infix fun Node.less_than_equal(other: Node) = NodeValueNode(this) less_than_equal NodeValueNode(other)
    infix fun Node.less_than_equal(other: Expr) = NodeValueNode(this) less_than_equal other
    infix fun Node.less_than_equal(other: RDFNode) = this less_than_equal NodeValueNode(other.asNode())
    infix fun Node.less_than_equal(other: Any) = this less_than_equal createTypedLiteral(other)

    infix fun RDFNode.less_than_equal(other: Expr) = NodeValueNode(this.asNode()) less_than_equal other
    infix fun RDFNode.less_than_equal(other: Node) = NodeValueNode(this.asNode()) less_than_equal other
    infix fun RDFNode.less_than_equal(other: RDFNode) = NodeValueNode(this.asNode()) less_than_equal NodeValueNode(other.asNode())
    infix fun RDFNode.less_than_equal(other: Any) = this less_than_equal createTypedLiteral(other)

    infix fun Any.less_than_equal(other: Any) = createTypedLiteral(this) less_than_equal createTypedLiteral(other)
    infix fun Any.less_than_equal(other: Expr) = createTypedLiteral(this) less_than_equal other
    infix fun Any.less_than_equal(other: Node) = createTypedLiteral(this) less_than_equal other
    infix fun Any.less_than_equal(other: RDFNode) = createTypedLiteral(this) less_than_equal other

    // Functions

    fun bNode() = E_BNode()
    fun bNode(expr: Expr) = E_BNode(expr)
    fun bNode(node: Node) = bNode(NodeValueNode(node))
    fun bNode(rdfNode: RDFNode) = bNode(rdfNode.asNode())
    fun bNode(any: Any) = bNode(createTypedLiteral(any))

    fun bound(expr: Expr) = E_Bound(expr)
    fun bound(node: Node) = bound(NodeValueNode(node))
    fun bound(rdfNode: RDFNode) = bound(rdfNode.asNode())
    fun bound(any: Any) = bound(createTypedLiteral(any))

    fun bind(expr: Expr, `as`: Var) {
        elements.add(ElementBind(`as`, expr))
    }
    fun bind(node: Node, `as`: Var) {
        bind(NodeValueNode(node), `as`)
    }
    fun bind(rdfNode: RDFNode, `as`: Var) {
        bind(rdfNode.asNode(), `as`)
    }
    fun bind(any: Any, `as`: Var) {
        bind(createTypedLiteral(any), `as`)
    }

    fun coalesce(lambda: CoalesceBuilder.() -> Unit) = CoalesceBuilder(lambda).build()

    // TODO: See if this needs to be expanded?
    fun conditional(condition: Expr, thenExpr: Expr, elseExpr: Expr) = E_Conditional(condition, thenExpr, elseExpr)

    fun datatype(expr: Expr) = E_Datatype(expr)
    fun datatype(node: Node) = datatype(NodeValueNode(node))
    fun datatype(rdfNode: RDFNode) = datatype(rdfNode.asNode())
    fun datatype(any: Any) = datatype(createTypedLiteral(any))

    fun dateTimeDay(expr: Expr) = E_DateTimeDay(expr)
    fun dateTimeDay(node: Node) = dateTimeDay(NodeValueNode(node))
    fun dateTimeDay(rdfNode: RDFNode) = dateTimeDay(rdfNode.asNode())
    fun dateTimeDay(any: Any) = dateTimeDay(createTypedLiteral(any))

    fun dateTimeHours(expr: Expr) = E_DateTimeHours(expr)
    fun dateTimeHours(node: Node) = dateTimeHours(NodeValueNode(node))
    fun dateTimeHours(rdfNode: RDFNode) = dateTimeHours(rdfNode.asNode())
    fun dateTimeHours(any: Any) = dateTimeHours(createTypedLiteral(any))

    fun dateTimeMinutes(expr: Expr) = E_DateTimeMinutes(expr)
    fun dateTimeMinutes(node: Node) = dateTimeMinutes(NodeValueNode(node))
    fun dateTimeMinutes(rdfNode: RDFNode) = dateTimeMinutes(rdfNode.asNode())
    fun dateTimeMinutes(any: Any) = dateTimeMinutes(createTypedLiteral(any))

    fun dateTimeMonth(expr: Expr) = E_DateTimeMonth(expr)
    fun dateTimeMonth(node: Node) = dateTimeMonth(NodeValueNode(node))
    fun dateTimeMonth(rdfNode: RDFNode) = dateTimeMonth(rdfNode.asNode())
    fun dateTimeMonth(any: Any) = dateTimeMonth(createTypedLiteral(any))

    fun dateTimeSeconds(expr: Expr) = E_DateTimeSeconds(expr)
    fun dateTimeSeconds(node: Node) = dateTimeSeconds(NodeValueNode(node))
    fun dateTimeSeconds(rdfNode: RDFNode) = dateTimeSeconds(rdfNode.asNode())
    fun dateTimeSeconds(any: Any) = dateTimeSeconds(createTypedLiteral(any))

    fun dateTimeTimezone(expr: Expr) = E_DateTimeTimezone(expr)
    fun dateTimeTimezone(node: Node) = dateTimeTimezone(NodeValueNode(node))
    fun dateTimeTimezone(rdfNode: RDFNode) = dateTimeTimezone(rdfNode.asNode())
    fun dateTimeTimezone(any: Any) = dateTimeTimezone(createTypedLiteral(any))

    fun dateTimeTZ(expr: Expr) = E_DateTimeTZ(expr)
    fun dateTimeTZ(node: Node) = dateTimeTZ(NodeValueNode(node))
    fun dateTimeTZ(rdfNode: RDFNode) = dateTimeTZ(rdfNode.asNode())
    fun dateTimeTZ(any: Any) = dateTimeTZ(createTypedLiteral(any))

    fun dateTimeYear(expr: Expr) = E_DateTimeYear(expr)
    fun dateTimeYear(node: Node) = dateTimeHours(NodeValueNode(node))
    fun dateTimeYear(rdfNode: RDFNode) = dateTimeHours(rdfNode.asNode())
    fun dateTimeYear(any: Any) = dateTimeHours(createTypedLiteral(any))

    // TODO: Exists
    // TODO: Function
    // TODO: Function Dynamic

    fun iri(expr: Expr) = E_IRI(expr)
    fun iri(node: Node) = iri(NodeValueNode(node))
    fun iri(rdfNode: RDFNode) = iri(rdfNode.asNode())
    fun iri(any: Any) = iri(createTypedLiteral(any))

    fun iri(expr: Expr, altSymbol: String) = E_IRI(expr, altSymbol)
    fun iri(node: Node, altSymbol: String) = iri(NodeValueNode(node), altSymbol)
    fun iri(rdfNode: RDFNode, altSymbol: String) = iri(rdfNode.asNode(), altSymbol)
    fun iri(any: Any, altSymbol: String) = iri(createTypedLiteral(any), altSymbol)

    fun isBlank(expr: Expr) = E_IsBlank(expr)
    fun isBlank(node: Node) = isBlank(NodeValueNode(node))
    fun isBlank(rdfNode: RDFNode) = isBlank(rdfNode.asNode())
    fun isBlank(any: Any) = isBlank(createTypedLiteral(any))

    fun isIRI(expr: Expr) = E_IsIRI(expr)
    fun isIRI(node: Node) = isIRI(NodeValueNode(node))
    fun isIRI(rdfNode: RDFNode) = isIRI(rdfNode.asNode())
    fun isIRI(any: Any) = isIRI(createTypedLiteral(any))

    fun isIRI(expr: Expr, altSymbol: String) = E_IsIRI(expr, altSymbol)
    fun isIRI(node: Node, altSymbol: String) = isIRI(NodeValueNode(node), altSymbol)
    fun isIRI(rdfNode: RDFNode, altSymbol: String) = isIRI(rdfNode.asNode(), altSymbol)
    fun isIRI(any: Any, altSymbol: String) = isIRI(createTypedLiteral(any), altSymbol)

    fun isLiteral(expr: Expr) = E_IsLiteral(expr)
    fun isLiteral(node: Node) = isIRI(NodeValueNode(node))
    fun isLiteral(rdfNode: RDFNode) = isIRI(rdfNode.asNode())
    fun isLiteral(any: Any) = isIRI(createTypedLiteral(any))

    fun isURI(expr: Expr) = E_IsURI(expr)
    fun isURI(node: Node) = isURI(NodeValueNode(node))
    fun isURI(rdfNode: RDFNode) = isURI(rdfNode.asNode())
    fun isURI(any: Any) = isURI(createTypedLiteral(any))

    fun lang(expr: Expr) = E_Lang(expr)
    fun lang(node: Node) = lang(NodeValueNode(node))
    fun lang(rdfNode: RDFNode) = lang(rdfNode.asNode())
    fun lang(any: Any) = lang(createTypedLiteral(any))

    // TODO: Lang Matches

    fun md5(expr: Expr) = E_MD5(expr)
    fun md5(node: Node) = md5(NodeValueNode(node))
    fun md5(rdfNode: RDFNode) = md5(rdfNode.asNode())
    fun md5(any: Any) = md5(createTypedLiteral(any))

    // TODO: Not Exists
    // TODO: Not One Of

    fun now() = E_Now()

    // TODO: One Of
    // TODO: One Of Base

    fun random() = E_Random()

    // TODO: Regex

    fun sameTerm(left: Expr, right: Expr) = E_SameTerm(left, right)
    fun sameTerm(left: Expr, right: Node) = sameTerm(left, NodeValueNode(right))
    fun sameTerm(left: Expr, right: RDFNode) = sameTerm(left, right.asNode())
    fun sameTerm(left: Expr, right: Any) = sameTerm(left, createTypedLiteral(right))

    fun sameTerm(left: Node, right: Expr) = sameTerm(NodeValueNode(left), right)
    fun sameTerm(left: Node, right: Node) = sameTerm(left, NodeValueNode(right))
    fun sameTerm(left: Node, right: RDFNode) = sameTerm(left, right.asNode())
    fun sameTerm(left: Node, right: Any) = sameTerm(left, createTypedLiteral(right))

    fun sameTerm(left: RDFNode, right: Expr) = sameTerm(left.asNode(), right)
    fun sameTerm(left: RDFNode, right: Node) = sameTerm(left, NodeValueNode(right))
    fun sameTerm(left: RDFNode, right: RDFNode) = sameTerm(left, right.asNode())
    fun sameTerm(left: RDFNode, right: Any) = sameTerm(left, createTypedLiteral(right))

    fun sameTerm(left: Any, right: Expr) = sameTerm(createTypedLiteral(left), right)
    fun sameTerm(left: Any, right: Node) = sameTerm(left, NodeValueNode(right))
    fun sameTerm(left: Any, right: RDFNode) = sameTerm(left, right.asNode())
    fun sameTerm(left: Any, right: Any) = sameTerm(left, createTypedLiteral(right))

    fun sha1(expr: Expr) = E_SHA1(expr)
    fun sha1(node: Node) = sha1(NodeValueNode(node))
    fun sha1(rdfNode: RDFNode) = sha1(rdfNode.asNode())
    fun sha1(any: Any) = sha1(createTypedLiteral(any))

    fun sha224(expr: Expr) = E_SHA224(expr)
    fun sha224(node: Node) = sha224(NodeValueNode(node))
    fun sha224(rdfNode: RDFNode) = sha224(rdfNode.asNode())
    fun sha224(any: Any) = sha224(createTypedLiteral(any))

    fun sha256(expr: Expr) = E_SHA256(expr)
    fun sha256(node: Node) = sha256(NodeValueNode(node))
    fun sha256(rdfNode: RDFNode) = sha256(rdfNode.asNode())
    fun sha256(any: Any) = sha256(createTypedLiteral(any))

    fun sha384(expr: Expr) = E_SHA384(expr)
    fun sha384(node: Node) = sha384(NodeValueNode(node))
    fun sha384(rdfNode: RDFNode) = sha384(rdfNode.asNode())
    fun sha384(any: Any) = sha384(createTypedLiteral(any))

    fun sha512(expr: Expr) = E_SHA512(expr)
    fun sha512(node: Node) = sha512(NodeValueNode(node))
    fun sha512(rdfNode: RDFNode) = sha512(rdfNode.asNode())
    fun sha512(any: Any) = sha512(createTypedLiteral(any))

    fun str(expr: Expr) = E_Str(expr)
    fun str(node: Node) = str(NodeValueNode(node))
    fun str(rdfNode: RDFNode) = str(rdfNode.asNode())
    fun str(any: Any) = str(createTypedLiteral(any))

    // TODO: strAfter
    // TODO: strBefore
    // TODO: strConcat
    // TODO: strContains
    // TODO: strDatatype

    fun strEncodeForURI(expr: Expr) = E_StrEncodeForURI(expr)
    fun strEncodeForURI(node: Node) = strEncodeForURI(NodeValueNode(node))
    fun strEncodeForURI(rdfNode: RDFNode) = strEncodeForURI(rdfNode.asNode())
    fun strEncodeForURI(any: Any) = strEncodeForURI(createTypedLiteral(any))

    // TODO: strEndsWith
    // TODO: strLang

    fun strLength(expr: Expr) = E_StrLength(expr)
    fun strLength(node: Node) = strLength(NodeValueNode(node))
    fun strLength(rdfNode: RDFNode) = strLength(rdfNode.asNode())
    fun strLength(any: Any) = strLength(createTypedLiteral(any))

    fun strLowerCase(expr: Expr) = E_StrLowerCase(expr)
    fun strLowerCase(node: Node) = strLowerCase(NodeValueNode(node))
    fun strLowerCase(rdfNode: RDFNode) = strLowerCase(rdfNode.asNode())
    fun strLowerCase(any: Any) = strLowerCase(createTypedLiteral(any))

    // TODO: strReplace
    // TODO: strStartsWith
    // TODO: strSubstring

    fun strUpperCase(expr: Expr) = E_StrUpperCase(expr)
    fun strUpperCase(node: Node) = strUpperCase(NodeValueNode(node))
    fun strUpperCase(rdfNode: RDFNode) = strUpperCase(rdfNode.asNode())
    fun strUpperCase(any: Any) = strUpperCase(createTypedLiteral(any))

    // TODO: URI

    fun strUUID() = E_StrUUID()

    fun version() = E_Version()

    // TODO: ExprAggregator...
}
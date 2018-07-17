package com.praeses.semantic.sparql.dsl

import org.apache.jena.graph.Triple
import org.apache.jena.query.Query
import org.apache.jena.rdf.model.ResourceFactory
import org.apache.jena.sparql.core.Var
import org.apache.jena.sparql.syntax.ElementTriplesBlock

/**
 * Created by Glen on 8/31/2017.
 */
fun main(args: Array<String>) {
    val a = Var.alloc("a")
    val b = Var.alloc("b")
    val c = Var.alloc("c")
    val queryBuilder = QueryBuilder()

    val testTriple = ElementTriplesBlock()
    testTriple.addTriple(Triple(a, b, c))

    val query = queryBuilder
            .select(a).distinct()
            .where {
                pattern(a, b, c)
                filter(!((a not_equal_to b) and (b not_equal_to c)) or (a equal_to c))
                filter(a not_equal_to "Hello World")
                filter(a not_equal_to ResourceFactory.createResource("http://www.test.com#hello_world.txt").asNode())
                filter(a not_equal_to b)
                filter(1 equal_to b)
                filter(4 + 6 + (a not_equal_to b) + 3)
                optional {
                    filter(b greater_than c)
                }
                bind(coalesce { add(b); add(c) }, a)
            }
            .limit(10).offset(5)
            .orderBy {
                variable("a")
                variable(b)
                variable(c, Query.ORDER_DESCENDING)
            }
            .build()

    println(query)
}
package com.praeses.semantic.sparql.dsl

import org.apache.jena.sparql.core.Var

/**
 * Created by Glen on 8/31/2017.
 */
fun main(args: Array<String>) {
    val a = Var.alloc("a")
    val b = Var.alloc("b")
    val c = Var.alloc("c")
    val queryBuilder = QueryBuilder()

    val query = queryBuilder
            .select(a).distinct()
            .where {
                pattern(a, b, c)
            }
            .limit(10).offset(5)
            .orderBy {
                variable("a")
            }
            .groupBy {
                variable(b)
            }
            .build()

    println(query)
}
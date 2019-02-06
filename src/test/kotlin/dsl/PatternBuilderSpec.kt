package dsl

import com.praeses.semantic.sparql.dsl.QueryBuilder
import com.praeses.semantic.sparql.dsl.path
import org.apache.jena.query.Query
import org.apache.jena.query.QueryExecution
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.rdf.model.*
import org.apache.jena.shared.PrefixMapping
import org.apache.jena.sparql.core.Var
import org.apache.jena.sparql.vocabulary.FOAF
import org.apache.jena.vocabulary.RDF
import org.junit.jupiter.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PatternBuilderSpec : Spek({
    describe("Basic Database") {
        val a: Var by memoized { Var.alloc("a") }
        val b: Var by memoized { Var.alloc("b") }

        val database: Model by memoized { ModelFactory.createDefaultModel() }

        describe("Basic Graph Pattern") {
            val subject: Resource by memoized { ResourceFactory.createResource("http://hello.world") }
            val predicate: Property by memoized { FOAF.name }
            val objekt: Literal by memoized { ResourceFactory.createStringLiteral("Robert") }

            val queryBuilder: QueryBuilder by memoized { QueryBuilder() }

            val query: Query by memoized {
                queryBuilder
                        .select(a, b)
                        .where {
                            pattern(a, predicate.asNode(), b)
                        }.build()
            }

            val queryExecution: QueryExecution by memoized { QueryExecutionFactory.create(query, database) }

            beforeEach {
                database.add(subject, predicate, objekt)
            }

            afterEach {
                queryExecution.close()
            }

            it("Basic Graph Pattern should return valid results") {
                val results = queryExecution.execSelect()

                val solution = results.nextSolution()

                val returnedA = solution.get(a.varName)
                val returnedB = solution.get(b.varName)

                Assertions.assertEquals(subject, returnedA)
                Assertions.assertEquals(objekt, returnedB)
            }

            it("Basic Graph Pattern should return correct number of results") {
                val results = queryExecution.execSelect()
                results.nextSolution()

                Assertions.assertFalse(results.hasNext())
            }
        }

        describe("Property Path") {
            val jane: Resource by memoized { ResourceFactory.createResource("http://example.com/jane") }

            val jill: Resource by memoized { ResourceFactory.createResource("http://example.com/jill") }
            val jillName: Literal by memoized { ResourceFactory.createStringLiteral("Jill") }

            val jon: Resource by memoized { ResourceFactory.createResource("http://example.com/jon") }

            val prefixMapping: PrefixMapping by memoized {
                PrefixMapping.Factory
                        .create().apply {
                            this.samePrefixMappingAs(PrefixMapping.Standard)
                            this.setNsPrefix("foaf", FOAF.getURI())
                        }
            }

            beforeEach {
                database.add(jane, RDF.type, FOAF.Person)
                database.add(jane, FOAF.name, "Jane")

                database.add(jill, RDF.type, FOAF.Person)
                database.add(jill, FOAF.name, jillName)

                database.add(jon, RDF.type, FOAF.Person)
                database.add(jon, FOAF.name, "Jon")
                database.add(jon, FOAF.surname, jillName)

                database.add(jane, FOAF.knows, jill)
                database.add(jill, FOAF.knows, jon)
            }

            describe("Star Property Path") {
                val query by memoized {
                    QueryBuilder().select(a)
                            .where {
                                pattern(a, path("foaf:knows*", prefixMapping), jon.asNode())
                            }.build()
                }

                val queryExecution: QueryExecution by memoized { QueryExecutionFactory.create(query, database) }

                it("Star Property Path should return correct number of results") {
                    val results = queryExecution.execSelect()

                    repeat(3) {
                        Assertions.assertTrue(results.hasNext())
                        results.nextSolution()
                    }

                    Assertions.assertFalse(results.hasNext())
                }

                it("Star Property Path should return the currect results") {
                    val results = queryExecution.execSelect()

                    val possible = listOf(jane, jill, jon)

                    results.forEach {
                        val returnedA = it[a.varName]
                        Assertions.assertTrue(possible.contains(returnedA))
                    }
                }
            }

            describe("Plus Property Path") {
                val query by memoized {
                    QueryBuilder().select(a)
                            .where {
                                pattern(a, path("foaf:knows+", prefixMapping), jon.asNode())
                            }.build()
                }

                val queryExecution: QueryExecution by memoized { QueryExecutionFactory.create(query, database) }

                it("Plus Property Path should return correct number of results") {
                    val results = queryExecution.execSelect()

                    repeat(2) {
                        Assertions.assertTrue(results.hasNext())
                        results.nextSolution()
                    }

                    Assertions.assertFalse(results.hasNext())
                }

                it("Plus Property Path should return the correct results") {
                    val results = queryExecution.execSelect()

                    val possible = listOf(jane, jill)

                    results.forEach {
                        val returnedA = it[a.varName]
                        Assertions.assertTrue(possible.contains(returnedA))
                    }
                }
            }

            describe("Reverse Property Path") {
                val query by memoized {
                    QueryBuilder().select(a)
                            .where {
                                pattern(a, path("^foaf:knows", prefixMapping), jill.asNode())
                            }.build()
                }

                val queryExecution: QueryExecution by memoized { QueryExecutionFactory.create(query, database) }

                it("Reverse Property Path should return correct number of results") {
                    val results = queryExecution.execSelect()

                    Assertions.assertTrue(results.hasNext())
                    results.nextSolution()
                    Assertions.assertFalse(results.hasNext())
                }

                it("Reverse Property Path should return the correct results") {
                    val results = queryExecution.execSelect()

                    results.forEach {
                        val returnedA = it[a.varName]
                        Assertions.assertEquals(jon, returnedA)
                    }
                }
            }

            describe("Sequence Property Path") {
                val query by memoized {
                    QueryBuilder().select(a)
                            .where {
                                pattern(a, path("foaf:knows / foaf:name", prefixMapping), jillName.asNode())
                            }.build()
                }

                val queryExecution: QueryExecution by memoized { QueryExecutionFactory.create(query, database) }

                it("Sequence Property Path should return correct number of results") {
                    val results = queryExecution.execSelect()

                    Assertions.assertTrue(results.hasNext())
                    results.nextSolution()
                    Assertions.assertFalse(results.hasNext())
                }

                it("Sequence Property Path should return the correct results") {
                    val results = queryExecution.execSelect()

                    val result = results.nextSolution()
                    val returnedA = result[a.varName]

                    Assertions.assertEquals(jane, returnedA)
                }
            }

            describe("Alternate Property Path") {
                val query by memoized {
                    QueryBuilder().select(a)
                            .where {
                                pattern(a, path("foaf:name | foaf:surname", prefixMapping), jillName.asNode())
                            }.build()
                }

                val queryExecution: QueryExecution by memoized { QueryExecutionFactory.create(query, database) }

                it("Alternate Property Path should return correct number of results") {
                    val results = queryExecution.execSelect()

                    repeat(2) {
                        Assertions.assertTrue(results.hasNext())
                        results.nextSolution()
                    }

                    Assertions.assertFalse(results.hasNext())
                }

                it("Alternate Property Path should return the correct results") {
                    val results = queryExecution.execSelect()

                    val possible = listOf(jon, jill)

                    results.forEach {
                        val returnedA = it[a.varName]

                        Assertions.assertTrue(possible.contains(returnedA))
                    }

                    Assertions.assertFalse(results.hasNext())

                }
            }
        }
    }
})
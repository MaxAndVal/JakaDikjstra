package com.lambert93.melkius_val.jacquadijkstra

class Edge(val id: String, val source: Vertex, val destination: Vertex, val weight: Int) {

    override fun toString(): String {
        return source.toString() + " " + destination
    }


}
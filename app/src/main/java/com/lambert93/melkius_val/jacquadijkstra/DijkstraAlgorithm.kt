package com.lambert93.melkius_val.jacquadijkstra

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.HashSet
import java.util.LinkedList


class DijkstraAlgorithm(graph: Graph) {

    private val nodes: List<Vertex>
    private val edges: List<Edge>
    private var settledNodes: MutableSet<Vertex>? = null
    private var unSettledNodes: MutableSet<Vertex>? = null
    private var predecessors: MutableMap<Vertex, Vertex>? = null
    private var distance: MutableMap<Vertex, Int>? = null

    init {
        // create a copy of the array so that we can operate on this array
        this.nodes = ArrayList(graph.vertexes)
        this.edges = ArrayList(graph.edges)
    }

    fun execute(source: Vertex) {
        settledNodes = HashSet()
        unSettledNodes = HashSet()
        distance = HashMap()
        predecessors = HashMap()
        distance!![source] = 0
        unSettledNodes!!.add(source)
        while (unSettledNodes!!.size > 0) {
            val node = getMinimum(unSettledNodes!!)
            settledNodes!!.add(node as Vertex)
            unSettledNodes!!.remove(node)
            findMinimalDistances(node)
        }
    }

    private fun findMinimalDistances(node: Vertex?) {
        val adjacentNodes = getNeighbors(node)
        for (target in adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance!![target] = getShortestDistance(node) + getDistance(node, target)
                predecessors?.set(target as Vertex, node as Vertex)
                unSettledNodes!!.add(target)
            }
        }

    }

    private fun getDistance(node: Vertex?, target: Vertex): Int {
        for (edge in edges) {
            if (edge.source == node && edge.destination == target) {
                return edge.weight
            }
        }
        throw RuntimeException("Should not happen")
    }

    private fun getNeighbors(node: Vertex?): List<Vertex> {
        val neighbors = ArrayList<Vertex>()
        for (edge in edges) {
            if (edge.source == node && !isSettled(edge.destination)) {
                neighbors.add(edge.destination)
            }
        }
        return neighbors
    }

    private fun getMinimum(vertexes: Set<Vertex>): Vertex? {
        var minimum: Vertex? = null
        for (vertex in vertexes) {
            if (minimum == null) {
                minimum = vertex
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex
                }
            }
        }
        return minimum
    }

    private fun isSettled(vertex: Vertex): Boolean {
        return settledNodes!!.contains(vertex)
    }

    private fun getShortestDistance(destination: Vertex?): Int {
        val d = distance!![destination]
        return d ?: Integer.MAX_VALUE
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    fun getPath(target: Vertex): LinkedList<Vertex>? {
        val path = LinkedList<Vertex>()
        var step = target
        // check if a path exists
        if (predecessors!![step] == null) {
            return null
        }
        path.add(step)
        while (predecessors!![step] != null) {
            step = predecessors?.get(step)!!
            path.add(step)
        }
        // Put it into the correct order
        Collections.reverse(path)
        return path
    }

}
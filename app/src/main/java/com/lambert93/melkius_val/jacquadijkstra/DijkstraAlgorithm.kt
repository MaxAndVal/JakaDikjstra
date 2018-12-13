package com.lambert93.melkius_val.jacquadijkstra

import java.util.*
import kotlin.collections.ArrayList


class DijkstraAlgorithm(graph: Graph) {

    private val nodes: List<GEO_POINT>
    private val edges: List<GEO_ARC>
    private var settledNodes: MutableSet<GEO_POINT>? = null
    private var unSettledNodes: MutableSet<GEO_POINT>? = null
    private var predecessors: MutableMap<GEO_POINT, GEO_POINT>? = null
    private var distance: MutableMap<GEO_POINT, Float>? = null

    init {
        // create a copy of the array so that we can operate on this array
        this.nodes = ArrayList(graph.nodes)
        val edges1 = ArrayList(graph.edges)
        val reverseEdges = ArrayList<GEO_ARC>()

        //Inversion des arcs pour simuler un graph non orienté
        for (i in 0 until edges1.size) {
            val rev = edges1[i].copy(
                id = edges1[i].id,
                deb = edges1[i].fin,
                fin = edges1[i].deb,
                temps = edges1[i].temps,
                distance = edges1[i].distance,
                sens = edges1[i].sens
            )
            reverseEdges.add(i, rev)
        }
        reverseEdges.addAll(graph.edges)
        this.edges = reverseEdges
    }

    fun execute(source: GEO_POINT) {
        settledNodes = HashSet()
        unSettledNodes = HashSet()
        distance = HashMap()
        predecessors = HashMap()
        distance!![source] = 0.0f
        unSettledNodes!!.add(source)
        while (unSettledNodes!!.size > 0) {
            val node = getMinimum(unSettledNodes!!)
            settledNodes!!.add(node as GEO_POINT)
            unSettledNodes!!.remove(node)
            findMinimalDistances(node)
        }
    }

    private fun findMinimalDistances(node: GEO_POINT) {
        val adjacentNodes = getNeighbors(node)
        for (target in adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance!![target] = getShortestDistance(node) + getDistance(node, target)
                predecessors?.put(target, node)
                unSettledNodes!!.add(target)
            }
        }
    }

    private fun getDistance(node: GEO_POINT, target: GEO_POINT): Float {
        for (edge in edges) {
            if (edge.deb == node.id && edge.fin == target.id) {
                return edge.distance
            }
        }
        //throw RuntimeException("Should not happen")
        return 0.0f
    }

    private fun getNeighbors(node: GEO_POINT): List<GEO_POINT> {
        val neighbors = ArrayList<GEO_POINT>()
        for (edge in edges) {
            val nodeEnd: GEO_POINT? = nodes.find { it.id == edge.fin }
            if (edge.deb == node.id && !isSettled(nodeEnd)) {
                neighbors.add(nodeEnd!!)
            }
        }
        return neighbors
    }

    private fun getMinimum(values: Set<GEO_POINT>): GEO_POINT? {
        var minimum: GEO_POINT? = null
        for (value in values) {
            if (minimum == null) {
                minimum = value
            } else {
                if (getShortestDistance(value) < getShortestDistance(minimum)) {
                    minimum = value
                }
            }
        }
        return minimum
    }

    private fun isSettled(point: GEO_POINT?): Boolean {
        return settledNodes?.contains(point)!!
    }

    private fun getShortestDistance(destination: GEO_POINT?): Float {
        val d = distance!![destination]
        return d ?: Float.MAX_VALUE
    }

    /**
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    fun getPath(target: GEO_POINT): LinkedList<GEO_POINT>? {
        val path = LinkedList<GEO_POINT>()
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
        path.reverse()
        return path
    }

}
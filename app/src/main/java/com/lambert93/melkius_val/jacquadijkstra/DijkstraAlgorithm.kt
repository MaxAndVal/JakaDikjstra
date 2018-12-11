package com.lambert93.melkius_val.jacquadijkstra

import java.util.*


class DijkstraAlgorithm(graph: Graph) {

    private val nodes: List<GEO_POINT>
    private val edges: List<GEO_ARC>
    private var settledNodes: MutableSet<GEO_POINT>? = null
    private var unSettledNodes: MutableSet<GEO_POINT>? = null
    private var predecessors: MutableMap<GEO_POINT, GEO_POINT>? = null
    private var distance: MutableMap<GEO_POINT, Float>? = null

    init {
        // create a copy of the array so that we can operate on this array
        this.nodes = ArrayList(graph.vertexes)
        this.edges = ArrayList(graph.edges)
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

    private fun findMinimalDistances(node: GEO_POINT?) {
        val adjacentNodes = getNeighbors(node)
        for (target in adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance!![target] = getShortestDistance(node) + getDistance(node, target)
                predecessors?.set(target, node!!)
                unSettledNodes!!.add(target)
            }
        }

    }

    private fun getDistance(node: GEO_POINT?, target: GEO_POINT): Float {
        for (edge in edges) {
            if (edge.deb == node?.id && edge.fin == target.id) {
                return edge.distance
            }
        }
        throw RuntimeException("Should not happen")
    }

    private fun getNeighbors(node: GEO_POINT?): List<GEO_POINT> {
        val neighbors = ArrayList<GEO_POINT>()
        for (edge in edges) {
            if (edge.deb == node?.id && !isSettled(node)) {
                neighbors.add(node)
            }
        }
        return neighbors
    }

    private fun getMinimum(vertexes: Set<GEO_POINT>): GEO_POINT? {
        var minimum: GEO_POINT? = null
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

    private fun isSettled(vertex: GEO_POINT): Boolean {
        return settledNodes!!.contains(vertex)
    }

    private fun getShortestDistance(destination: GEO_POINT?): Float {
        val d = distance!![destination]
        return d ?: Float.MAX_VALUE
    }

    /*
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
        Collections.reverse(path)
        return path
    }

}
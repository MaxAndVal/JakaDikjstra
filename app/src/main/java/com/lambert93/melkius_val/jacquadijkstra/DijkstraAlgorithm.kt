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

    /*public void execute(Vertex source) {
        settledNodes = new HashSet<Vertex>();
        unSettledNodes = new HashSet<Vertex>();
        distance = new HashMap<Vertex, Integer>();
        predecessors = new HashMap<Vertex, Vertex>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }*/

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

    /*private void findMinimalDistances(Vertex node) {
        List<Vertex> adjacentNodes = getNeighbors(node);
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }*/

    private fun getDistance(node: GEO_POINT, target: GEO_POINT): Float {
        for (edge in edges) {
            if (edge.deb == node.id && edge.fin == target.id) {
                return edge.distance
            }
        }
        //throw RuntimeException("Should not happen")
        return 0.0f
    }

    /*private int getDistance(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }
*/

    private fun getNeighbors(node: GEO_POINT): List<GEO_POINT> {
        val neighbors = ArrayList<GEO_POINT>()
        for (edge in edges) {
            var nodeEnd: GEO_POINT? = null
            for (node in nodes) {
                if (node.id == edge.fin) {
                    nodeEnd = node
                }
            }
            if (edge.deb == node.id && !isSettled(nodeEnd)) {
                neighbors.add(nodeEnd!!)
            }
        }
        return neighbors
    }

    /*private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }*/

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

    /*private int getShortestDistance(Vertex destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }*/

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
        path.reverse()
        return path
    }

    /*public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }*/

}
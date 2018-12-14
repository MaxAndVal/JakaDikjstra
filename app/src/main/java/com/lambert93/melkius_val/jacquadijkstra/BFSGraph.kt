package com.lambert93.melkius_val.jacquadijkstra

import android.util.Log
import java.util.*

class BFSGraph
internal constructor(
    private val V: Int   // No. of vertices
) {
    private var adj: LinkedList<LinkedList<Int>> = LinkedList()

    init {
        for (i in 0 until V)
            adj.add(LinkedList())
    }

    // Function to add an edge into the graph
    internal fun addEdge(v: Int, w: Int) {
        adj[v].add(w)
    }

    // prints BFS traversal from a given source s
    internal fun BFS(s: Int) {
        var s = s
        // Mark all the vertices as not visited(By default
        // set as false)
        val visited = BooleanArray(V)

        // Create a queue for BFS
        val queue = LinkedList<Int>()

        // Mark the current node as visited and enqueue it
        visited[s] = true
        queue.add(s)

        while (queue.size != 0) {
            // Dequeue a vertex from queue and print it
            s = queue.poll()
            Log.d(TAG, "$s ")
            Log.d(TAG, "$adj")
            print(s.toString() + " ")

            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            val i = adj[s].listIterator()
            while (i.hasNext()) {
                val n = i.next()
                if (!visited[n]) {
                    visited[n] = true
                    queue.add(n)
                }
            }
        }
    }
}

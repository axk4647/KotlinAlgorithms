package sorts

import kotlin.math.min


data class Edge(var from: Int, var to: Int, var weight: Int)

fun topologicalSort(graph: Map<Int, List<Edge>>, numNodes: Int): IntArray {
    val ordering = IntArray(numNodes)
    val visited = BooleanArray(numNodes)
    var i = numNodes - 1

    for (at in 0 until numNodes) {
        if (!visited[at]) {
            i = dfs(i, at, visited, ordering, graph)
        }
    }

    return ordering
}

private fun dfs(
        i: Int, at: Int, visited: BooleanArray, ordering: IntArray, graph: Map<Int, List<Edge>>): Int {

    var index = i
    visited[at] = true
    val edges = graph[at]

    if (edges != null)
        for ((_, to) in edges)
            if (!visited[to])
                index = dfs(index, to, visited, ordering, graph)

    ordering[index] = at
    return index - 1
}


fun dagShortestPath(graph: Map<Int, List<Edge>>, start: Int, numNodes: Int): Array<Int> {
    val sortedGraph: IntArray = topologicalSort(graph, numNodes)
    val dist = Array(numNodes) { Int.MAX_VALUE }
    dist[start] = 0

    for (i in sortedGraph.indices) {
        val nodeIndex: Int = sortedGraph[i]
        val adjacentEdges = graph[nodeIndex]

        if (adjacentEdges != null) {
            for (edge in adjacentEdges) {
                val newDist = dist[nodeIndex] + edge.weight
                if (dist[edge.to] == Int.MAX_VALUE) {
                    dist[edge.to] = newDist
                }

                dist[edge.to] = min(newDist, dist[edge.to])
            }
        }
    }

    return dist
}

fun main() {
    val n = 7
    val graph = mutableMapOf<Int, MutableList<Edge>>()
    for(i in 0 until n) {
        graph[i] = mutableListOf()
    }
    graph[0]?.add(Edge(0, 1, 3))
    graph[0]?.add(Edge(0, 5, 3))
    graph[0]?.add(Edge(0, 2, 2))
    graph[1]?.add(Edge(1, 3, 1))
    graph[1]?.add(Edge(1, 2, 6))

//    graph[2]?.add(Edge(2, 3, 1))
//    graph[2]?.add(Edge(2, 4, 10))
//    graph[3]?.add(Edge(3, 4, 5))

    graph[3]?.add(Edge(3, 2, 1))
    graph[3]?.add(Edge(3, 4, 10))
    graph[2]?.add(Edge(2, 4, 5))


    graph[5]?.add(Edge(5, 4, 7))

    val ordering: IntArray = topologicalSort(graph, n)
    println(ordering.contentToString())

    val dists: Array<Int> = dagShortestPath(graph, 0, n)

    // Find the shortest path from 0 to 4 which is 8.0
    println(dists[4])

    // Find the shortest path from 0 to 6 which
    // is null since 6 is not reachable!
    println(dists[6])
}

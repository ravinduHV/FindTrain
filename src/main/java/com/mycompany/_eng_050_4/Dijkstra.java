
package com.mycompany._eng_050_4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    public static final int INF = Integer.MAX_VALUE;
    public static Result dijkstra(int[][] graph, int start, int end) {
        int n = graph.length;
        int[] dist = new int[n];  //to store shortest distances
        int[] prev = new int[n];  //to store predecessors
        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);    // Initialize predecessors as -1
        dist[start] = 0;

        boolean[] visited = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[] { start, 0 });

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

            if (visited[u]) continue;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (graph[u][v] != -1 && !visited[v]) {
                    int newDist = dist[u] + graph[u][v];
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = u;
                        pq.add(new int[] { v, newDist });
                    }
                }
            }
        }

        return new Result(dist[end], reconstructPath(prev, start, end));
    }

    private static List<Character> reconstructPath(int[] prev, int start, int end) {
        List<Character> path = new ArrayList<>();
        for (int at = end; at != -1; at = prev[at]) {
            path.add((char) ('A' + at));
        }
        Collections.reverse(path);


        if (path.get(0) != (char) ('A' + start)) {
            return Collections.emptyList();
        }
        return path;
    }
}

import java.util.*;

public class 图的无权图单源最短路径广度求解 {
    public static void main(String[] args) {
        int[][] graph = {{4,3,1},{3,2,4},{3},{4},{}};
        int length = graph.length;
        boolean[] visited = new boolean[length];
        int[] due = new int[length];
        Queue<Integer> que = new LinkedList<>();

        visited[0] = true;
        que.add(0);
        due[0] = 0;
        while(!que.isEmpty()){
            int w = que.poll();
            int[] a = graph[w];
            for(int s :a) {
                if (!visited[s]) {
                    visited[s] = true;
                    due[s] = due[w]+1;
                    que.add(s);
                }
            }
        }

        for (int i : due){
            System.out.print(i);
        }
    }

}

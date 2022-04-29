import java.util.*;
public class 图的深度递归 {
    static int[][] graph = {{4,3,1},{3,2,4},{3},{4},{}};
    static int length = graph.length;
    static boolean[] visited = new boolean[length];
    static List<Integer> ans = new LinkedList<>();

    public static void main(String[] args) {
        for (int i = 0; i < length; i++){
            if (!visited[i]){
                DFS(i);
            }
        }
        for (int i : ans) {
            System.out.print(i);
        }
    }

    public static void DFS(int i){
        visited[i] = true;
        ans.add(i);
        int[] a = graph[i];
        for (int s :a){
            if (!visited[s]) DFS(s);
        }
    }
}

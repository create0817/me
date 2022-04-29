import java.util.*;

public class 图的广度递归 {
    static List<Integer> ans = new LinkedList<>();
    static Queue<Integer> que = new LinkedList<>();
    static int[][]graph = {{4,3,1},{3,2,4},{3},{4},{}};
    static int length = graph.length;
    static boolean[] visited = new boolean[length];      //visited会被默认初始化为false

    public static void main(String[] args) {
        for (int i = 0; i < length; i++) {
            if (visited[i]) continue;
            BFS(i);
        }

        for(int i:ans){
            System.out.print(i);
        }
    }

    public static void BFS(int i){
        ans.add(i);
        que.add(i);

        while(!que.isEmpty()){
            int w = que.poll();
            int[] a = graph[w];
            for (int s :a){
                if (!visited[s]){
                    BFS(s);
                }
            }
        }
    }
}

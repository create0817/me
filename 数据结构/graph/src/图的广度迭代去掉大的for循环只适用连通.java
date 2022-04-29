import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class 图的广度迭代去掉大的for循环只适用连通 {
    public static void main(String[] args){
        int[][] graph ={{4,3,1}, {3,2,4}, {3},{4},{}};

        int number = graph.length;
        boolean[] visited = new boolean[number];
        List<Integer> ans = new LinkedList<>();
        for (boolean boo : visited){
            boo = false;
        }
        Queue<Integer> que = new LinkedList<>();

        ans.add(0);
        visited[0] = true;
        que.add(0);
        while(!que.isEmpty()){
            int w = que.poll();
            int[] a = graph[w];
            for(int s :a){
                if (!visited[s]){
                    ans.add(s);
                    visited[s] = true;
                    que.add(s);
                }
            }
        }

        for(int i :ans){
            System.out.print(i);
        }
    }
}

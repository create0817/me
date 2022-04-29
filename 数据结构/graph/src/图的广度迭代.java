import java.util.*;

public class 图的广度迭代 {
    public static void main(String[] args){
        int[][] graph ={{4,3,1}, {3,2,4}, {3},{4},{}};

        int number = graph.length;
        boolean[] visited = new boolean[number];
        List<Integer> ans = new LinkedList<>();
        for (boolean boo : visited){
            boo = false;
        }
        Queue<Integer> que = new LinkedList<>();

        for(int i = 0; i < number; i++){
            if (visited[i]) continue;
            ans.add(i);
            visited[i] = true;
            que.add(i);
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
        }
        for(int i :ans){
            System.out.print(i);
        }
    }
}
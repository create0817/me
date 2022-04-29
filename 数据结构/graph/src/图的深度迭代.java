import java.util.*;

public class 图的深度迭代 {
    public static void main(String[] args) {
        int[][] graph ={{4,3,1},{3,2,4},{3},{4},{}};
        int length = graph.length;
        boolean[] visited = new boolean[length];
        Stack<Integer> stack= new Stack<>();
        List<Integer> ans = new LinkedList<>();

        for(int i = 0; i < length; i++){
            if (!visited[i]){
                stack.push(i);
                visited[i] = true;

                while(!stack.isEmpty()){
                    int w = stack.pop();
                    ans.add(w);
                    int[] a = graph[w];
                    int lengtha = a.length;
                    for (int j = lengtha-1; j >= 0; j--){
                        int s = a[j];
                        if (!visited[s]){
                            stack.push(s);
                            visited[s] = true;
                        }
                    }
                }
            }
        }

        for (int i : ans){
            System.out.print(i);
        }
    }
}

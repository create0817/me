import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class 图的拓扑排序AOV网用栈 {
    public static void main(String[] args) {
        int[][] graph = {{0,1,0,1,0},{0,0,1,1,0},{0,0,0,0,1},{0,0,1,0,1},{0,0,0,0,0,}};
        int length = graph.length;
        int[] indegree = new int[length];
        List<Integer> ans = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < length; i++){                                                         //初始化入度组
            for (int j = 0; j < length; j++){
                if (graph[i][j] == 1) indegree[j]++;
            }
        }


        for (int i = 0; i < length; i++) {                                                          //将入度为0的点压入栈并将入度设为无穷
            if (indegree[i] == 0) {
                stack.push(i);
                indegree[i] = Integer.MAX_VALUE;
            }
        }

        while(!stack.isEmpty()){                                                                    //当栈中还有顶点时循环
            int k = stack.pop();                                                                    //输出一个栈里的节点
            ans.add(k);                                                                             //添加到答案组
            for (int i = 0; i < length; i++){                                                       //将k节点出去的边都去掉
                if (graph[k][i] == 1) indegree[i]--;
            }
            for (int i = 0; i < length; i++) {                                                      //将入度为0的点压入栈并将入度设为无穷
                if (indegree[i] == 0) {
                    stack.push(i);
                    indegree[i] = Integer.MAX_VALUE;
                }
            }
        }

        for (int a : ans){                                                                        //输出答案组
            System.out.print(a+1);
            System.out.print(" ");
        }

        if (ans.size() < length) System.out.println("排序失败，图中存在环路");                         //输出排序结果
        else{
            System.out.println("排序成功");
        }
    }
}

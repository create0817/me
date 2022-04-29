import java.util.ArrayList;
import java.util.List;

public class 图的拓扑排序AOV网只用循环 {
    public static void main(String[] args) {
        int[][] graph = {{0,1,0,1,0},{0,0,1,1,0},{0,0,0,0,1},{0,0,1,0,1},{0,0,0,0,0,}};
        int length = graph.length;
        int[] indegree = new int[length];
        List<Integer> ans = new ArrayList<>();

        for (int i = 0; i < length; i++){                                                         //初始化入度组
            for (int j = 0; j < length; j++){
                if (graph[i][j] == 1) indegree[j]++;
            }
        }

        for (int k = 0; k < length; k++){                                                        //找length次节点
            int index = -1;
            for (int i = 0; i < length; i++){                                                    //遍历一次找入度为0的点
                if (indegree[i] == 0) {
                    ans.add(i);                                                                  //将节点号记录进答案组中
                    index = i;                                                                   //用index记录下节点号
                    indegree[i] = Integer.MAX_VALUE;                                             //访问过了则标记其入度为无穷
                    break;                                                                       //找到一个入度为0的点就赶紧跳出来，避免有两个入度为0的节点混淆
                }                                                                                //另一个避免多个入度为零的节点的方法是将其压入栈，出栈时再处理其出去的边
            }
            for (int i = 0; i < length; i++){                                                    //将此节点的所有出去的边去掉
                if (graph[index][i] == 1) indegree[i]--;
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

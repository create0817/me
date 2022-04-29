
public class 图的最小生成树Prim算法 {
    public static void main(String[] args) {
        int[][] graph = {{0,2,1,3},{2,0,4,5},{1,4,0,6},{3,5,6,0}};
        int length =graph.length;
        boolean[] visited = new boolean[length];
        visited[0] = true;
        int[][] ans = new int[length][2];
        ans[0][0] = 0;                                                                      //将0顶点先放入答案组中
        int[] weight = new int[length];                                                     //定义已知图到每个未知节点的最小权重为权重组

        for (int i = 0; i < length; i++){                                                   //初始化没有边的情况（在本图中不需要）
            for (int j = 0; j < length; j++){
                if (graph[i][j] == 0 ) graph[i][j] = Integer.MAX_VALUE;
            }
            graph[i][i] = 0;                                                                //将节点自己与自己为0
        }


        for (int i = 0; i < length; i++){                                                   //将权重组初始为第一个节点到其他节点的权重
            weight[i] = graph[0][i];
        }

        for (int i = 1; i < length; i++){                                                   //循环length-1次

            int min = Integer.MAX_VALUE;
            int index = -1;
            for (int j = 0; j < length; j++){                                               //找到离已知节点权重最小的节点
                if (visited[j]) continue;
                if (weight[j] != Integer.MAX_VALUE && min > weight[j]){                     //如果已知图到j节点右边且此权值低于当前最小值
                    min = weight[j];
                    index = j;
                }
            }
            if (index == -1) break;                                                         //循环到有节点并不连通（已知图到此节点权值无穷）或所有节点已经并入已知图则结束（此图不用）
            visited[index] = true;                                                          //将新节点标位已知
            ans[i][0] = index;                                                              //将新节点加入答案组中
            ans[i][1] = min;                                                                //将新节点花费的权重值放入答案组

            for (int j = 0; j < length; j++){                                               //看新节点有没有到未知节点更小的权重，有则更新权重组
                if (visited[j]) continue;
                if(graph[index][j] < weight[j]) weight[j] = graph[index][j];
            }
        }

        System.out.println("第1顶点的标号为"+ans[0][0]);
        for(int i = 1; i < length; i++){                                                                  //输出答案组
            System.out.println("第"+i+"条边的权值为"+ans[i][1]);
            System.out.println("第"+(i+1)+"个顶点的标号为"+ans[i][0]);
        }


    }

}

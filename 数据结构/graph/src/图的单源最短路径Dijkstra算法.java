public class 图的单源最短路径Dijkstra算法 {
    static int[][] graph = {{0,0,10,0,30,100},{0,0,5,0,0,0},{0,0,0,50,0,0},{0,0,0,0,0,10},{0,0,0,20,0,60},{0,0,0,0,0,0}};
    static int length = graph.length;

    public static void main(String[] args) {
        int v = 0;                                                                       //设顶点0为源点
        int[] ans = Dijkstra(v);                                                         //调用Dijkstra算法并用ans数组接收
        for (int i :ans) {                                                               //输出ans数组
            System.out.println(i);
        }
    }

    public static int[] Dijkstra(int v){
        if (v < 0 || v >= length) throw new ArrayIndexOutOfBoundsException();          //如果v不是顶点号则报错

        boolean[] visited = new boolean[length];                                       //用boolean组记录哪些顶点已经并入最短路径组了
        int[] distance = new int[length];                                              //用int[]记录源点到现在已知内各个点的最短距离

        for (int i = 0; i < length; i++) {                                             //将不存在边的两个顶点之前的距离改为无限
            for (int j = 0; j < length; j++){
                if (graph[i][j] == 0) graph[i][j] = Integer.MAX_VALUE;
            }
        }
        graph[v][v] = 0;                                                               //源点到自己还是设为0
        for(int i = 0; i < length; i++){                                               //将源点到各个点间的最小距离初始化为源点各个边的值
            distance[i] = graph[v][i];
        }
        visited[v] = true;                                                             //将源点设为已经并入已知图
                                                                                       //目前都为初始化工作，核心在下面

        for (int i = 0; i < length; i++){                                              //一共进行length轮（并入length次顶点）

            int min = Integer.MAX_VALUE;                                               //初始化最小值（为了找到所有未并入顶点中里当前顶点最近的顶点）
            int index = -1;                                                            //初始化这个顶点的值
            for(int j = 0; j < length; j++){                                           //检查每个顶点
                if (visited[j]) continue;                                              //如果这个点已经并入了已知图，则跳过
                if (distance[j] < min){                                                //如果源点目前到这个点的距离小于这次查找中已知的最小距离
                    index = j;                                                         //先记录下顶点号
                    min = distance[j];                                                 //先记录下这个距离（还没看过后面的还不知道是不是最小）
                }
            }                                                                          //查找目前离已知图最近的顶点完毕
            if (index == -1) break;                                                    //如果没找到比无限更小的距离的节点，直接结束
            visited[index] = true;                                                     //如果没有结束，找到了的话将这个顶点并入已知图

            for(int j = 0; j < length; j++){                                           //循环每个顶点
                if (visited[j]) continue;                                              //如果已经在已知图中则跳过
                if (graph[index][j] != Integer.MAX_VALUE &&(graph[index][j]+distance[index]) < distance[j]) distance[j] = graph[index][j]+distance[index];
            }                                                                          //如果新点到这个点存在边且源点到新点的距离加新点到这个点的距离小于原来源点到这个点的距离，则更新最小距离（相当于源点到这个点有了新的路径）
        }

        return distance;                                                               //将最终的源点到各个顶点的距离输出
    }
}

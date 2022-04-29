import java.sql.SQLOutput;

public class 图的全局最短路径Floyd算法 {
    public static void main(String[] args) {
        int[][] graph = {{0,6,13},{10,0,4},{5,0,0}};
        int length = graph.length;
        int[][] distance = new int[length][length];

        for (int i = 0; i < length; i++){
            for (int j = 0; j < length; j++){
                if (graph[i][j] == 0 && i != j) graph[i][j] = Integer.MAX_VALUE;                   //将不存在边的权值设为无穷
                distance[i][j] = graph[i][j];                                                      //初始化距离矩阵为图的基本情况
            }
        }

        for (int i = 0; i < length; i++){                                                          //将每个顶点都作为i顶点来一遍
            for (int j = 0; j < length; j++){
                for (int k = 0; k < length; k++){
                    if (distance[j][i] != Integer.MAX_VALUE && distance[i][k] != Integer.MAX_VALUE && distance[j][i] + distance[i][k] < distance[j][k]){
                        distance[j][k] = distance[j][i] + distance[i][k];                          //当以i为中间节点，如果j节点到k节点经过i权重更小，则用更小的权重
                    }
                }
            }
        }

        for (int[] a : distance){                                                                  //输出距离矩阵
            for (int b :a){
                System.out.print(b);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}

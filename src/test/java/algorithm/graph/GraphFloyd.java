package algorithm.graph;

/**
 * 弗洛伊德算法
 *
 * @author yuxiaoyu
 */
public class GraphFloyd {

    public static void main(String[] args) {
        int p = 4; // 图的所有顶点数
        int I = 3; // 计算顶点的个数
        int J = 3; // 计算顶点的个数
        int K = 3; // 任意两个顶点之间允许经过的点数
        int m = 9999; // 假意无穷大
        int[][] a = new int[p][p];
        a[0][0] = 0; // 顶点0到顶点0之间的距离为 0
        a[0][1] = 2; // 顶点0到顶点1之间的距离为 2
        a[0][2] = 6; // 顶点0到顶点2之间的距离为 6
        a[0][3] = 4; // 顶点0到顶点3之间的距离为 4

        a[1][0] = m;
        a[1][1] = 0;
        a[1][2] = 3;
        a[1][3] = m;

        a[2][0] = 7;
        a[2][1] = m;
        a[2][2] = 0;
        a[2][3] = 1;

        a[3][0] = 5;
        a[3][1] = m;
        a[3][2] = 12;
        a[3][3] = 0;

        // Floyd-Warshall算法核心语句
        for (int k = 0; k <= K; k++) {
            for (int i = 0; i <= I; i++) {
                for (int j = 0; j <= J; j++) {
                    if (a[i][j] > a[i][k] + a[k][j])
                        a[i][j] = a[i][k] + a[k][j];
                }
            }
        }

        for (int i = 0; i <= I; i++) {
            for (int j = 0; j <= J; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }

    }
}

package com.example.sgsserver.game;

//
//        P -> 兵
//        C -> 炮
//        R -> 车
//        N -> 马
//        B -> 象
//        Q -> 士
//        K -> 帅
//        . -> 空
//        -> 棋盘外
//
//大写为红方，小写为黑方
public class Game {
    private final char[][] board = new char[10][9];


    //当前可以操作的玩家 0:红方 1:黑方
    private Boolean nowPlayer = false;

    public char[][] GetBoard() {
        return board;
    }


    //返回,-1:错误操作 0:红方胜利 1:黑方胜利 2:正确操作成功，但没有结束游戏
    public int Operate(String from, String to) {
        int f1 = from.charAt(0) - '0';
        int f2 = from.charAt(1) - '0';
        int t1 = to.charAt(0) - '0';
        int t2 = to.charAt(1) - '0';

        int res = -1;

        char f = board[f1][f2];
        char t = board[t1][t2];
        //边界
        if (t1 > 9 || t1 < 0 || t2 > 8 || t2 < 0) {
            return -1;
        }
        //是否在原地踏步
        if (f1 == t1 && f2 == t2) {
            return -1;
        }
        //是在操作自己棋子，是否误吃自己棋子
        if (nowPlayer) {
            //是否误吃自己棋子
            if (board[t1][t2] <= 'z' && board[t1][t2] >= 'a') {
                return -1;
            }
            //是在操作自己棋子
            if (board[f1][f2] > 'z' || board[f1][f2] < 'a') {
                return -1;
            }
        } else {
            //是否误吃自己棋子
            if (board[t1][t2] <= 'Z' && board[t1][t2] >= 'A') {
                return -1;
            }
            //是在操作自己棋子
            if (board[f1][f2] > 'Z' || board[f1][f2] < 'A') {
                return -1;
            }
        }

        //着法判断
        switch (board[f1][f2]) {
            //红兵
            case 'P':
                //是否是往前走
                if (f1 >= t1) {
                    //是否只走了一格
                    if (f1 - t1 + Math.abs(t2 - f2) == 1) {
                        //判断是否结束游戏
                        if (board[t1][t2] == 'k') {
                            return 0;
                        }
                        //移动
                        board[t1][t2] = 'P';
                        board[f1][f2] = '.';
                        res = 2;
                    }
                } else {
                    return -1;
                }
                break;
            //黑兵
            case 'p':
                //是否是往前走
                if (f1 <= t1) {
                    //是否只走了一格
                    if (t1 - f1 + Math.abs(t2 - f2) == 1) {
                        //判断是否结束游戏
                        if (board[t1][t2] == 'K') {
                            return 1;
                        }
                        //移动
                        board[t1][t2] = 'p';
                        board[f1][f2] = '.';
                        res = 2;
                    }
                } else {
                    return -1;
                }
                break;
            //炮
            case 'C':
            case 'c':
                //判断是否走直线
                if (f1 != t1 && f2 != t2) {
                    return -1;
                }
                if (t != '.') {
                    if (rangeStraight(f1, f2, t1, t2) != 1) {
                        return -1;
                    }
                    //判断是否结束游戏
                    if (t == 'k') {
                        return 0;
                    }
                    if (t == 'K') {
                        return 1;
                    }
                } else {
                    if (rangeStraight(f1, f2, t1, t2) != 0) {
                        return -1;
                    }
                }
                //移动
                board[t1][t2] = f;
                board[f1][f2] = '.';
                res = 2;
                break;
            //车
            case 'r':
            case 'R':
                //判断是否走直线
                if (f1 != t1 && f2 != t2) {
                    return -1;
                }
                //中间是否有间隔
                if (rangeStraight(f1, f2, t1, t2) != 0) {
                    return -1;
                }
                //判断是否结束游戏
                if (t == 'k') {
                    return 0;
                }
                if (t == 'K') {
                    return 1;
                }
                //移动
                board[t1][t2] = f;
                board[f1][f2] = '.';
                res = 2;
                break;
            //马
            case 'N':
            case 'n':
                //是否走的日字
                if (Math.abs(f1 - t1) + Math.abs(f2 - t2) != 3) {
                    return -1;
                }
                //判断马脚
                if (f1 - t1 == 2) {
                    if (board[f1 - 1][f2] != '.') {
                        return -1;
                    }
                }
                if (f1 - t1 == -2) {
                    if (board[f1 + 1][f2] != '.') {
                        return -1;
                    }
                }
                if (f2 - t2 == 2) {
                    if (board[f1][f2 - 1] != '.') {
                        return -1;
                    }
                }
                if (f2 - t2 == -2) {
                    if (board[f1][f2 + 1] != '.') {
                        return -1;
                    }
                }
                //移动
                board[t1][t2] = f;
                board[f1][f2] = '.';
                res = 2;
                break;
            //象
            case 'B':
            case 'b':
                //判断是否过河
                if (t1 > 4 && t == 'b') {
                    return -1;
                }
                if (t1 < 5 && t == 'B') {
                    return -1;
                }
                //是否走田
                if (Math.abs(f1 - t1) != 2 && Math.abs(f2 - t2) != 2) {
                    return -1;
                }
                //判断象脚
                if (board[(f1 + t1) / 2][(f2 + t2) / 2] != '.') {
                    return -1;
                }
                //移动
                board[t1][t2] = f;
                board[f1][f2] = '.';
                res = 2;
                break;
            //士
            case 'q':
            case 'Q':
                //是否走出界限
                if (t2 < 3 || t2 > 5) {
                    return -1;
                }
                if (t1>2&&t1<7)
                {
                    return -1;
                }

                //是否斜着走一格
                if (Math.abs(f1-t1)!=1||Math.abs(f2-t2)!=1){
                    return -1;
                }

                //移动
                board[t1][t2] = f;
                board[f1][f2] = '.';
                res = 2;
                break;
            //帅
            case 'k':
            case 'K':
                //是否走出界限
                if (t2 < 3 || t2 > 5) {
                    return -1;
                }
                if (t1>2&&t1<7)
                {
                    return -1;
                }

                //是否只走了一格
                if (Math.abs(f1-t1)+Math.abs(f2-t2)!=1){
                    return -1;
                }

                //移动
                board[t1][t2] = f;
                board[f1][f2] = '.';
                res = 2;
                break;
        }

        if (res == 2) {
            nowPlayer = !nowPlayer;
        }
        return res;
    }

    //返回起点和终点直线之间包含几个棋子（不包含起点和终点
    private int rangeStraight(int f1, int f2, int t1, int t2) {
        //判断是否为直线
        if (f1 != t1 && f2 != t2) {
            return -1;
        }

        int res = 0;

        if (f1 != t1) {
            if (f1 > t1) {
                for (int i = t1 + 1; i < f1; i++) {
                    if (board[i][f2] != '.') {
                        res++;
                    }
                }
            } else {
                for (int i = f1 + 1; i < t1; i++) {
                    if (board[i][f2] != '.') {
                        res++;
                    }
                }
            }
        } else {
            if (f2 > t2) {
                for (int i = t2 + 1; i < f2; i++) {
                    if (board[f1][i] != '.') {
                        res++;
                    }
                }
            } else {
                for (int i = f2 + 1; i < t2; i++) {
                    if (board[f1][i] != '.') {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    public Boolean getNowPlayer() {
        return nowPlayer;
    }

    public Game() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = '.';
            }
        }

        //初始化黑方棋子
        //车
        board[0][0] = board[0][8] = 'r';
        //马
        board[0][1] = board[0][7] = 'n';
        //象
        board[0][2] = board[0][6] = 'b';
        //士
        board[0][3] = board[0][5] = 'q';
        //将
        board[0][4] = 'k';
        //炮
        board[2][1] = board[2][7] = 'c';
        //兵
        board[3][0] = board[3][2] = board[3][4] = board[3][6] = board[3][8] = 'p';

        //初始化红方棋子
        //车
        board[9][0] = board[9][8] = 'R';
        //马
        board[9][1] = board[9][7] = 'N';
        //象
        board[9][2] = board[9][6] = 'B';
        //士
        board[9][3] = board[9][5] = 'Q';
        //将
        board[9][4] = 'K';
        //炮
        board[7][1] = board[7][7] = 'C';
        //兵
        board[6][0] = board[6][2] = board[6][4] = board[6][6] = board[6][8] = 'P';


    }

    public void PrintGame(){
        System.out.println(board);
    }
}

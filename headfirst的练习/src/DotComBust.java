import java.util.*;

public class DotComBust {
    private GameHelper helper = new GameHelper();
    private ArrayList <DotCom> dotComList = new ArrayList<DotCom>();
    private int numOfGuesses = 0;

    private void setUpGame(){
        DotCom one = new DotCom();                     //创建三个Dotcom对象并且用setName命名，并加入dotComList数组中
        one.setName("Pets.com");
        DotCom two = new DotCom();
        two.setName("eToys.com");
        DotCom three = new DotCom();
        three.setName("Go2.com");
        dotComList.add(one);
        dotComList.add(two);
        dotComList.add(three);

        System.out.println("你的目标是击沉三个点网站");       //对整个游戏进行提示
        System.out.println("Pets.com, eToys.com, Go2.com");
        System.out.println("请在尽量少的发射次数内击沉它们吧！");

        for (DotCom dotComToSet : dotComList){            //对于每个 Dotcom对象做出随机初始化位置
            ArrayList<String> newLocation = helper.placeDotCom(3);          //调用helper的设定位置方法
            dotComToSet.setLocationCells(newLocation);                              //设置每个dotCom对象的位置
        }
    }

    private void startPlaying(){
        while(!dotComList.isEmpty()){                          //只要没有击沉完所有船，就输出再猜一个，并调用本类方法检查一下用户的输入
            String userGuess = helper.getUserInput("来猜一个");        //用helper的方法去得到输入
            checkUserGuess(userGuess);                                           //调用本类方法检测玩家的选择对不对
        }

        finishGame();                                         //将本类的完成游戏调用
    }

    private void checkUserGuess(String userGuess){           //检查一下输入并输出下结果
        numOfGuesses++;                                     //猜测的次数增加
        String result = "miss";                             //先设定为miss
        for (DotCom dotComToTest : dotComList){             //循环检测一下每个dotCom对象
            result = dotComToTest.checkYourself(userGuess);      //结果为调用dotCom的检查方法的输出；
            if (result.equals("hit")){                           //如果结果为击中
                break;                                           //跳出循环
            }
            if (result.equals("kill")){                          //如果结果为杀掉了这个
                dotComList.remove(dotComToTest);                  //在dotComList数组中移除这个对象
                break;                                            //跳出循环
            }
        }

        System.out.println(result);                              //输出本次发射的结果
    }

    private void finishGame(){                                    //构造结束游戏的方法
        System.out.println("恭喜你！所有的网站都被击沉啦！");          //输出一些结余
        if (numOfGuesses <= 18) {                                 //如果猜测次数比较低，则一种输出。如果高了则另一种输出
            System.out.println("你只用了"+numOfGuesses+"次就成功啦！");
            System.out.println("你真是太棒了！");
        }else {
            System.out.println("你用了"+numOfGuesses+"才成功哦！");
            System.out.println("继续加油！！！");
        }
    }

    public static void main(String[] args){
        DotComBust game = new DotComBust();                //创建本类的对象
        game.setUpGame();                                  //调用本类的设定方法
        game.startPlaying();                               //调用本类的开始游戏方法（包含本类的检查方法和结束游戏的方法）
    }
}

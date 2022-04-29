import java.util.*;

public class DotCom {
    private ArrayList<String> LocationCells;
    private String name;

    public void setLocationCells(ArrayList<String> loc){
        LocationCells = loc;
    }            //对本对象的位置进行封装

    public void setName(String n){
        name = n;
    }                                   //对本对象的名字进行封装

    public String checkYourself(String userInput){                              //检测玩家的输入对不对
        String result = "miss";                                                //先设定结果为miss
        int index = LocationCells.indexOf(userInput);                          //输出这个对象里含的数组对应玩家输入的下标
        if ( index >=0){                                                       //如果下标为正或者说玩家猜中了
            LocationCells.remove(index);                                       //去掉这个数组的这个元素

            if (LocationCells.isEmpty()) {                                    //如果这个数组空了，则将结果输出为kill
                result = "kill";
                System.out.println("哇！你击沉了"+name);                         //并输出阶段性提示击沉了这个对象
            }else{
                result = "hit";                                               //数组没空则将结果设定为hit
            }
        }
        return result;                                                       //返回结果
    }
}

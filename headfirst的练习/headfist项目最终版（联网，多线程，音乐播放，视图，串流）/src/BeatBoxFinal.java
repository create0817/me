import java.awt.*;
import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.event.*;


public class BeatBoxFinal {
    JFrame theFrame;
    JPanel mainPanel;
    JList incomingList;
    JTextField userMessage;
    ArrayList<JCheckBox> checkboxList;
    int nextNum;
    Vector<String> listVector = new Vector<String>();
    String userName;
    ObjectOutputStream out;
    ObjectInputStream in;
    HashMap<String,boolean[]> otherSeqsMap = new HashMap<String,boolean[]>();

    Sequencer sequencer;                                                   //声明播放器
    Sequence sequence;                                                     //声明CD
    Sequence mySequence = null;
    Track track;

    String[] instrumentName = {"Bass Drum","Closed Hit-Hat","Open Hit-Hat","Acoustic Snare","Crash Cymbal","Hand Clap","High Tom","Hi Bongo","Maracas","Whistle","Low Conga","Cowbell","Vibraslap","Low_mid Tom","High Agogo","Open Conga"};
    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};  //预设乐器的名称和乐器对应的关键字

    public static void main (String[] args){
        new BeatBoxFinal().startUp("shizheli");
    } //客户端的主函数，括号内为显示名称

    public void startUp(String name){
        userName = name;                                                   //将传入的name用userName接收
        try{                                                               //尝试
            Socket sock = new Socket("127.0.0.1",4242);         //与服务器建立socket连接端口为4242
            out = new ObjectOutputStream(sock.getOutputStream());          //构造socket的输出串流
            in = new ObjectInputStream(sock.getInputStream());             //从socket中获取串流（其实并未获得任何)
            Thread remote = new Thread(new RemoteReader());                //读取服务器的socket串流
            remote.start();                                                //启动线程
        }catch(Exception ex){
            System.out.println("couldn't connect - you'll have to play alone.");       //如果错误则输出没有连接网络
        }
        setUpmidi();                                                       //设定歌曲部分
        buildGUI();                                                        //启动创建GUI的方法
    }

    public void buildGUI(){

        theFrame = new JFrame("Cyber BeatBox");                       //创建框架并命名
        BorderLayout layout = new BorderLayout();                          //创建BorderLayout(默认的)框架布局管理器（有东南西北中五个区）
        JPanel background = new JPanel(layout);                            //创建一个面板并将面板布局管理器放进去
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));   //设定面板的尺寸

        checkboxList = new ArrayList<JCheckBox>();                         //创建放勾选框的列表

        Box buttonBox = new Box(BoxLayout.Y_AXIS);                         //创建以BoxLayout为布局管理器的按钮盒

        JButton start = new JButton("Start");                         //创建start的按钮
        start.addActionListener(new MyStartListener());                    //start类监听这个按钮
        buttonBox.add(start);                                              //将按钮放进按钮盒

        JButton stop = new JButton("stop");                           //创建stop按钮
        stop.addActionListener(new MyStopListener());                      //stop类监听这个按钮
        buttonBox.add(stop);                                               //将按钮放进按钮盒

        JButton upTemp = new JButton("Tempo Up");                     //创建upTemp（加快节奏）按钮
        upTemp.addActionListener(new MyUpTempoListener());                 //upTemp监听这个按钮
        buttonBox.add(upTemp);                                             //将按钮放进按钮盒

        JButton downTemp = new JButton("Temp down");                  //创建downTemp（放慢节奏）按钮
        downTemp.addActionListener(new MyDownTempoListener());             //downTemp类监听这个按钮
        buttonBox.add(downTemp);                                           //将按钮放进按钮盒

        JButton sendIt = new JButton("sendIt");                       //创建sendIt按钮
        sendIt.addActionListener(new MySendListener());                    //send类监听这个按钮
        buttonBox.add(sendIt);                                             //将按钮放进去按钮盒

        userMessage = new JTextField();                                    //创建一个文本框
        buttonBox.add(userMessage);                                        //将文本框加入按钮盒

        incomingList =  new JList();                                       //创建一个多列列表
        incomingList.addListSelectionListener(new MyListSelectionListener());//添加自己的音乐实例
        incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//设置选择模式为单个选择
        JScrollPane theList = new JScrollPane(incomingList);               //创建核心面板并把多列列表添加进去
        buttonBox.add(theList);                                            //将核心面板添加进按钮盒里
        incomingList.setListData(listVector);                              //

        Box nameBox = new Box(BoxLayout.Y_AXIS);                           //创建用BoxLayout布局管理器的一个名字盒
        for(int i = 0; i < 16;i++){                                        //循环16次
            nameBox.add(new Label(instrumentName[i]));                     //在名字盒中添加预先准备的16个名字
        }

        background.add(BorderLayout.EAST,buttonBox);                       //将按钮盒添加到背景面板的东（右）边
        background.add(BorderLayout.WEST,nameBox);                         //将名字盒添加到背景面板的西（左）边

        theFrame.getContentPane().add(background);                         //将背景面板加入框架中
        GridLayout grid = new GridLayout(16,16);                //创建grid布局管理器
        grid.setVgap(1);                                                   //设置grid的竖直间距
        grid.setVgap(2);                                                   //设置grid的竖直间距
        mainPanel = new JPanel(grid);                                      //创建主面板并使用grid布局管理器
        background.add(BorderLayout.CENTER,mainPanel);                     //将主面板添加进背景面板当中

        for(int i = 0 ; i < 256 ; i++){                                    //循环256次
            JCheckBox c = new JCheckBox();                                 //创建一个勾选框
            c.setSelected(false);                                          //勾选框选择不勾选
            checkboxList.add(c);                                           //添加进勾选框列表
            mainPanel.add(c);                                              //添加进主面板
        }                                                                  //end for i

        theFrame.setBounds(50,50,300,300);              //设定窗口的大小
        theFrame.pack();                                                   //使窗口大小符合内含组件的大小
        theFrame.setVisible(true);                                         //显示窗口
    }

    public void setUpmidi(){                                               //设定基本的音乐
        try{                                                               //尝试
            sequencer = MidiSystem.getSequencer();                         //创建发生装置sequencer
            sequencer.open();                                              //打开发生装置
            sequence = new Sequence(Sequence.PPQ,4);              //创建这个要播放分CD
            track = sequence.createTrack();                                //创建唯一歌曲的信息
            sequencer.setTempoInBPM(120);                                  //设定播放器的播放节奏
        }catch (Exception e) {e.printStackTrace();}                        //报错
    }

    public void buildTrackAndStart(){                                      //创建歌曲并且开始
        ArrayList<Integer> trackList = null;                               //创建乐器列表
        sequence.deleteTrack(track);                                       //CD删除原来的歌曲
        track = sequence.createTrack();                                    //CD上创建新的歌曲

        for(int i =  0; i < 16 ; i++){                                     //循环16次（对应16种乐器）
            trackList = new ArrayList<Integer>();                          //创建此种乐器的乐器列表
            for(int j = 0;j < 16;j++){                                     //循环16次（对应每种乐器的16种拍子）
                JCheckBox jc = (JCheckBox) checkboxList.get(j+(16*i));     //勾选款jc为勾选框列表中的一个
                if (jc.isSelected()){                                      //如果这个勾选框被勾选了
                    int key = instruments[i];                              //key值为此种乐器的关键字
                    trackList.add(new Integer(key));                       //将此关键字添加进乐器列表里
                }else{                                                     //如果没被勾选
                    trackList.add(null);                                   //添加null
                }                                                          //end else
            }                                                              //end for j
            makeTracks(trackList);                                         //制作这种乐器的歌曲
        }                                                                  //end for i
        track.add(makeEvent(192,9,1,0,15));     //确保第十六拍有事件，否则不会重复播放
        try{                                                               //尝试
            sequencer.setSequence(sequence);                               //将CD放进发生装置
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);           //指定无穷的播放次数
            sequencer.start();                                             //开始播放
            sequencer.setTempoInBPM(120);                                  //设定播放器的节奏为120
        }catch(Exception e){e.printStackTrace();}                          //报错

    }

    public class MyStartListener implements ActionListener {                      //继承监听的类
        public void actionPerformed(ActionEvent a){
            buildTrackAndStart();
        }      //当事件发生时，开始播放
    }

    public class MyStopListener implements ActionListener {
        public void actionPerformed(ActionEvent a){
            sequencer.stop();
        }         //当事件发生时，停止播放
    }

    public class MyUpTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){                              //当事件发生时，加快播放节奏
            float tempoFactor = sequencer.getTempoFactor();                      //tempoFactor接收播放器现在的节奏
            sequencer.setTempoFactor((float)(tempoFactor*1.03));                 //播放器设定新的播放节奏为原来的1.03倍
        }
    }

    public class MyDownTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){                              //当事件发生时，放慢播放节奏
            float tempoFactor = sequencer.getTempoFactor();                      //tempoFactor接收播放器现在的节奏
            sequencer.setTempoFactor((float)(tempoFactor*0.97));                 //播放器设定新的播放节奏为原来的0.97
        }
    }

    public class MySendListener implements ActionListener{
        public void actionPerformed(ActionEvent a){                              //当事件发生时，将用户选择的勾选框保存起来
            boolean[] checkboxState = new boolean[256];                          //用boolean组来保存勾选框的状态
            for(int i = 0; i < 256 ;i++){                                        //循环256次
                JCheckBox check = (JCheckBox) checkboxList.get(i);               //将第i个勾选框存进check
                if (check.isSelected()){                                         //如果check被选择了
                    checkboxState[i] = true;                                     //将保存boolean组的对应项变为true
                }                                                                //end if
            }                                                                    //end for i
            String messageToSend = null;
            try{                                                                 //尝试
                out.writeObject(userName +nextNum++ +":" + userMessage.getText());//将用户名和用户消息框的消息写入串流
                out.writeObject(checkboxState);                                  //将勾选框状态写入串流
            }catch(Exception ex){                                                //报错
                System.out.println("Sorry dude. Could not send it to the server.");//提醒用户
            }
            userMessage.setText("");                                             //预设用户消息为空
        }
    }

    public  class MyListSelectionListener implements ListSelectionListener{      //用户点选信息时马上加载节拍样式并开始播放
        public void valueChanged(ListSelectionEvent le){                         //更改复选框中的值
            if (!le.getValueIsAdjusting()){                                      //如果le的值改变了
                String selected = (String) incomingList.getSelectedValue();      //得到勾选框中已经选择了的值
                if (selected != null){                                           //如果有被选择的
                    boolean[] selectedState = (boolean[]) otherSeqsMap.get(selected);//用哈希表将有值的位置变成boolean组类型的true
                    changeSequence(selectedState);                               //将boolean类型组变成勾选框组来兴
                    sequencer.stop();                                            //播放器先停掉
                    buildTrackAndStart();                                        //制作歌曲并开始播放
                }
            }
        }
    }

    public class RemoteReader implements Runnable{                                      //线程执行任务
        boolean[] checkboxState = null;                                                 //将勾选框状态设为空
        String nameToShow = null;                                                       //设定一个空的字符串
        Object obj = null;                                                              //设定一个空对象
        public void run(){                                                              //设定线程运行的方法
            try{                                                                        //尝试
                while((obj=in.readObject()) != null){                                   //从输入串流中获取对象
                    System.out.println("got an object from server");                    //输出从服务器处得到了对象
                    System.out.println(obj.getClass());                                 //输出对象的类型
                    String nameToShow = (String) obj;                                   //将对象转为字符串
                    checkboxState = (boolean[]) in.readObject();                        //将勾选框状态设为从串流输入中得到的值
                    otherSeqsMap.put(nameToShow,checkboxState);                         //将字符串和用哈希表对应起来
                    listVector.add(nameToShow);                                         //信息加入JList组件
                }                                                                       //end try
            }catch (Exception ex){ex.printStackTrace();}                                //报错
        }
    }

    public class MyPlayMineListener implements ActionListener{                          //播放自己的音乐（目前不支持）
        public void actionPerformed(ActionEvent a){                                     //监听
            if (mySequence != null){                                                    //如果我的CD不是空的
                sequence = mySequence;                                                  //将CD设置为我的CD
            }
        }
    }

    public void changeSequence(boolean[] checkboxState){                                 //布尔类型的勾选框改为JCheckBox类型
        for(int i = 0 ; i < 256;i++){                                                    //循环256次
            JCheckBox check = (JCheckBox) checkboxList.get(i);                           //check为接收勾选框列表的的第i个勾选框
            if (checkboxState[i]){                                                       //当布尔类型为true时
                check.setSelected(true);                                                 //勾选框类型也为true
            }else{                                                                       //否则
                check.setSelected(false);                                                //勾选框类型为false
            }
        }
    }

    public void makeTracks(ArrayList list){                                              //制作歌曲
        Iterator it = list.iterator();                                                   //设定一个it的迭代器
        for (int i = 0; i < 16;i++) {                                                    //循环16次
            Integer num = (Integer) it.next();                                           //num是迭代器的下一个数值
            if (num != null) {                                                           //如果这个值存在
                int numKey = num.intValue();                                             //这个值转换成int类型
                track.add(makeEvent(144, 9, numKey, 100, i));           //制作NOTE ON类型使用此乐器的事件
                track.add(makeEvent(128, 9, numKey, 100, i + 1));  //制作NOTE OFF类型使用此乐器的事件
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two,int tick){          //制作歌曲的事件
        MidiEvent event = null;                                                         //创建一个歌曲时间
        try{                                                                            //尝试
            ShortMessage a = new ShortMessage();                                        //创建一个短消息
            a.setMessage(comd, chan, one, two);                                         //设定消息的类型，频道，音符，音道
            event = new MidiEvent(a,tick);                                              //消息加入事件并设定时机
        }catch (Exception E){}                                                          //报错
        return event;                                                                   //返回事件
    }
}

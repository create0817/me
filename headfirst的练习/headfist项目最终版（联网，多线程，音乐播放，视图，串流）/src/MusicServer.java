import java.io.*;
import java.net.*;
import java.util.*;

public class MusicServer {                                                                        //将客户端的串流用in传入并分成两个对象，然后用输出串流写入文件中
    ArrayList<ObjectOutputStream> clientOutputStream;                                             //设定类型为串流对象组的客户端串流对象组

    public static void main (String[] args){
        new MusicServer().go();
    }                           //主函数启动服务器

    public void go(){                                                                             //客户端的运行方法
        clientOutputStream = new ArrayList<ObjectOutputStream>();                                 //设定一个客户端串流组

        try{                                                                                      //尝试
            ServerSocket serverSock = new ServerSocket(4242);                                //设置服务器的socket端口为4242

            while(true){                                                                          //一直循环
                Socket clientSocket = serverSock.accept();                                        //客户端的socket是服务器接收的socket
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());  //设定服务器要向文件输出的串流
                clientOutputStream.add(out);                                                      //将要输出的串流加入串流组当中

                Thread t = new Thread(new ClientHandler(clientSocket));                           //创建一个线程
                t.start();                                                                        //启动线程

                System.out.println("got a connection");                                           //输出连接成功
            }                                                                                     //end 循环
        }catch (Exception ex){ex.printStackTrace();}                                              //报错
    }

    public class ClientHandler implements Runnable{                                               //线程执行的任务（内部类）（处理客户端）
        ObjectInputStream in;                                                                     //设定一个输入串流对象
        Socket clientSocket;                                                                      //设定一个客户端socket

        public ClientHandler(Socket socket){                                                      //客户端处理的构造方法
            try{                                                                                  //尝试
                clientSocket = socket;                                                            //socket改名为用户socket
                in = new ObjectInputStream(clientSocket.getInputStream());                        //从客户端那里获得串流
            }catch(Exception ex){ex.printStackTrace();}                                           //报错
        }

        public void run(){                                                                        //读取服务器信息
            Object o2 = null;                                                                     //设定对象1
            Object o1 = null;                                                                     //设定对象2
            try{                                                                                  //尝试
                while((o1 = in.readObject()) != null){                                            //当有从客户端接受的对象时（第一个是文本）
                    o2 = in.readObject();                                                         //o2等于从客户端接受的对象（第二个是256个勾选框）
                    System.out.println("read two object");                                        //输出读到了两个对象
                    tellEveryone(o1,o2);                                                          //将对象写入文件中
                }                                                                                 //end try
            }catch(Exception ex) {ex.printStackTrace();}                                          //报错
        }
    }

    public void tellEveryone(Object one , Object two){                                            //将对象写入文件中
        Iterator it = clientOutputStream.iterator();                                              //用it迭代器接收串流组的迭代器
        while(it.hasNext()){                                                                      //如果it当中有下一个则一直循环
            try{                                                                                  //尝试
                ObjectOutputStream out = (ObjectOutputStream) it.next();                          //输出串流等于it中的下一个
                out.writeObject(one);                                                             //将对象1写入串流中
                out.writeObject(two);                                                             //将对象2写入串流中
            }catch (Exception ex){ex.printStackTrace();}                                          //报错
        }
    }
}

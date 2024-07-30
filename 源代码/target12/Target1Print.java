import java.util.Scanner;
import MYPROGRESSPACKGE.*;
import target1.FirstCFS;
import target1.RoundRobin;

import java.util.Queue;
//import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Target1Print {
 
    public static void print(LinkedList<MyProcess>  RRresult,String alorithmName){
       double ATT=0;//平均周转时间
       double AWTT=0; //平均带权周转时间
           double AverageWaitingTime=0 ;//平均等待时间
           double AverageRef=0;//平均相应时间
        double count=0;
        System.out.println("选用"+alorithmName+"算法的运行结果如下");
  //写平均周转时间和等待时间
        for (MyProcess process : RRresult) {
            // 这里是对每个MyProcess对象进行的操作
            // 例如打印process的信息
            System.out.println("进程"+process.getId()+"的到达时间是"+process.getArriveTime()+"   离开时间是"+process.getFinishtime()+"   停留时间是"+process.getStayTime()+"   有效运行时间是"+process.getTotalNeedRuntime());
            ATT+=process.getStayTime();
            AWTT+=process.getStayTime()/process.getTotalNeedRuntime();
            AverageWaitingTime+= process.getStayTime()-process.getTotalNeedRuntime() ;
            count++;

        }
        ATT=ATT/count;
        AWTT=AWTT/count;
        AverageRef=AverageRef/count;
        AverageWaitingTime=AverageWaitingTime/count;
        System.out.println("平均周转时间是"+ATT);
        System.out.println("平均带权周转时间"+AWTT);
        System.out.println("平均等待时间是"+AverageWaitingTime);
        System.out.println(alorithmName+"算法信息输出结束");
    }
}

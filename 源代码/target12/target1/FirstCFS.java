package target1;
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

public class FirstCFS {
   public static void DFCFS( Map<Integer, LinkedList<MyProcess>> FCFS, LinkedList<MyProcess>  FCFSresult){
     // LinkedList<MyProcess> waitingList=new LinkedList<>();
     int time =0;
     for (Map.Entry<Integer, LinkedList<MyProcess>> entry : FCFS.entrySet()) {
      Integer key = entry.getKey();
      LinkedList<MyProcess> processList = entry.getValue();

      // 处理每个时间点的进程列表
      for (MyProcess process : processList) {
          // 例如打印进程信息
       time=  time>process.getArriveTime() ? time:process.getArriveTime();//time先到进程到达的时间 
          time+= process.getTotalNeedRuntime();//然后再执行
                  process.setfinishTime(time);//应该是执行完只用设置一个finishtime
           FCFSresult.add(process);
         }
  }

   } 

}

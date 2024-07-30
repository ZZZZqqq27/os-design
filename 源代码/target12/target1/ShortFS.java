package target1;
import java.util.Scanner;
import MYPROGRESSPACKGE.*;
import target1.FirstCFS;
import target1.RoundRobin;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Queue;
//import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public  class ShortFS{

public static void DoShortFS(Map<Integer, LinkedList<MyProcess>> SJF , LinkedList<MyProcess>  SJFresult){
int time=0;

MyProcess nowdo=null;
PriorityQueue<MyProcess> sjfWaiting = new PriorityQueue<>(Comparator.comparingInt(MyProcess::getleftTime));
addToWaitingList(SJF,sjfWaiting, time);
while(!(SJF.isEmpty()&&sjfWaiting.isEmpty())){
    time++;

//System.out.println("time+1是"+time);
nowdo = sjfWaiting.poll();//刚开始有可能是null，因为还没到对应的key
if(nowdo!=null){
if(nowdo.runMyProcess()){//如果不是
    nowdo.setfinishTime(time);
  //  System.out.println("time是"+time);
SJFresult.add(nowdo);
nowdo=null;
}else{
    //System.out.println("leftTime"+nowdo.getleftTime());
sjfWaiting.add(nowdo);
nowdo=null;


}
}
else{
   // System.out.println(time+"时没有能执行的");
}
addToWaitingList(SJF, sjfWaiting, time);
}

}
    public static int tryTimePlusOne( Map<Integer, LinkedList<MyProcess>> SFS,PriorityQueue<MyProcess> waitingList,int time){
        int timePlusOne=time+1;
        addToWaitingList(SFS, waitingList, timePlusOne);
        return timePlusOne;
    }
    public static void addToWaitingList( Map<Integer, LinkedList<MyProcess>> SFS,PriorityQueue<MyProcess> waitingList,int time){
        if (SFS.containsKey(time)) {
            LinkedList<MyProcess> MyProcessList = SFS.remove(time);
            if (MyProcessList != null) {
                waitingList.addAll(MyProcessList);
            }
        }

            }
}
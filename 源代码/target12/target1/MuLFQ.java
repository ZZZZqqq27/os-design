package target1;
import java.util.Scanner;

import target1.FirstCFS;
import MYPROGRESSPACKGE.MyProcess;
import java.util.Queue;
//import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
public class MuLFQ {
    /*
     * 多级调度队列的执行方式
     * 
     * // 创建一个包含a个ArrayList的列表，每个ArrayList代表一行
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>(a);
        多级队列，队列的个数可以动态增长，但是考虑到队列数不应该让用户来设置，我们这里就默认 有三个队列
     * 
     */ 
    public static void doMuLFQ( Map<Integer, LinkedList<MyProcess>> processMap, LinkedList<MyProcess>  resultPrint){
        //根据理论知识，进程队列所在的优先级越高，时间片越小 设置为 2、3、4
        //这里我们的选择是，先在第一个队列执行，然后再第二个队列执行，都没执行完，再第三个队列执行
        LinkedList<MyProcess> waitingListOne=new LinkedList<>();    //每个队列里按照fifo
        LinkedList<MyProcess> waitingListTwo=new LinkedList<>();//
        LinkedList<MyProcess> waitingListThree=new LinkedList<>();//
        //LinkedList<MyProcess> helpWaitingList=new LinkedList<>();
      /*  for (Map.Entry<Integer, LinkedList<MyProcess>> entry : processMap.entrySet()) {
            LinkedList<MyProcess> processList = entry.getValue();
            waitingListOne.addAll(processList);
        }
        int time=0;
         for (MyProcess process : waitingListOne) {
            time=time<process.getArriveTime()? process.getArriveTime():time;
           
           // System.out.println(process); // 假设MyProcess类重写了toString方法
        }
        while (!waitingListOne.isEmpty()) {
         
            MyProcess process = waitingListOne.pollFirst(); // 移除并返回第一个元素
           time=time<process.getArriveTime()? process.getArriveTime():time;
         int help=process.timeNeedPlus(2);
         time+=help;
        if( process.runMyProcess(help)){
            process.setfinishTime(time);
        resultPrint.add(process);
        }
else{
waitingListTwo.add(process);

}
        }

        while (!waitingListTwo.isEmpty()) {
         
            MyProcess process = waitingListTwo.pollFirst(); // 移除并返回第一个元素
          // time=time<process.getArriveTime()? process.getArriveTime():time;
        //这个地方都走过一圈了，应该不用这句了
          int help=process.timeNeedPlus(3);
         time+=help;
        if( process.runMyProcess(help)){
            process.setfinishTime(time);
        resultPrint.add(process);
        }
else{
waitingListThree.add(process);

}
        }
        while (!waitingListThree.isEmpty()) {
         
            MyProcess process = waitingListThree.pollFirst(); // 移除并返回第一个元素
          // time=time<process.getArriveTime()? process.getArriveTime():time;
        //这个地方都走过一圈了，应该不用这句了
          int help=process.timeNeedPlus(4);
         time+=help;
        if( process.runMyProcess(help)){
            process.setfinishTime(time);
        resultPrint.add(process);
        }
else{
waitingListThree.add(process);

}*/
int time=0;
RoundRobin.addToWaitingList(processMap, waitingListOne, time);
    while(!(processMap.isEmpty()&&waitingListOne.isEmpty()&&waitingListTwo.isEmpty()&&waitingListThree.isEmpty())){
        //低优先级的时间片执行到一半有高优先级的进来是要打断的
        if(!waitingListOne.isEmpty()){
           MyProcess processThis=  waitingListOne.pollFirst() ;
           boolean finish=false; 
           for(int i=0;i<2;i++){ 
                    time++;
                    RoundRobin.addToWaitingList(processMap, waitingListOne, time);
                if(processThis.runMyProcess()){
                    processThis.setfinishTime(time);
                finish=true;
                    resultPrint.add(processThis);
                  i=2;    }else
                  {
                    
                  }              
                  }
            if(!finish){
                waitingListTwo.add(processThis);
            }

        }
else if(waitingListOne.isEmpty()&&(!waitingListTwo.isEmpty())){
    MyProcess processThis=  waitingListTwo.pollFirst()       ;
    boolean rollToThree=true;
    for(int i=0;i<3;i++){
time++;
boolean runEnough=processThis.runMyProcess();//run是一定要run的
boolean someThing =RoundRobin.addToWaitingList(processMap, waitingListOne, time);
if(runEnough&someThing){
    i=3;


rollToThree=false;
    processThis.setfinishTime(time);
    resultPrint.add(processThis);
}else if(
    (!runEnough)&someThing
){
i=3;

}else if( runEnough&(!someThing)){
i=3;
rollToThree=false;
processThis.setfinishTime(time);
resultPrint.add(processThis);
}else{


}

}
if(rollToThree){
    waitingListThree.add(processThis);
}
}else if(waitingListOne.isEmpty()&&waitingListTwo.isEmpty()&&!waitingListThree.isEmpty()){
   MyProcess processThis=  waitingListThree.pollFirst()       ;
    boolean rollToThree=true;
    for(int i=0;i<4;i++){
time++;
boolean runEnough=processThis.runMyProcess();//run是一定要run的
boolean someThing =RoundRobin.addToWaitingList(processMap, waitingListOne, time);
if(runEnough&&someThing){
    i=4;
    
rollToThree=false;
    processThis.setfinishTime(time);
    resultPrint.add(processThis);
}else if(
    (!runEnough)&someThing
){
i=4;
}else if( runEnough&(!someThing)){
i=4;
rollToThree=false;
processThis.setfinishTime(time);
resultPrint.add(processThis);
}else{


}

}
if(rollToThree){
    waitingListThree.add(processThis);
}
}else {
    time++;//如果三个队列都没有，尝试引进新的到waitingListOne
    RoundRobin.addToWaitingList(processMap, waitingListOne, time);
}


    }
        }
    }


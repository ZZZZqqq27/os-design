package MYPROGRESSPACKGE;

//进程需要满足的方面
/* 1.进程执行的总时长
 * 2.
 * 
 * 
*/
import java.util.Scanner;
import java.util.Queue;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProcess  {
    private int  id;
    private int totaltime;
    public int leftTime;
    private int arriveTime;//感觉可以写在
    private int PriotyLevel;
    private int finishtime;
    public MyProcess(int a){
        id=a;
    };
    public MyProcess(int ID,int Totaltime,int ArriveTime){
       id=ID;
        totaltime=Totaltime;
        leftTime=  Totaltime;
    arriveTime=ArriveTime;        
        PriotyLevel=0;
}
    public MyProcess(int ID,int  Totaltime,int ArriveTime,int prio ){
       id=ID;
        totaltime=Totaltime;
        leftTime=Totaltime;
        arriveTime=ArriveTime;
        PriotyLevel=prio;
    }
    public void run(int a){
        
        //如果执行完了
    }
    public boolean runMyProcess(){
        if(leftTime<=0)  throw new RuntimeException(id+"lefttime是"+leftTime+"但是仍然要被执行");
        leftTime--;
        return leftTime==0? true:false ;
        
    }
    public int timeNeedPlus(int Timeslice){
        if(leftTime>=Timeslice){
   return Timeslice;
}else{return leftTime;
        }}

     public boolean runMyProcess(int time){
        leftTime-=time;
       if(leftTime==0){

        return true;
       }else if(leftTime>0){
return false;

       }else{
        throw new RuntimeException("不对");
  // return false;  
    }
     }
    
   // public int get
    public void setfinishTime(int FinishTime){
finishtime=FinishTime;
    }
    public int getArriveTime() {
        return arriveTime;
    }
    public int getFinishtime() {
        return finishtime;
    }
public int getId() {
    return id;
}
public double getTotalNeedRuntime() {
    return totaltime;
}
public int getStayTime()
{
    return finishtime-arriveTime;
}
public int getleftTime(){
    return leftTime;
}

}

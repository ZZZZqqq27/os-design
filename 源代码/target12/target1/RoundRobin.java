package target1;
import MYPROGRESSPACKGE.*;
import java.util.LinkedList;
import java.util.Map;
public class RoundRobin {
    public static void doDD(
    
        Map<Integer, LinkedList<MyProcess>> RR,
        LinkedList<MyProcess>  RRresult,int timeSliceValue
    ){
    
    
        int time=0;
        LinkedList<MyProcess> waitingList=new LinkedList<>();
        MyProcess nowdo = null;

  

while (!(RR.isEmpty() && waitingList.isEmpty())) {
    //刚开始 time是0，只用增加waiting list就好了·
    addToWaitingList(RR, waitingList, time);
    if (!waitingList.isEmpty()) {
        if (nowdo != null) {
            System.out.println("有问题结束");
            throw new RuntimeException("处理中断，进程未完成");  // 使用异常处理错误
        }
        nowdo = waitingList.removeFirst();
        boolean helpJudgeFinish=false;
        for (int i = 0; i < timeSliceValue; i++) {
            time++;addToWaitingList(RR, waitingList, time); 
            if (nowdo.runMyProcess()) {
                nowdo.setfinishTime(time);
                RRresult.add(nowdo);
                nowdo = null;  // 如果进程完成，确保清除 nowdo
                    i=timeSliceValue;//这里要中断
                    helpJudgeFinish=true;
            }
        }
        if(!helpJudgeFinish){
            if(nowdo==null) { throw new RuntimeException("..");};
            waitingList.add(nowdo);
        nowdo=null;
        }
    } else {
        time++;  // 如果 waitingList 为空，增加时间
    }
}

//System.out.println(time);


    };

    public static void RRprint(LinkedList<MyProcess>  RRresult){
        System.out.println("选用RR算法的运行结果如下");
  //写平均周转时间和等待时间
        for (MyProcess process : RRresult) {
            // 这里是对每个MyProcess对象进行的操作
            // 例如打印process的信息
            System.out.println("进程"+process.getId()+"的到达时间是"+process.getArriveTime()+" 离开时间是"+process.getFinishtime()+"   停留时间是"+process.getStayTime()+"   有效运行时间是"+process.getTotalNeedRuntime());

            
        }
    }
    public static boolean addToWaitingList( Map<Integer, LinkedList<MyProcess>> RR,LinkedList<MyProcess> waitingList,int time){
      boolean result=false;
        if (RR.containsKey(time)) {
            LinkedList<MyProcess> MyProcessList = RR.remove(time);
            if (MyProcessList != null) {
                result=true;
                waitingList.addAll(MyProcessList);
            }
        }
            return result;
            }
}

import java.util.Scanner;
import MYPROGRESSPACKGE.*;
import target1.FirstCFS;
import target1.MuLFQ;
import target1.RoundRobin;
import target1.ShortFS;
import target2.*;
//import target2.algori;
import java.util.Queue;
//import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public  class Simulator{

public static void main(String[] args) {
  //  DoTarget1();
   Dotaget2();
 
}
//target2 辅助函数
static void  showInfo(RAM ram){
    ram.showInfo();
}

static void showNowAlPerformance(RAM ram){
ram.showPerformance();
}

static void useProcess(RAM ram,Scanner scanner){
System.out.println("请输入您想使用的进程号");
int ProgressId= scanner.nextInt();
System.out.println("请输入您想使用该进程的地址");
int address=scanner.nextInt();
int PageOffset=address%64;
int virtualPageNumber=address/64+1;//求出了 这个地方要看好0，1
int RAMPageNumber = ram.TryDoRead(ProgressId, virtualPageNumber);
//假设一个进程64byte
if(RAMPageNumber<=0&&RAMPageNumber!=-999){throw new RuntimeException("在main函数里，doread的执行返回值出了错误");}
if(RAMPageNumber>0){
System.out.println("该地址现在在物理内存中是在第"+RAMPageNumber+"（内存的物理框的范围是1到16，0不作数）");//
System.out.println("在物理内存中的地址是"+((RAMPageNumber-1)*64+PageOffset));
}}


static void tryToCreateANewProgress(RAM ram,int progressNumber,Scanner scanner){
    System.out.println("请输入该进程有多少个虚拟页面(请输入一个正整数)");
    int VitualPageNumber=scanner.nextInt();
    if(VitualPageNumber<=0){
        throw new RuntimeException("您输入的虚拟页面的个数不是正整数");
    }
    int needRam=VitualPageNumber<4 ?VitualPageNumber:4 ;
ram.CreateNewProgress(progressNumber, VitualPageNumber, needRam);

}



 static void tryToDeleteANewProgress(RAM ram,Scanner scanner){//完成了
System.out.println("请输入你想要删除的进程的进程号");
int wantToDelete=scanner.nextInt();
ram.tryToDeleteAProgress(wantToDelete);

 }

 static void changeAlgorithm(RAM ram,Scanner scanner){
System.out.print("目前的单进程的替换算法是");
 ram.showNowALGORI();
 System.out.println("输入您想要替换成什么算法(1,2,3)");
 System.out.println("1.FIFO");
System.out.println("2.LRU");
System.out.println("3.CLOCK");
int newAlNumber= scanner.nextInt();
algori targetAlgori;

switch(newAlNumber){
    case 1:
    targetAlgori=algori.FIFO;
    break;
    case 2:
    targetAlgori=algori.LRU;
    break;
    case 3:
    targetAlgori=algori.CLOCK;
    break;
default :System.out.println("您未输入想要替换成什么算法,程序即将退出");
throw new RuntimeException("您输入的算法替换不正确");

}
ram.changeAL(ram.returnAlGORI(), targetAlgori);

}



 static void Dotaget2(){
    System.out.println("下面开始target2");
    /**
     *物理内存有多少个帧理论上也可以键盘输入，但是我们这里固定为16个页 
         给进程首次分配帧也可以采用首次适应算法、循环首次适应算法等等
         我们这里就选用首次适应算法，  
 
         在进程不断的执行的过程中我们可以根据一个进程的缺页率，进行固定分配局部置换，可变分配全局置换，可变分配局部置换等等
         我们这里就选用固定局部置换
 
         另外，由于模拟的内存页数较小，我们就不用多级页表了
         多个进程共用一个2行的tlb
         （是否要模拟访问时间待定）
         我们假设一个进程的虚拟内存有8个页面，我们假设字节寻址、一个页面64byte 
         （也就是6位为页内偏移地址，我们不需要考虑）
 
         这里我们不限制进程虚拟空间的下限，也就是说它可以小于 4，有可能把 16个桢的物理内存拆散，不是mod 4==0，
         上限限制为8个页面
    */
   int progressNumber=1;
   Scanner scannerTarget2=new Scanner(System.in);
 boolean loop=true;
 RAM ram=new RAM();
 System.out.println("系统初始化完毕，默认开始选择FIFO算法");
   while(loop){
  
     System.out.println("请输入你想选择的功能");
     System.out.println("1.显示内存中现有的进程和他们所占据的物理页面号");
     System.out.println("2.尝试新建一个进程");
     System.out.println("3.尝试删除一个进程");
     System.out.println("4.读写某进程的 内容");
     System.out.println("5.更改进程的页表的替换算法");//更改算法时先显示算法的效果，再清空
     System.out.println("6.查看目前算法内存访问的tlb命中情况和各进程的页表命中情况");
     System.out.println("7.结束");
     int number = scannerTarget2.nextInt() ;
     switch(number){
 case 1:
 showInfo(ram);
 break;
 case 2:
 tryToCreateANewProgress(ram,progressNumber,scannerTarget2);
 progressNumber++;
 break;
  case 3:
     tryToDeleteANewProgress(ram,scannerTarget2);
  break;
  case 4:
  useProcess(ram,scannerTarget2);
  break;
  case 5:changeAlgorithm(ram,scannerTarget2);
  break;
  case 6:showNowAlPerformance(ram);
  break; 
  case 7: System.out.println("退出内存模拟");
 loop=false;
  break;
  default :
  System.out.println("Invalid command");
 
     }
 }
   System.out.println("target 2执行结束");
   
 }
static void DoTarget1(){

 //先把单独的几个算法都能实现的写出来
    //三个队列 ，ready,等待，执行（就一个）
    //Queue<MyProcess> queue = new ArrayDeque<>();这个应该是可以在每个算法的函数里分别写的
    //Queue<MyProcess> printQueue=new ArrayDeque<>();

    //理论上应该 进程输入是按照到达时间递增，但是这里我们不要这个限制，所以第二个FCFS算法，我们还是按照进程按照到达时间乱序进入，这时，我们还是可以用
    // Map<Integer, LinkedList<MyProcess>> FCFS
    
    Map<Integer, LinkedList<MyProcess>> RR = new HashMap<>();//四个应该是都要有
    Map<Integer, LinkedList<MyProcess>> FCFS = new HashMap<>();//
    Map<Integer, LinkedList<MyProcess>> MLFQ = new HashMap<>();
    Map<Integer, LinkedList<MyProcess>> SJF = new HashMap<>();
    LinkedList<MyProcess>  RRresult =new  LinkedList<>()  ;//存放算法执行完的对象，用来最后print出来算法的执行情况
    LinkedList<MyProcess>  FCFSresult =new  LinkedList<>()  ;
    LinkedList<MyProcess>  MLFQresult =new  LinkedList<>()  ;
    LinkedList<MyProcess>  SJFresult =new  LinkedList<>()  ;
    boolean chooseRR=chooseAlg("RR");
    //System.out.println(chooseRR);
    int timeSliceValue=0;
    if(chooseRR){System.out.println("请输入时间片的大小");
    timeSliceValue=getAInt();
}
    boolean chooseFCFS=chooseAlg("FCFS");
    boolean chooseMLFQ=chooseAlg("MLFQ");
    boolean chooseSJF= chooseAlg("SJF");
    //System.out.println();
   
   
    MyProcessInit(chooseRR,chooseFCFS,chooseMLFQ,chooseSJF,RR,FCFS,MLFQ,SJF);
    if(!(chooseFCFS||chooseMLFQ||chooseRR||chooseSJF)){
        System.out.println("您没有选择任何算法");
        System.exit(0);
    }
   if(chooseRR){
System.out.println("执行RR");
    RoundRobin.doDD(RR, RRresult, timeSliceValue);
    //如果选择了，那么再执行，这里要做很多准备，,再执行具体算法的模拟函数
   }
 
   if(chooseFCFS){    
    System.out.println("执行FCFS");
    FirstCFS.DFCFS(FCFS, FCFSresult);
   }
   if(chooseMLFQ){
System.out.println("执行MuLFQ");
MuLFQ.doMuLFQ(MLFQ, MLFQresult);
   }
   if(chooseSJF){
    System.out.println("执行SJF");
    ShortFS.DoShortFS(SJF, SJFresult);
   }
    
   printResult(chooseRR, chooseFCFS, chooseMLFQ, chooseSJF,RRresult,FCFSresult,MLFQresult,SJFresult); 

}
static void printResult(boolean RR,boolean FCFS,boolean MLFQ,boolean SJF, LinkedList<MyProcess> RRResult,LinkedList<MyProcess> FCFSResult,LinkedList<MyProcess> MLFQResult,LinkedList<MyProcess> SJFesult){
if(RR) {Target1Print.print(RRResult, "RR算法");} //调用每个类的静态print函数
if(FCFS) {Target1Print.print(FCFSResult, "FCFS算法");} ;
if(MLFQ) {
Target1Print.print(MLFQResult, "MuLFQ算法");

};
if(SJF){Target1Print.print(SJFesult, "ShortFS算法");};
}

static boolean chooseAlg(String alName ){
    boolean result=false;
System.out.println("是否要选择"+alName+"算法(如果是，请输入是):");
Scanner scanner = new Scanner(System.in);

String input = scanner.nextLine();  
if(input.equals("是")) result=true;      

return result;
}


static void  MyProcessInit(boolean RR,boolean FCFS,boolean MLFQ,boolean SJF, Map<Integer, LinkedList<MyProcess>> RRMap, Map<Integer, LinkedList<MyProcess>> FCFSMap,
Map<Integer, LinkedList<MyProcess>> MLFQMap, Map<Integer, LinkedList<MyProcess>> SJFMap){
    System.out.println("请选择你创建的进程数量");

    int MyProcessNumber=-1;

MyProcessNumber=getAInt();

for(int i=0;i<MyProcessNumber;i++){//i就是 id
System.out.println("请输入第"+i+"个进程的总执行时间（大于0）");
int duringTime=getAInt();
System.out.println("请输入第"+i+"个进程的到达时间");
int arriveTime=getAInt();
System.out.println("请输入第"+i+"个进程的优先级");
int priority=getAInt();
addObjectToMap(RR, i, duringTime, arriveTime, priority, RRMap);
addObjectToMap(FCFS, i, duringTime, arriveTime, priority, FCFSMap);
addObjectToMap(MLFQ, i, duringTime, arriveTime, priority, MLFQMap);
addObjectToMap(SJF, i, duringTime, arriveTime, priority, SJFMap);

//创建进程后，加入hashmap这一个装着就行
}
}
static void addObjectToMap(boolean Algorithm,int ID,int DURINGTIME, int ARRIVETIME, int PRIORITY ,Map<Integer, LinkedList<MyProcess>> Map){
    if(!Algorithm)return;
    
    if (!Map.containsKey(ARRIVETIME)) {
        Map.put(ARRIVETIME, new LinkedList<>());
    }
    
    // 现在可以安全地添加或操作列表
    Map.get(ARRIVETIME).add(new MyProcess(ID, DURINGTIME, ARRIVETIME, PRIORITY));
    
}

static int getAInt(){
    
  int number;
  int count=3;
while (true) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("请输入一个正整数：");
    // 检查是否有整数输入
    if (scanner.hasNextInt()) {
        
        number = scanner.nextInt();  // 读取整数

        System.out.println("你输入的正整数是：" + number);
        break;  // 输入有效，退出循环
    } else {
        count--;
        if(count==0){
            System.out.println("您输入的不符合要求程序即将退出");
            System.exit(0);
        }
        System.out.println("输入错误，请输入一个正整数！");
        System.out.println("您还有"+count+"次机会");
        scanner.next();  // 清除错误的输入
    }
    
}
return number;
}
}
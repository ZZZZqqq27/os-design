/*
 * 
 * for (MyProcessTarget2 process : ObjectList) {
    // 这里是对每个process对象进行的操作
    System.out.println(process); // 例如打印每个进程的信息
}

 */
/**
 * 对于0，1的处理
 * 用户看到的虚拟页号和真实页号 都是 1到最大值
 * 
 */
package target2;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.util.LinkedList;

//mport javax.management.RuntimeErrorException;

//import javax.management.RuntimeErrorException;

import java.util.ArrayList;
import java.util.Iterator;

public class RAM {
    private static algori ALGORI;
    PageInfo[] TLBArray ;//TLB的替换也需要选用算法，
    //这里选择，如果一个发生tlb替换，如果另一个的recent use 数值大于
    //CLOCK，
    //FIFO
    //LRU需要在这里实现     
    private static LinkedList<MyProcessTarget2> ObjectList;//记录目前有的进程，
    private static int[] progressId ;
    //RAM固定有16个帧
    private static double TLBhitTime;
    private static double totalReadTime;
    private static double deletedProcessReadTime;
    private static double deletedProcessMissingTime;
public RAM(){
progressId=new int[16];
ObjectList=new LinkedList<>();
TLBArray = new PageInfo[2];
TLBArray[0]=new PageInfo(-1, -1, -1);
TLBArray[1]=new PageInfo(-1, -1, -1);
ALGORI=algori.FIFO;
for(int i=0;i<16;i++){
    progressId[i]=-1;
}
TLBhitTime=0;
totalReadTime=0;
}
public void showNowALGORI(){
    System.out.println(ALGORI);
    
}

public void showInfo(){
//这里不显示每个进程的信息了，显示内存的信息
showNowALGORI();
System.out.println("下面输出内存中页框的信息");
for(int i=0;i<16;i++){
if(progressId[i]!=-1){
    System.out.println("内存中的第"+(i+1)+"个页框是"+progressId[i]+"进程的");//我们全部统一，在对外展示时都没有0
}

}
System.out.println("下面是tlb的信息");
boolean helpJudgeTLBContainsSomethind=false;
for(int i=0;i<2;i++){
   if(TLBArray[i].getPageInFoprogressNumber()>0) {System.out.println(TLBArray[i]);
    helpJudgeTLBContainsSomethind=true;
}
}
if(!helpJudgeTLBContainsSomethind){System.out.println("********目前TLB中还没有有效的信息");}
System.out.println("内存信息输出结束，");
}

public void CreateNewProgress(int NewProgressId,int VirtualPageAmount,int needInRam){//这个need给main函数的地方处理，如果比4大，那就算成4
    //这个地方为了后面加算法，肯定还得加参数
    int [] recordIndexInRam=new int[needInRam];//虚拟页号就是顺着的
    int count=0;
    for(int i=0;i<16;i++ ){
        if(progressId[i]==-1){ 
         if(count<needInRam)   recordIndexInRam[count]=i; //这个地方先记录页框号
            count++;}
    }
    if(count<needInRam){
        System.out.println("您内存目前剩余的空闲帧无法创造一个需要分配"+needInRam+"物理内存中的帧的进程");
        return;
    }
    MyProcessTarget2 newProgress=new MyProcessTarget2(NewProgressId ,VirtualPageAmount,needInRam,ALGORI,recordIndexInRam);
//这里要初始化clock,fifolru,放在构造函数里
ObjectList.add(newProgress);
for(int i=0;i<needInRam;i++){
newProgress.writePageTable(i, recordIndexInRam[i]+1);//页表中的物理页框的范围也是 1到16
progressId[recordIndexInRam[i]]=NewProgressId;
}
}

private int ReadTlb(int progressNumber,int virtualPageNumber){
    int help=-1;
    boolean TLBhas=false;
    for(int i=0;i<2;i++){
        if(TLBArray[i].isThisRow(progressNumber, virtualPageNumber)>-1){
            TLBhas=true;
            help=i;
        }
}
 if (TLBhas){  System.out.println("查找tlb成功，无需访问页表");
 TLBhitTime++;
    TLBArray[help].recentUsePlus();
int result=TLBArray[help].isThisRow(progressNumber, virtualPageNumber);
  

return result;

}
else{
    
    return -1;
}
}
private void changeTlb(int progressNumber,int virtualPageNumber,int RamNumber ){//change
//在这里要完成对tlb优先级的
int index=  TLBArray[0].getRecentUseCount()<=TLBArray[1].getRecentUseCount()? 0:1 ;
//这里recentUseCount 给一个1
TLBArray[1-index].recentUseCountSubIfOtherReplace();
PageInfo newPageinfo=new PageInfo(progressNumber, virtualPageNumber, RamNumber,1);
System.out.println("TLB中的条目:"+TLBArray[index]+"即将替换为"+newPageinfo);
TLBArray[index]=newPageinfo;
}


private int doRead(int progressNumber,int virtualPageNumber,MyProcessTarget2 inList){
  totalReadTime++;
    int RAMNumber=ReadTlb(progressNumber, virtualPageNumber);
    if(RAMNumber>0){//这个地方后面    
      if(ALGORI==algori.LRU){  inList.increaseWhenTlbAndLru(RAMNumber);
      }
        
        return RAMNumber;
    }
    int readResult=inList.ReadPageForm(virtualPageNumber);//返回了物理页号
    changeTlb(progressNumber, virtualPageNumber, readResult);






    return readResult;
}



private int ReadPageForm(MyProcessTarget2 inList ,int virtualPageNumber){
return inList.ReadPageForm(virtualPageNumber);
}

/* */
public int TryDoRead(int progressNumber,int virtualPageNumber){
    boolean helpJudge=false;
    MyProcessTarget2 helpProcess=null;
    for (MyProcessTarget2 process : ObjectList) {
        if(process.getprocessId()==progressNumber)
      {helpJudge=true;
      helpProcess=process;
      break;}
    }
    
    if(!helpJudge){System.out.println("您想访问的进程不存在");
    return -999;    
 }  if(helpProcess==null){
    throw new RuntimeException("处理中断，进程未完成");

    //这个是应该不会被触发才对
 }   
if( virtualPageNumber<=0||(virtualPageNumber>helpProcess.getVirtualPageCount()) )//虚拟页号是1到virtualPageCount
    {System.out.println("您想访问的"+progressNumber+"号进程没有"+"该地址");
    return -999;
}
//然后给do Read 不要都写在一个函数里



return doRead(progressNumber, virtualPageNumber,helpProcess);
}




 public boolean tryToDeleteAProgress(int deleteProgressId){
   boolean  hasThisProgress=false;
    for (MyProcessTarget2 process : ObjectList) {
       if(process.getprocessId()==deleteProgressId){
       hasThisProgress=true;
        break;
       }
    }
        if(!hasThisProgress){
            System.out.println("您尝试删除的进程本身就不存在");
            return false;
        }
//这里怎么delete一会儿要看一下语法
//int 
Iterator<MyProcessTarget2> iterator = ObjectList.iterator();
boolean helpAgain=false;
while (iterator.hasNext()) {
    MyProcessTarget2 processTarget = iterator.next();
    if (processTarget.getprocessId() == deleteProgressId) {
deletedProcessMissingTime+=processTarget.getPageTableMissTime();
deletedProcessReadTime+=processTarget.getReadPagetime();
iterator.remove(); // 删除当前对象
    helpAgain=true;
    }
}
if(!helpAgain){
    throw new RuntimeException("应该是能删除到"+deleteProgressId+"的，但是出现问题了");
}
for(int i=0;i<16;i++ ){
    if(progressId[i]==deleteProgressId){
        progressId[i]=-1;
    }

}
//在这里把tlb也删了
for(int i=0;i<2;i++){
if(TLBArray[i].getPageInFoprogressNumber()==deleteProgressId){
    
    System.out.println(TLBArray[i]+"即将被删除");
    TLBArray[i]=new PageInfo(-1, -1, -1);
}
}
System.out.println("删除"+deleteProgressId+"成功");

        return true;
    }



public void changeAL(algori NowAL ,algori Targetal){
    if(NowAL==Targetal){
        System.out.println("源算法和目的算法相同");
        return;}

    System.out.println("将替换算法从"+NowAL+"变为"+Targetal);
    //替换算法前
    showPerformance();
    ALGORI=Targetal;
    TLBhitTime=0;
    totalReadTime=0;
    deletedProcessMissingTime=0;
    deletedProcessReadTime=0;
    if(!ObjectList.isEmpty()){
    for (MyProcessTarget2 process : ObjectList) {
       process.swtichAL(NowAL, Targetal);
    }
}else{
    System.out.println("目前没有存在的进程，算法替换完成");
}

}
public algori returnAlGORI(){
    return ALGORI;
}
public void showPerformance(){
   showNowALGORI();
   double ExisteachTotalread=0;
   double ExistMissTime=0;
   for (MyProcessTarget2 process : ObjectList) {
    ExisteachTotalread+=process.getReadPagetime();
ExistMissTime+=process.getPageTableMissTime();
}

    if(ExisteachTotalread+TLBhitTime+deletedProcessReadTime!=totalReadTime){
        throw new RuntimeException("数量对不上");
    }else{
        System.out.println("从开始执行目前的算法到目前为止");
        System.out.println("一共进行了"+totalReadTime+"次访问");
       System.out.println("TLB命中"+TLBhitTime+"次");
        System.out.println("访问已经被删除的进程页表一共"+deletedProcessReadTime+"次，其中有"+deletedProcessMissingTime+"次缺页");
        System.out.println("访问目前内存内的页表"+ExisteachTotalread+"次其中有"+ExistMissTime+"缺页，缺页率为");
        if(totalReadTime!=0){
            System.out.println("总TLB命中率为"+TLBhitTime/totalReadTime);
            System.out.println("总缺页率为"+(ExistMissTime+deletedProcessMissingTime) /totalReadTime);
            System.out.println();
        }
    }   
  

}


}

/************************************************************************* */
 class PageInfo {
    int progressNumber;
    int virtualPageNumber;
    int RamNumber;
    int recentUseCount;
    public int getPageInFoprogressNumber(){
        return progressNumber;
    }

    public PageInfo(int progressNumber, int virtualPageNumber, int number) {
        this.progressNumber = progressNumber;
        this.virtualPageNumber = virtualPageNumber;
        this.RamNumber = number;
        recentUseCount=0;
    }
    public PageInfo(int progressNumber, int virtualPageNumber, int number,int RecentUseCount) {
        this.progressNumber = progressNumber;
        this.virtualPageNumber = virtualPageNumber;
        this.RamNumber = number;
        recentUseCount=RecentUseCount;
    }
    public void recentUseCountSubIfOtherReplace(){
        if(recentUseCount>=2) recentUseCount--;
    }
    public int getRecentUseCount(){
return recentUseCount;
    }

    public int isThisRow(int ProgressNumber,int VirtualPageNumber){//tlb命中时要给那一项的recent值加1
        if(ProgressNumber==this.progressNumber&&VirtualPageNumber==this.virtualPageNumber)
        {
           // recentUseCount++;
            return  RamNumber;
        }
        else
        return -1;
    }

    public void recentUsePlus(){
        recentUseCount++;
    }
    public String toString(){
        return "在TLB中访问到"+progressNumber+"号进程中"+virtualPageNumber+"号虚拟页号在"+RamNumber+"号物理页框中";
    }
   
}


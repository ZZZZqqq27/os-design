package target2;
import java.util.LinkedHashMap;
import java.util.Map;
import java.nio.channels.Pipe;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.*;
public class MyProcessTarget2 {
/*
 * 由于一个进程创建完了之后在内存中占据的位置已经固定了，所以在页表中只要改，哪行是哪行就行了，值都是index+1的值
 * （只要初始化的时候是+1就可以）
 * 
 * 
 */


//后面这里要有一个缺页中断利用率的统计，这个后面有时间再弄
    //private int 
    private double readPagetime;
    private void readPagetimereset(){
        readPagetime=0;
    }
    private void readPagetimePlusOne(){
        readPagetime++;
    }
    public double getReadPagetime(){
        return readPagetime;
    }
    private double PageTableMissTime;
    
    private void PageTableMissTimeReset(){
        PageTableMissTime=0;
    }
    private void PageTableMissTimePlusOne(){
        PageTableMissTime++;
    }
    public double getPageTableMissTime(){
        return PageTableMissTime;
    }
    private int CLOCKHELPINT;
    private int processId;
    private int virtualPageCount;//这个进程的虚拟页的总数
    private int PhysicalPageNumber;//这个进程的物理页框的总数
    private algori ALGORI ;
    private  int[] PageTable;//页表里存着的是+1的
    private int [][] CLOCK;//CLCOK 每个的int [][0]装虚拟页号 int[][1]装物理页号 存着的都是+1的
                            //物理页号是正的代表标志位是1，负的代表是0，再轮到要被替换掉
    //HashMap<Integer, Integer> LRUhashMap ;//LRU 存着的都是+1的
    HashMap<Integer, int[]> LRUhashMap = new HashMap<>();//key是物理页号，后面是  int[1]是次数
//最后实现的实际上不是low recent 是low total

    LinkedList<int[]> FIFO ; //FIFO  存着的都是+1的 同样的 int[0]是物理页号，int[1]是虚拟页号，
    
    public MyProcessTarget2(int ID ,int VPC,int PhysiPN,algori ALGORIFromRam,int []Init ){
        this.ALGORI=ALGORIFromRam;
        PhysicalPageNumber=PhysiPN;
        processId=ID;
virtualPageCount=VPC;

        readPagetime=0;
        PageTableMissTime=0;
        PageTable=new int[VPC];
        LRUhashMap=new HashMap<>();
        CLOCK=new int[PhysicalPageNumber][2];
        FIFO=new LinkedList<>();
        for(int i=0;i<VPC;i++){
            PageTable[i]=-1;
        }
        switch(ALGORIFromRam){
            case FIFO:
            FIFOInit(Init);
        break;
            case LRU:
            LRUInit(Init);
            break;
            case CLOCK:
            CLOCKInit(Init);
            break;

            default:
            break;
    
    }
        CLOCKHELPINT=0;
    }// 1,2,3,4 -> 5,2*(1),3(1),4 
    public void FIFOInit(int[]Init){/* 这个init是没+1的 */
       
        for(int i=0; i<PhysicalPageNumber;i++){
            int []toADD=new int[2];
            toADD[0]=i+1;
                toADD[1]=Init[i]+1;
                FIFO.add( toADD);
        }

    }
    public void LRUInit(int[]Init){
       //key是物理页号
        for(int i=0;i<PhysicalPageNumber;i++){
            int []toADD=new int[2];
            toADD[0]=i+1;//虚拟页号
                toADD[1]=0;
            LRUhashMap.put(Init[i]+1, toADD);

        }

    }
    public void CLOCKInit(int[]Init){
for(int i=0;i<PhysicalPageNumber;i++){
CLOCK[i][0]=i+1;//虚拟页号,装的
CLOCK[i][1]=Init[i]+1;//实际的物理页号

}


    }
    public void increaseWhenTlbAndLru(int RamNumber){
       int [] VALUE= LRUhashMap.get(RamNumber);
    VALUE[1]++;
    LRUhashMap.put(RamNumber,VALUE);        
}

    public int getprocessId(){
        return processId;
    }
    public int getVirtualPageCount(){
        return virtualPageCount;
    }
    public void writePageTable(int row,int realPageFrameNumber){
        PageTable[row]=realPageFrameNumber;

    }
    //pub
    public void swtichAL(algori sourceAlgori,algori targetAlgori){
        readPagetimereset();
        PageTableMissTimeReset();
       if(sourceAlgori==algori.FIFO&&targetAlgori==algori.LRU)FIFOtoLRU();
        if(sourceAlgori==algori.FIFO&&targetAlgori==algori.CLOCK)FIFOtoCLOCK();
        if(sourceAlgori==algori.LRU&&targetAlgori==algori.FIFO)LRUtoFIFO(); 
        if(sourceAlgori==algori.LRU&&targetAlgori==algori.CLOCK)LRUtoCLOCK();
        if(sourceAlgori==algori.CLOCK&&targetAlgori==algori.FIFO)CLOCKtoFIFO();
       if(sourceAlgori==algori.CLOCK&&targetAlgori==algori.LRU) CLOCKtoLRU();
    }   
    private  void FIFOtoLRU()
    {  
        //很简单，从队首先出来的，被访问次数是0，后面的依次递增即可
        System.out.println("进程"+processId+"切换完毕");
    }
    private void FIFOtoCLOCK(){
       // //两个多的标志位给1，两个少的标志位给0
    //指针放在第一个零上
        System.out.println("进程"+processId+"切换完毕");
    }
    private void LRUtoFIFO(){
        //和FIFOtoLRU返回来
        System.out.println("进程"+processId+"切换完毕");
    }
    private void LRUtoCLOCK(){
        //两个多的标志位给1，两个少的标志位给0
        System.out.println("进程"+processId+"切换完毕");
    }
    private void CLOCKtoFIFO(){
  //不好做模拟，就随缘写了  
        System.out.println("进程"+processId+"切换完毕");
    }
    private void CLOCKtoLRU(){
//也简单写了，标志位是1的给访问次数是2，标志位是0的，给访问次数是1

        System.out.println("进程"+processId+"切换完毕");
    }
    public int ReadPageForm(int virtualPageNumber){//返回的是物理页号
readPagetimePlusOne();//每次访问

        //这里可能会引到缺页中断
        if(PageTable[  virtualPageNumber-1 ]>0){
        
            changeDSWhenHit(PageTable[  virtualPageNumber-1 ]);
            return PageTable[  virtualPageNumber-1 ];///***********后面如果有溢出或者是报错，注意这个地方 */
        }
        else{
            System.out.println("进程"+processId+"的"+virtualPageNumber+"页不在内存中,发生缺页中断");
            return PageMissing(virtualPageNumber);
        }
    }
    private void changeDSWhenHit(int RamPageNumber){
        switch(ALGORI){
case FIFO://啥都不用做
break ;
case LRU:
            LRUchangeWhenHit(RamPageNumber);
break;
case   CLOCK ://这里我们不扫表盘，只循环查找一下把对应的标志位置为1
            CLOCKchangeWhenHit(RamPageNumber);
break;
}
    }
    private void LRUchangeWhenHit(int  RamPageNumber){//给对应的物理页号的访问次数加一
        int[] array = LRUhashMap.get(RamPageNumber);
        if (array == null) {
            throw new RuntimeException("LRU没有正确维护");
        }
        array[1] += 1; // 对数组第一个元素加1
        LRUhashMap.put(RamPageNumber, array); // 更新HashMap中的值

    }
    private void CLOCKchangeWhenHit(int RamPageNumber){
        boolean find=false;
        for(int i=0;i<PhysicalPageNumber;i++){

          if(RamPageNumber== Math.abs(CLOCK[i][1])){
            CLOCK[i][1]=Math.abs(CLOCK[i][1]);
            find=true;
            i=PhysicalPageNumber;
          }
        }
        
        if(!find){
            throw new RuntimeException("CLOCK算法没有正确维护");
        }
    }


    private int PageMissing(int virtualPageNumber ){//
    PageTableMissTimePlusOne();//
        int []changeInfo;//第一个是选择了哪个物理页框，第二个是原来被替换出去的虚拟页框号是多少
    
        switch (this.ALGORI) {
            case LRU:
            changeInfo =   LRUMissing(virtualPageNumber);
                break;
            case FIFO:
         changeInfo =    FIFOMissing(virtualPageNumber); 

                break;
                case CLOCK:
           changeInfo=  CLOCKMissing(virtualPageNumber);
                break;
                default:
                throw new RuntimeException("在切换算法时枚举类型出错了");
        }
        System.out.println(ALGORI+"进程"+processId+"把进程"+changeInfo[1]+"号虚拟页框从"+changeInfo[0]+"号物理页框替换了出去，"+"把"+virtualPageNumber+"号虚拟页面替换了进来");
        writePageTable(virtualPageNumber-1, changeInfo[0]);//写页表
        return changeInfo[1];
    }
    
    private int[] LRUMissing(int virtualPageNumber){//缺少异常处理
        int[]result=new int[2]; //虚拟页号和物理页号都是加一的
       //在替换时要把其他没有被换掉的相关数据减少一点
       Integer minKey = null;
int minValue = Integer.MAX_VALUE;

for (HashMap.Entry<Integer, int[]> entry : LRUhashMap.entrySet()) {
    int[] valueArray = entry.getValue();
    if (valueArray.length > 1 && valueArray[1] < minValue) {
        minValue = valueArray[1];
        minKey = entry.getKey();
    }
    //valueArray[1] =valueArray[1]>=3? valueArray[1]-1: valueArray[1];
   // LRUhashMap.put(entry.getKey(), valueArray);
   //可以进行各种设计，这里就用最基础的，不减少，并且给1
}
result[0]=minKey;
result[1]=LRUhashMap.get(minKey)[0];
int [] WantToInsert=new int[2] ;
WantToInsert[0]=virtualPageNumber;
       WantToInsert[1]=1;//这里不要给1
       LRUhashMap.put(minKey,WantToInsert);
    
        return result;
    }
    private int[] FIFOMissing(int virtualPageNumber){//虚拟页号没有减1，改页表要改减过的index
        int[]result=new int[2]; 
        int[] firstElementRemoved = FIFO.poll();
      result[1]= firstElementRemoved[1];
       result[0]=firstElementRemoved[0];
       firstElementRemoved[1]=virtualPageNumber;
      FIFO.add(firstElementRemoved); 
        return result;
    }
    private int[] CLOCKMissing(int virtualPageNumber){
        int[]result=new int[2]; //
      boolean BREAK=false;
        CLOCKHELPINT=CLOCKHELPINT%PhysicalPageNumber;
      while(!BREAK){
        if(CLOCK[CLOCKHELPINT][1]==0){

            throw new RuntimeException("clock算法中出现了有的 物理页号是0，不应该是这样");
        }
        if(CLOCK[CLOCKHELPINT][1]>0){
            CLOCK[CLOCKHELPINT][1]*=-1;
            CLOCKHELPINT=(CLOCKHELPINT+1)%PhysicalPageNumber;
        }else{//找到是0的了，可以不用接着循环了
            BREAK=true;
            
            CLOCK[CLOCKHELPINT][1]*=-1;
            result[1]=CLOCK[CLOCKHELPINT][0];
            result[0]=CLOCK[CLOCKHELPINT][1];
            CLOCK[CLOCKHELPINT][0]=virtualPageNumber;
            
            CLOCKHELPINT=(CLOCKHELPINT+1)%PhysicalPageNumber;
            
        }
       
    }
      
        return result;
    }
   
}

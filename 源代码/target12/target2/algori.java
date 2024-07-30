package target2;

 public enum algori{
    FIFO("目前的单进程帧替换算法是FIFO"),
    LRU("目前的单进程帧替换算法是LRU"),
    CLOCK("目前的单进程帧替换算法是CLOCK");
    private final String description;
    algori(String Description){
this.description=Description;
    }
    public String toString(){
        return description;
    }
}
  在application中初始化
  NetWorkManager.getInstance().init(this,true,"DEBUG");
  在使用中的activity中
   NetWorkManager.getInstance().regiestObserver(this);
  ondestory
   NetWorkManager.getInstance().unRegiestObserver(this);
   网络变化时需要进行处理的方法中加上
  @NetWork()注解
implementation 'com.github.llj1:NetWorkLib:1.0'

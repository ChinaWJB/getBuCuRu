# getBuCuRu
**Docker常用指令：**

    //给当前目录下的文件进行重命名
    String reName = "mv quickSort.jar quick.jar";
    
    //构建docker镜像
    String buildDocker = "docker build -t qsort:v0.1 .";
    
    //根据docker的名字得到dockerId
    String getDockerID = "docker inspect -f {{.ID}} test01";
    
    //查看指定docker（test02）的详细信息
    String getDockerInfo = "docker inspecDt test01";
    
    //运行docker并指定运行内核，“--rm”
    String runDocker = "docker run --rm -v `pwd`/data:/data --cpuset-cpus=6,7 qsort:v0.1";
    
    //创建容器
    String creatContainerGetID = "docker create --name test01 " +
            "--cpuset-cpus=6 --memory=2G -v `pwd`/data:/data qsort:v0.1";
    
    //创建容器，自动挂载程序的jar包，不需要重新创建镜像
    String creatContainerGetIDAuto = "docker create --name test01 " +
            "--cpuset-cpus=6,7 --memory=4G -v `pwd`/data:/data -v `pwd`/quick.jar:/quick.jar qsort:v0.1";
    
    //启动容器
    String startContainer = "docker start test01";
    
    //停止容器
    String stopContainer = "docker stop test01";
    
    //删除容器
    String removeContainer = "docker rm test01";
    
    //停止所有的容器，但是不停止守护进程，-q是只输出所有容器的ID
    String stopAllContainer = "docker stop $(docker ps -q) ";



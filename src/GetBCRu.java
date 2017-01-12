import java.io.*;

/**
 * Created by wjb on 2017/1/10.
 */
public class GetBCRu {
    public static void main(String[] args) {
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

        Process getContainerIDPro = null;
        Process creatConGetIDPro = null;
        Process startContainerPro = null;

        BufferedReader getContainerIDBR = null;
        BufferedReader creatConGetIDBR = null;
        BufferedReader startContainerBR = null;

        String containerID = null;

        int num =1;//打印，输出控制次数

        try {
            //创建容器之前先停止和删除容器
//            Runtime.getRuntime().exec(stopContainer);
//            Thread.sleep(1000);
//            Runtime.getRuntime().exec(removeContainer);
//            Thread.sleep(2000);

            //创建容器
//            creatConGetIDPro = Runtime.getRuntime().exec(creatContainerGetID);
//            Thread.sleep(3000);
//            creatConGetIDPro = new BufferedReader(new InputStreamReader(creatConGetIDPro.getInputStream()));
//            System.out.println(creatConGetIDPro.readLine());
//            System.out.println("Create Container Success ");
//            Thread.sleep(5000);

            //获得容器ID
            getContainerIDPro = Runtime.getRuntime().exec(getDockerID);
            Thread.sleep(2000);
            getContainerIDBR = new BufferedReader(new InputStreamReader(getContainerIDPro.getInputStream()));
            containerID = getContainerIDBR.readLine();
            System.out.println("ContainerID： " + containerID);

            //启动容器
            Runtime.getRuntime().exec(startContainer);
            //startContainerPro = Runtime.getRuntime().exec(startContainer);
            Thread.sleep(2000); //启动容器后休眠2秒，保证容器充分启动
            //startContainerBR = new BufferedReader(new InputStreamReader(startContainerPro.getInputStream()));
            //System.out.println(startContainerBR.readLine());
            System.out.println("Start Container Success");


            //获取缓存cache的命令  Cu
            String[] cmd1 = new String[] { "/bin/sh", "-c", " cat /sys/fs/cgroup/memory/docker/"
                    + containerID + "/memory.stat | grep cache" };
            // String cmd1 = "cat /proc/meminfo | grep Buffers";
            //获取已用内存的命令  Ru
            String[] cmd2 = new String[] { "/bin/sh", "-c", " cat /sys/fs/cgroup/memory/docker/"
                    + containerID + "/memory.usage_in_bytes" };
            /* String cmd2 = "cat /proc/meminfo | grep ^Cached"; */

            /********以下为获取Cache和已用内存，并存入文件*********/
            Process getCuProcess;   //获取Cache的子进程
            Process getRuProcess;   //获取已用内存的子进程

            File file = new File("./data/sortBuCuRu"
                    + System.currentTimeMillis() + ".xls");
            FileOutputStream fos = new FileOutputStream(file);

            //循环500次来获取数据，为了能找到，正好是在排序时间段内的BuCuRu
            while(num<5000){
                /********** 获取Cu *********/
                //解析获取缓存cache
                long time1 = System.currentTimeMillis();         //获得系统当前时间，毫秒的形式
                getCuProcess = Runtime.getRuntime().exec(cmd1);  //执行获取Cu的指令

                //获取数据
                BufferedReader br1 = new BufferedReader(new InputStreamReader(getCuProcess.getInputStream()));
                String line1 = br1.readLine();//cache 2998272
                Thread.sleep(20);//休眠20毫秒，控制整个查询的时间
                System.out.println("Cache Line1: " + line1);

                //解析并存入文件
                String[] arr = line1.split("\\s+"); //按空格进行拆分，空格可以是Space,Table等等一个或多个
                fos.write((time1 + " A").getBytes());
                fos.write("\t".getBytes());
                fos.write(arr[1].getBytes());
                fos.write("\t".getBytes());

                /************* 获取Ru ************/
                //解析获取已使用内存
                long time2 = System.currentTimeMillis();         //获得系统当前时间，毫秒的形式
                getRuProcess = Runtime.getRuntime().exec(cmd2);  //执行获取Ru的指令

                //获取数据
                BufferedReader br2 = new BufferedReader(new InputStreamReader(getRuProcess.getInputStream()));
                String line2 = br2.readLine();
                System.out.println("Ru Line2: " + line2);

                //解析并存入文件
                fos.write((time2 + " A").getBytes());
                fos.write("\t".getBytes());
                fos.write(line2.getBytes());
                fos.write("\r\n".getBytes());

                num++;  //次数加1
                System.out.println("num: " + num);
            }
            fos.flush();
            fos.close();
            //等待启动容器子进程完成
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End");
    }
}

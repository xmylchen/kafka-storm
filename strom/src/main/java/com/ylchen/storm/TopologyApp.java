package com.ylchen.storm;
import com.ylchen.constant.Constants;
import com.ylchen.storm.bolt.CountBolt;
import com.ylchen.storm.bolt.InsertBolt;
import com.ylchen.storm.spout.KafkaInsertDataSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.trident.TridentTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class TopologyApp {
    private  final Logger logger = LoggerFactory.getLogger(TopologyApp.class);

    public  void runStorm(String[] args) {
        // 定义一个拓扑
        TopologyBuilder builder = new TopologyBuilder();
        // 设置1个Executeor(线程)，默认一个
        builder.setSpout(Constants.KAFKA_SPOUT, new KafkaInsertDataSpout(), 2);
        // shuffleGrouping:表示是随机分组
        // 设置1个Executeor(线程)，和两个task
        //first level
        builder.setBolt("Y_SEND", new CountBolt(),40).setNumTasks(1).shuffleGrouping(Constants.KAFKA_SPOUT);
        //second level
        builder.setBolt(Constants.INSERT_BOLT, new InsertBolt(), 80).setNumTasks(1).shuffleGrouping("Y_SEND");
        Config conf = new Config();
        //设置一个应答者
        conf.setNumAckers(2);
        //设置一个work
        conf.setNumWorkers(2);
        try {
            // 有参数时，表示向集群提交作业，并把第一个参数当做topology名称
            // 没有参数时，本地提交
            if (args != null && args.length > 0) {
                logger.info("运行远程模式");
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } else {
                // 启动本地模式
                logger.info("运行本地模式");
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology("TopologyApp", conf, builder.createTopology());
            }
        } catch (Exception e) {
            logger.error("storm启动失败!程序退出!",e);
            System.exit(1);
        }

        logger.info("storm启动成功...");

    }
}

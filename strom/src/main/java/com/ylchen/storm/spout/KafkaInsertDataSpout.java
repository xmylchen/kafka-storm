package com.ylchen.storm.spout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.ylchen.constant.Constants;
import com.ylchen.model.User;
import com.ylchen.util.GetSpringBean;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;


/**
 *
 */
public class KafkaInsertDataSpout extends BaseRichSpout{

    /**
     *
     */
    private static final long serialVersionUID = -2548451744178936478L;

    private static final Logger logger = LoggerFactory.getLogger(KafkaInsertDataSpout.class);

    private SpoutOutputCollector collector;

    private KafkaConsumer<String, String> consumer;

    private ConsumerRecords<String, String> msgList;



    @SuppressWarnings("rawtypes")
    @Override
    public void open(Map map, TopologyContext arg1, SpoutOutputCollector collector) {
        kafkaInit();
        this.collector = collector;
    }


    @Override
    public void nextTuple() {
        for (;;) {
            try {
                msgList = consumer.poll(100);
                if (null != msgList && !msgList.isEmpty()) {
                    String msg = "";
                    List<User> list=new ArrayList<User>();
                    for (ConsumerRecord<String, String> record : msgList) {
                        // 原始数据
                        msg = record.value();
                        if (null == msg || "".equals(msg.trim())) {
                            continue;
                        }
                        try{
                            list.add(JSON.parseObject(msg, User.class));
                        }catch(Exception e){
                            logger.error("数据格式不符!数据:{}",msg);
                            continue;
                        }
                    }
                    logger.info("Spout发射的数据:"+list);
                    //发送到bolt中
                    this.collector.emit(new Values(JSON.toJSONString(list)));
                    consumer.commitAsync();
                }else{
                    TimeUnit.SECONDS.sleep(3);
                    logger.info("未拉取到数据...");
                }
            } catch (Exception e) {
                logger.error("消息队列处理异常!", e);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e1) {
                    logger.error("暂停失败!",e1);
                }
            }
        }
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(Constants.FIELD));
    }

    /**
     * 初始化kafka配置
     */
    private void kafkaInit(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.127:9092");
        props.put("max.poll.records", "100");
        props.put("enable.auto.commit", true);
        props.put("group.id", "0");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        String topic="ylchen";
        this.consumer.subscribe(Arrays.asList(topic));
        logger.info("消息队列[" + topic + "] 开始初始化...");
    }
}

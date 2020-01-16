package com.ylchen.storm.bolt;

import com.ylchen.constant.Constants;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class CountBolt extends BaseRichBolt {
    private OutputCollector collector;
    private int count=0;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String countId = tuple.getStringByField(Constants.FIELD);
        if (countId !=null || !countId.isEmpty()){
            count ++;
        }
        System.out.print(countId);
        collector.emit(new Values(countId));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(Constants.COUNT));
    }
}

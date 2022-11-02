package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrEvent extends AggregationModel {
    public AggrEvent() {
        fields = Arrays.asList(
                "action",
                "class",
                "dir",
                "dst_addr",
                "dst_country_code",
                "dst_country_name",
                "dst_lat",
                "dst_long",
                "dst_port",
                "eth_dst",
                "eth_len",
                "eth_src",
                "eth_type",
                "gid",
                "iface",
                "ip_id",
                "ip_len",
                "mpls",
                "msg",
                "pkt_gen",
                "pkt_len",
                "pkt_num",
                "priority",
                "proto",
                "rev",
                "rule",
                "sensor_id",
                "service",
                "sid",
                "src_addr",
                "src_country_code",
                "src_country_name",
                "src_lat",
                "src_long",
                "src_port",
                "tcp_ack",
                "tcp_flags",
                "tcp_len",
                "tcp_seq",
                "tcp_win",
                "tos",
                "ttl",
                "vlan"
        );
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
        topic = "event_all_10s";
        dropRowIfNull = false;
    }
}

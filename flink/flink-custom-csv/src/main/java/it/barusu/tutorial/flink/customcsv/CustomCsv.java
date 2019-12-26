package it.barusu.tutorial.flink.customcsv;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple12;
import org.apache.flink.api.java.utils.ParameterTool;

import java.io.Serializable;

public class CustomCsv {
    public static void main(String... args) {

        ParameterTool params = ParameterTool.fromArgs(args);

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        if (!params.has("input") || !params.has("output")) {
            System.out.println("Usage: command --input <input_file> --output <output_file>");
        }

        DataSet<Data> data = env.readCsvFile(params.get("input"))
                .fieldDelimiter(",")
                .includeFields(true, true, true, true, true, true,
                        true, true, true, true, true, true)
                .pojoType(Data.class);

//        data.map((d, (Tuple12<String, String, String, String, String, String,
//                String, String, String, String, String, String>) t) -> {
//
//        })

        // todo un-implement


    }

    @Getter
    @Setter
    @ToString
    public static class Data implements Serializable {

        private String name;
        private String code;
        private String domain;
        private String password;
        private String serverPort;
        private String vip;
        private String master;
        private String slave;
        private String sentinel;
        private String memory;
        private String cpu;
        private String physicsMemory;


        public Data(String name, String code, String domain, String password,
                    String serverPort, String vip, String master, String slave,
                    String sentinel, String memory, String cpu, String physicsMemory) {
            this.name = name;
            this.code = code;
            this.domain = domain;
            this.password = password;
            this.serverPort = serverPort;
            this.vip = vip;
            this.master = master;
            this.slave = slave;
            this.sentinel = sentinel;
            this.memory = memory;
            this.cpu = cpu;
            this.physicsMemory = physicsMemory;
        }
    }
}

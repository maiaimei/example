package cn.maiaimei.example.config;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "sqls")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Sqls {
    @XmlElement(name = "sql")
    private List<Sql> sqls;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Sql {
        @XmlAttribute(name = "key")
        private String key;

        @XmlValue
        private String value;
    }
}

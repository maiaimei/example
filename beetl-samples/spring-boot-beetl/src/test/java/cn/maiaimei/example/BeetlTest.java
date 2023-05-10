package cn.maiaimei.example;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class BeetlTest {
    
    @Test
    void testCase1() throws IOException {
        // 初始化模板资源加载器
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        // 配置Beetl，这里使用默认配置
        Configuration config = Configuration.defaultConfiguration();
        // 初始化Beetl的核心GroupTemplate
        GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, config);
        // 自定义模板
        String tpl = "<html>\n"+
                "\t<head>\n"+
                "\t\t<title>${title}</title>\n"+
                "\t</head>\n"+
                "\t<body>\n"+
                "\t\t<h1>${name}</h1>\n"+
                "\t<body>\n"+
                "</html>";
        // 通过GroupTemplate传入自定义模板加载出Beetl模板Template
        Template template = groupTemplate.getTemplate(tpl);
        // 使用Template中的操作，将数据与占位符绑定
        template.binding("title","Beetl使用Demo");
        template.binding("name","Beetl使用练习1：直接绑定数据");
        // 渲染字符串
        String str = template.render();
        System.out.println(str);
    }

    @Test
    void testCase2() throws IOException {
        // 初始化模板资源加载器
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        // 配置Beetl，这里使用默认配置
        Configuration config = Configuration.defaultConfiguration();
        // 初始化Beetl的核心GroupTemplate
        GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, config);
        // 自定义模板
        String tpl = "<html>\n"+
                "\t<head>\n"+
                "\t\t<title>Beetl使用Demo</title>\n"+
                "\t</head>\n"+
                "\t<body>\n"+
                "\t\t<h1>Beetl使用练习2：使用Map绑定数据</h1>\n"+
                "\t\t<%" +
                " for(var entry in map){" +
                " println(entry.key+\":\"+entry.value);" +
                " }"+
                "%>\n"+
                "\t<body>\n"+
                "</html>";
        // 通过GroupTemplate传入自定义模板加载出Beetl模板Template
        Template template = groupTemplate.getTemplate(tpl);
        // 使用Template中的操作，将数据与占位符绑定
        Map<String, String> map = new HashMap<>();
        map.put("title","Beetl使用Demo");
        map.put("name","Beetl使用练习2：使用Map绑定数据");
        template.binding("map",map);
        // 渲染字符串
        String str = template.render();
        System.out.println(str);
    }
    
    @Test
    void testCase3() throws IOException {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/templates");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate("case3.btl");
        t.binding("x","123456");
        String str = t.render();
        System.out.println(str);
    }
    
}

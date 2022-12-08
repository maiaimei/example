package cn.maiaimei.demo.ioc.color;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ColorImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                "cn.maiaimei.demo.ioc.color.Yellow"
        };
    }
}

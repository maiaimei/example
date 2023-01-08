# bean-validation

[java代码简洁之道 用bean validation和hibernate validator提升代码质量,让代码少点臭味道](https://www.bilibili.com/video/BV17i4y157Ah)

## 常用的校验约束注解

<img src="./images/20230108101046.png" />

<img src="./images/20230108101247.png" />

## 约束和校验类的绑定原理

XxxValidator 校验 @Xxx，如：`NotBlankValidator` 校验 `@NotBlank`

XxxValidator 实现接口 `javax.validation.ConstraintValidator`

XxxValidator 和 @Xxx 的绑定关系在`org.hibernate.validator.internal.metadata.core.ConstraintHelper` 设置

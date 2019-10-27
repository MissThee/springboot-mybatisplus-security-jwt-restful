```properties
    spring.jpa.show-sql=true
    #无修改命名
    #spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    #遇到大写字母 加”_”的命名
    spring.jpa.hibernate.naming.physical-strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
    #自动生成实体类
    spring.jpa.generate-ddl=true
    #生成方式，删除再创建、更新等
    spring.jpa.hibernate.ddl-auto=create-drop
    #create-drop
    #update
```
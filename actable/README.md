# mybatis-enhance-actable-1.0-SNAPSHOT

mybatis-enhance-actable是对Mybatis做的增强功能，支持SpringBoot以及传统的SpringMvc项目结构，简单配置即可，该框架是为了能够使习惯了hibernate框架的开发者能够快速的入手Mybatis， 
基于Spring和Mybatis的Maven项目，增强了Mybatis的功能，过配置model注解的方式来创建表，修改表结构，并且实现了共通的CUDR功能提升开发效率，同时能够兼容tk.mybatis和mybatis-plus，如需使用依赖相关的pom依赖即可，目前仅支持Mysql，后续会扩展针对其他数据库的支持。

mybatis-enhance-actable是采用了Spring、Mybatis技术的Maven结构，详细介绍如下：

 **基本使用规范**

1.需要依赖mybatis-enhance-actable-1.0-SNAPSHOT.jar

```
    <dependency>
        <groupId>org.cmcc.service.log</groupId>
        <artifactId>mybatis-enhance-actable</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <!-- 如需使用tk.mybatis同时需要依赖该jar包，如需使用自行添加依赖即可 -->
    <dependency>
        <groupId>tk.mybatis</groupId>
        <artifactId>mapper-spring-boot-starter</artifactId>
        <version>2.1.5</version>
    </dependency>
```

2.需要配置对于actable支持的配置

```
    actable.table.auto=update
    actable.model.pack=com.xxx.store.model(ps:要扫描的用于建表做依据的model目录)
    actable.database.type=mysql
    
    本系统提供四种模式：
    
    2.1 当actable.table.auto=create时，系统启动后，会将所有的表删除掉，然后根据model中配置的结构重新建表，该操作会破坏原有数据。
    
    2.2 当actable.table.auto=update时，系统会自动判断哪些表是新建的，哪些字段要修改类型等，哪些字段要删除，哪些字段要新增，该操作不会破坏原有数据。
    
    2.3 当actable.table.auto=none时，系统不做任何处理。
    
    2.4 当actable.table.auto=add，新增表/新增字段/新增索引/新增唯一约束的功能，不做做修改和删除 (只在版本1.0.9.RELEASE及以上支持)。
    
    2.5 actable.model.pack这个配置是用来配置要扫描的用于创建表的对象的包名
        
    2.6 actable.database.type这个是用来区别数据库的，预计会支持这四种数据库mysql/oracle/sqlserver/postgresql，但目前仅支持mysql

```

3. 支持actable的mybatis配置(必备的配置)

```
    <!-- mybatis的配置文件中需要做两项配置，因为mybatis-enhance-actable项目底层是直接依赖mybatis的规范执行sql的，因此需要将其中的mapping和dao映射到一起 -->
    1. classpath*:org/cmcc/service/log/mapping/*/*.xml
    2. org.cmcc.service.log.dao.*
```

4. 扫描actable的包到spring容器中管理(必备的配置)

```
    1. org.cmcc.service.log.manager.*
```

 **Springboot+Mybatis的项目使用步骤方法**

1. 首先pom文件依赖actable框架

```
    <dependency>
        <groupId>org.cmcc.service.log</groupId>
        <artifactId>mybatis-enhance-actable</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <!-- 如需使用tk.mybatis同时需要依赖该jar包，之前1.3.1和1.3.0版本默认依赖了，但是有的小伙伴使用mybatis-plus会有冲突，所以这一版本默认不依赖，如需使用自行依赖即可 -->
    <dependency>
        <groupId>tk.mybatis</groupId>
        <artifactId>mapper-spring-boot-starter</artifactId>
        <version>2.1.5</version>
    </dependency>
```
    
2. 项目的application.properties文件配置例如下面

```
    # actable的配置信息
    actable.table.auto=update
    actable.model.pack=com.xxx.store.model(ps:要扫描的model目录)
    actable.database.type=mysql
    # mybatis的配置信息，key也可能是：mybatis.mapper-locations
    mybatis.mapperLocations=classpath*:org/cmcc/service/log/mapping/*/*.xml;自己的mapper.xml没有可不填
```

3. springboot启动类需要做如下配置

```
    1. 通过注解@ComponentScan配置，扫描actable要注册到spring的包路径"org.cmcc.service.log.manager.*"
    2. 通过注解@MapperScan配置，扫描mybatis的mapper，路径为"org.cmcc.service.log.dao.*"
```

 **传统Spring+Mybatis的Web项目使用步骤方法** 
 
1. 首先pom文件依赖actable框架

```
    <dependency>
        <groupId>org.cmcc.service.log</groupId>
        <artifactId>mybatis-enhance-actable</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <!-- 如需使用tk.mybatis同时需要依赖该jar包 -->
    <dependency>
        <groupId>tk.mybatis</groupId>
        <artifactId>mapper-spring-boot-starter</artifactId>
        <version>2.1.5</version>
    </dependency>
```

2. 在你的web项目上创建个目录比如config下面创建个文件autoCreateTable.properties文件的内容如下：

```
    actable.table.auto=update
    actable.model.pack=com.xxx.store.model(ps:要扫描的model目录)
    actable.database.type=mysql
```

3. spring的配置文件中需要做如下配置：
```
    <!-- 自动扫描(自动注入mybatis-enhance-actable的Manager)必须要配置，否则扫描不到底层的mananger方法 -->
    <context:component-scan base-package="org.cmcc.service.log.manager.*" />
    
    <!-- 这是mybatis-enhance-actable的功能开关配置文件,其实就是将上面第2点说的autoCreateTable.properties文件注册到spring中，以便底层的mybatis-enhance-actable的方法能够获取到-->
    <bean id="configProperties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
        <property name="locations">
            <list>
                <value>classpath*:config/autoCreateTable.properties</value>
            </list>
        </property>
    </bean>
    <bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer">
        <property name="properties" ref="configProperties" />
    </bean>
    
    <!-- mybatis的配置文件中需要做两项配置，因为mybatis-enhance-actable项目底层是直接依赖mybatis的规范执行sql的，因此需要将其中的mapping和dao映射到一起 -->
    1. classpath*:org/cmcc/service/log/mapping/*/*.xml
    2. org.cmcc.service.log.dao.*
    
    举例这两个配置配置的详细位置
    
    <!-- myBatis文件 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <!-- 自动扫描entity目录, 省掉Configuration.xml里的手工配置 -->
        <property name="mapperLocations">
            <array>
              <value>classpath*:自己的mappring.xml没有可不填</value>
              <value>classpath*:org/cmcc/service/log/mapping/*/*.xml</value>
            </array>
        </property>
        <property name="typeAliasesPackage" value="自己的model.*没有可不填" />
        <!-- <property name="configLocation" value="classpath:core/mybatis-configuration.xml" /> -->
    </bean>
    

    <!-- 如果不使用tk.mybatis需要使用这个bean成如下： -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="org.cmcc.service.log.dao.*;自己的dao.*没有可不填" />
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
    </bean>

    <!-- 如果要使用tk.mybatis需要使用这个bean成如下： -->
    <bean class="tk.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="org.cmcc.service.log.dao.*;自己的dao.*没有可不填"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <property name="properties">
            <value>
                mappers=tk.mybatis.mapper.common.Mapper,tk.mybatis.mapper.common.special.InsertListMapper
            </value>
        </property>
    </bean>
```
	
**代码用途讲解** 

    1. MySqlCharsetConstant.java这个对象里面配置的是mysql的数据类型，这里配置的类型越多，意味着创建表时能使用的类型越多
    
    2. @Column.java也是一个自定义的注解，用于标记model中的字段上，作为创建表的依据如不标记，不会被扫描到，有几个属性用来设置字段名、字段类型、长度等属性的设置，详细请看代码上的注释
    
    3. @Table.java也是一个自定义的注解，用于标记在model对象上，有一个属性name，用于设置该model生成表后的表名，如不设置该注解，则该model不会被扫描到
    
    4. @Index.java是一个自定义注解，用于标记在model中的字段上，表示为该字段创建索引，有两个属性一个是设置索引名称，一个是设置索引字段，支持多字段联合索引，如果都不设置默认为当前字段创建索引
    
    5. @Unique.java是一个自定义注解，用于标记在model中的字段上，表示为该字段创建唯一约束，有两个属性一个是设置约束名称，一个是设置约束字段，支持多字段联合约束，如果都不设置默认为当前字段创建唯一约束
    
    6. @TableComment用来配置表的注释，可用来替代@Table的comment
    
    7. @IsKey/@IsAutoIncrement/@IsNotNull用来代替 @Column中的isKey/isAutoIncrement/isNull三个属性，当然旧的配置方式仍然是支持的 
    
    8. @ColumnComment字段注释，用来替代@Column中的comment
    
    9. @DefaultValue字段默认值，用来替代@Column中的defaultValue
    
    10.@ColumnType字段类型，用来替代@Column中的type，取值范围MySqlTypeConstant.java中的枚举
    
    11.@TableCharset表字符集，用来替代@Table中的charset，取值范围MySqlCharsetConstant.java中的枚举
    
    12.@TableEngine表引擎类型，用来替代@Table中的engine，取值范围MySqlEngineConstant.java中的枚举
    
    13.支持javax.persistence包中的部分注解，用于对tk.mybatis做支持
    
        javax.persistence.Column         同       Column
        javax.persistence.Column.name    同       Column.name
        javax.persistence.Column.length  同       Column.length
        javax.persistence.Column.scale   同       Column.decimalLength
        javax.persistence.Table          同       Table
        javax.persistence.Id             同       IsKey
    
    13.系统启动后会去自动调用SysMysqlCreateTableManagerImpl.java的createMysqlTable()方法，没错，这就是核心方法了，负责创建、删除、修改表。

 **model的写法例子(这里的@Table和@Column都是用的actable中的，也支持使用javax.persistence包下的@Table和@Column以及@Id)**
```
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test")
@TableComment("测试表")
@TableEngin(MySqlEngineConstant.InnoDB)
@TableCharset(MySqlCharsetConstant.UTF8MB4)
public class UserEntity extends BaseModel{

    private static final long serialVersionUID = 5199200306752426433L;
    
    // 第一种主键设置方式
    @Column(name = "id",type = MySqlTypeConstant.INT,length = 11,isKey = true,isAutoIncrement = true)
    private Integer	id;
    
    // 第二种简化的主键设置方式
    @IsKey
    @IsAutoIncrement
    @Column
    @ColumnType(MySqlTypeConstant.INT)
    private Integer	id;
    
    // 第一种设置索引的方法，这种方法会在数据库默认创建索引名称为actable_idx_{login_name},索引字段为login_name
    @Index
    // 第二种设置索引的方法，这种方法会在数据库创建索引名称为actable_idx_{t_idx_login_name},索引字段为login_name
    @Index("t_idx_login_name")
    // 第三种设置索引的方法，这种方法会在数据库创建索引名称为actable_idx_{t_idx_login_name},索引字段为login_name
    @Index(value="t_idx_login_name",columns={"login_name"})
    // 第四种设置索引的方法，可以设置联合索引，这种方法会在数据库创建索引名称为actable_idx_{login_name_mobile},索引字段为login_name和mobile
    @Index(columns={"login_name","mobile"})
    // 第五种设置索引的方法，可以设置联合索引，这种方法会在数据库创建索引名称为actable_idx_{login_name_mobile},索引字段为login_name和mobile
    @Index(value="t_idx_login_name_mobile",columns={"login_name","mobile"})
    // 唯一约束的注解的使用方法，跟@Index相同
    @Unique
    @Column(name = "login_name",type = MySqlTypeConstant.VARCHAR,length = 111)
    private String	loginName;
    
    // column的简化写法，不配置默认使用当前属性名作为字段名，当前类型默认转换至mysql支持的类型
    @Column
    private String	mobile;
    
    // column的简化写法，根据需要设置注解属性
    @Column(name = "createTime")
    private Date	create_time;
    
    @Column(name = "update_time",type = MySqlTypeConstant.DATETIME)
    private Date	update_time;
    
    @Column(name = "number",type = MySqlTypeConstant.DECIMAL,length = 10,decimalLength = 2)
    private BigDecimal	number;
    
    // 第一种设置字段非空的方法
    @Column(name = "lifecycle",type = MySqlTypeConstant.CHAR,length = 1,isNull=false)
    // 第二种设置字段非空的方法
    @IsNotNull
    @Column
    private String	lifecycle;
    
    @Column
    @DefaultValue("111")
    @ColumnComment("昵称")
    private String	realName;
}
```
 **@Column不设置类型时的默认转换规则如下（建议类型使用对象类型不要用基本数据类型）**

        javaToMysqlTypeMap.put("class java.lang.String", MySqlTypeConstant.VARCHAR);
        javaToMysqlTypeMap.put("class java.lang.Long", MySqlTypeConstant.BIGINT);
        javaToMysqlTypeMap.put("class java.lang.Integer", MySqlTypeConstant.INT);
        javaToMysqlTypeMap.put("class java.lang.Boolean", MySqlTypeConstant.BIT);
        javaToMysqlTypeMap.put("class java.math.BigInteger", MySqlTypeConstant.BIGINT);
        javaToMysqlTypeMap.put("class java.lang.Float", MySqlTypeConstant.FLOAT);
        javaToMysqlTypeMap.put("class java.lang.Double", MySqlTypeConstant.DOUBLE);
        javaToMysqlTypeMap.put("class java.math.BigDecimal", MySqlTypeConstant.DECIMAL);
        javaToMysqlTypeMap.put("class java.sql.Date", MySqlTypeConstant.DATE);
        javaToMysqlTypeMap.put("class java.util.Date", MySqlTypeConstant.DATE);
        javaToMysqlTypeMap.put("class java.sql.Timestamp", MySqlTypeConstant.DATETIME);
        javaToMysqlTypeMap.put("class java.sql.Time", MySqlTypeConstant.TIME);
        
 **共通的CUDR功能使用**

    1.使用方法很简单，大家在manager或者Controller中直接注入BaseCRUDManager这个接口就可以了
    
    2.旧的BaseMysqlCRUDManager类废弃了请不要使用

    3.注意：接口调用方法时传入的对象必须是modle中用于创建表的对象才可以
    
    4.最新版本1.3.0.RELEASE引入了对tk.mybatis的支持，方便更灵活的CUDR，仅限于使用javax.persistence的注解Column/Table/Id时生效
 
 **AC.Table支持tk.mybatis框架的CUDR方法**
    
    请参考tk.mybatis官方文档使用即可。
    
    import Index;
    import IsAutoIncrement;
    import IsKey;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    
    import javax.persistence.Column;
    import javax.persistence.Id;
    import javax.persistence.Table;
    import java.io.Serializable;
    import java.util.Date;
    
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "user_entity")
    public class UserEntity extends UserEntity1 implements Serializable {
    
        @Id
        @IsKey
        @IsAutoIncrement
        @Column
        private Long id;
    
        @Column(name = "login_name")
        @Index
        private String loginName;
    
        @Column(name = "nick_name")
        private String nickName;
    
        @Column(name = "real_name")
        private String realName;
    
        @Column(name = "password")
        private String password;
    
        @Column(name = "mobile")
        private String mobile;
    
        @Column(name = "istrue")
        private Boolean isTrue;
    
        @Column(name = "it")
        private Integer it;
    
        @Column
        private Date createTime;
    
        @Column
        private Date modifyTime;
    }
    
    import com.alex.orderapi.dao.entity.UserEntity;
    import tk.mybatis.mapper.common.Mapper;
    
    public interface UserMapper extends Mapper<UserEntity> {
    
    }
    
    @RequestMapping("/select/user1")
    public String select1(HttpServletRequest request) {
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("1").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("2").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("3").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("4").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("5").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("6").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("7").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("8").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("9").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("10").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("11").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("12").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("13").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("14").build());
        userMapper.insert(UserEntity.builder().loginName("s").createTime(new Date()).realName("v").password("r").mobile("2").build());
        List<UserEntity> userEntities1 = userMapper.selectAll();
        List<UserEntity> select = userMapper.select(UserEntity.builder().mobile("2").build());
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("mobile","3").andEqualTo("loginName","s");
        List<UserEntity> userEntities = userMapper.selectByExample(example);
        UserEntity v = userMapper.selectOne(UserEntity.builder().id(30L).build());
        UserEntity userEntity = userMapper.selectByPrimaryKey(10);
        List<UserEntity> userEntities2 = userMapper.selectByRowBounds(UserEntity.builder().build(), new RowBounds(0, 3));
        List<UserEntity> userEntities3 = userMapper.selectByExampleAndRowBounds(Example.builder(UserEntity.class).build(), new RowBounds(3, 3));
        Example example1 = new Example(UserEntity.class);
        example1.setOrderByClause("id desc");
        List<UserEntity> userEntities4 = userMapper.selectByExampleAndRowBounds(example1, new RowBounds(0, 3));
        return "selectAll: " + JSON.toJSONString(userEntities1) +
                "<p> select: " + JSON.toJSONString(select) +
                "<p> selectByExample：" + JSON.toJSONString(userEntities) +
                "<p> selectOne：" + JSON.toJSONString(v) +
                "<p> selectByPrimaryKey：" + JSON.toJSONString(userEntity) +
                "<p> selectByRowBounds：" + JSON.toJSONString(userEntities2) +
                "<p> selectByExampleAndRowBounds：" + JSON.toJSONString(userEntities3) +
                "<p> selectByExampleAndRowBounds：" + JSON.toJSONString(userEntities4);
    }

 **BaseCRUDManager使用代码示例**
```
@Controller
public class TestController{
	
	@Autowired
	private TestManager testManager;
	
	@Autowired
	private BaseCRUDManager baseCRUDManager;
	
	/**
	 * 首页
	 */
	@RequestMapping("/testDate")
	@ResponseBody
	public String testDate(){
        UserEntity insert = baseCRUDManager.insert(UserEntity.builder().loginName("111").build());
        UserEntity insertSelective = baseCRUDManager.insertSelective(UserEntity.builder().loginName("222").build());
        List<UserEntity> userEntities1 = baseCRUDManager.selectAll(UserEntity.class);
        boolean b = baseCRUDManager.updateByPrimaryKey(UserEntity.builder().id(1L).mobile("1111").build());
        boolean b1 = baseCRUDManager.updateByPrimaryKeySelective(UserEntity.builder().id(2L).mobile("1111").build());
        UserEntity userEntity = baseCRUDManager.selectOne(UserEntity.builder().id(1L).mobile("1111").build());
        UserEntity userEntity1 = baseCRUDManager.selectByPrimaryKey(UserEntity.builder().id(8L).mobile("1111").build());
        List<UserEntity> select = baseCRUDManager.select(UserEntity.builder().mobile("1111").build());
        int i = baseCRUDManager.selectCount(UserEntity.builder().build());
        int sss = baseCRUDManager.delete(UserEntity.builder().realName("sss").build());
        int i1 = baseCRUDManager.deleteByPrimaryKey(UserEntity.builder().id(5L).loginName("222").build());
        boolean b2 = baseCRUDManager.existsByPrimaryKey(UserEntity.builder().id(1L).build());
        boolean b3 = baseCRUDManager.existsByPrimaryKey(UserEntity.builder().id(222L).build());
        UserEntity user = new UserEntity();
        user.setCurrentPage(1);
        user.setPageSize(5);
        LinkedHashMap<String, String> ordermap = new LinkedHashMap<>();
        ordermap.put("id",BaseModel.ASC);
        user.setOrderBy(ordermap);
        PageResultCommand<UserEntity> search = baseCRUDManager.search(user);
        PageResultCommand<UserEntity> search3 = baseCRUDManager.search(user, 1, 5, ordermap);
        return "success";
	}
}

```
 **AC.Table支持的共通类BaseCRUDManager的CUDR方法接口文档如下**
 
    /**
      * 根据实体对象的非Null字段作为Where条件查询结果集，如果对象的属性值都为null则返回全部数据等同于selectAll
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return List实体对象列表
      * @version 支持版本1.1.0.RELEASE
      */
     <T> List<T> select(T t);
 
     /**
      * 根据实体对象的@IsKey主键字段的值作为Where条件查询结果，主键字段不能为null
      * @param t 实体对象(只设置主键值即可，其他字段值不会读取)
      * @param <T> 实体对象类型
      * @return 实体对象
      * @version 支持版本1.1.0.RELEASE
      */
     <T> T selectByPrimaryKey(T t);
 
     /**
      * 查询表全部数据
      * @param clasz 实体对象的class
      * @param <T> 实体对象类型
      * @return List实体对象列表
      * @version 支持版本1.1.0.RELEASE
      */
     <T> List<T> selectAll(Class<T> clasz);
 
     /**
      * 根据实体对象的非Null字段作为Where条件查询结果集的Count，如果对象的属性值都为null则Count全表
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return 结果数量
      * @version 支持版本1.1.0.RELEASE
      */
     <T> int selectCount(T t);
 
     /**
      * 根据实体对象的非Null字段作为Where条件查询结果集，如果对象的属性值都为null则返回结果集的第一条使用的limit 1
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return 实体对象
      * @version 支持版本1.1.0.RELEASE
      */
     <T> T selectOne(T t);
 
     /**
      * 根据实体对象的非Null字段作为Where条件进行删除操作，如果对象的属性值都为null则删除表全部数据
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return 返回成功条数
      * @version 支持版本1.1.0.RELEASE
      */
     <T> int delete(T t);
 
     /**
      * 根据实体对象的@IsKey主键字段的值作为Where条件进行删除操作，主键字段不能为null
      * @param t 实体对象(只设置主键值即可，其他字段值不会读取)
      * @param <T> 实体对象类型
      * @return 返回成功条数
      * @version 支持版本1.1.0.RELEASE
      */
     <T> int deleteByPrimaryKey(T t);
 
     /**
      * 根据实体对象的@IsKey主键字段的值作为Where条件查询该数据是否存在，主键字段不能为null
      * @param t 实体对象(只设置主键值即可，其他字段值不会读取)
      * @param <T> 实体对象类型
      * @return true存在，fasle不存在
      * @version 支持版本1.1.0.RELEASE
      */
     <T> boolean existsByPrimaryKey(T t);
 
     /**
      * 根据实体对象保存一条数据，主键如果没有设置自增属性则必须不能为null
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return 实体对象
      * @version 支持版本1.1.0.RELEASE
      */
     <T> T insert(T t);
 
     /**
      * 根据实体对象保存一条数据，如果属性值为null则不插入默认使用数据库的字段默认值，主键如果没有设置自增属性则必须不能为null
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return 实体对象
      * @version 支持版本1.1.0.RELEASE
      */
     <T> T insertSelective(T t);
 
     /**
      * 根据实体对象主键作为Where条件更新其他字段数据，主键必须不能为null
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return 更新结果
      * @version 支持版本1.1.0.RELEASE
      */
     <T> boolean updateByPrimaryKey(T t);
 
     /**
      * 根据实体对象主键作为Where条件更新其他字段数据，如果其他字段属性值为null则忽略更新，主键必须不能为null
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return 更新结果
      * @version 支持版本1.1.0.RELEASE
      */
     <T> boolean updateByPrimaryKeySelective(T t);
 
    /**
     * 直接根据sql查询数据，并根据指定的对象类型转化后返回
     *
     * @param sql 动态sql
     * @param beanClass 返回list对象类型
     * @param <T> 实体对象类型
     * @return list的实体对象类型
     * @version 支持版本1.1.0.RELEASE
     */
    <T> List<T> query(String sql, Class<T> beanClass);

    /**
     * 直接根据sql查询返回数据
     *
     * @param sql 自定义的sql
     * @return list map结构的数据
     * @version 支持版本1.1.0.RELEASE
     */
    List<LinkedHashMap<String, Object>> query(String sql);
     
     /**
      * 根据实体对象的非Null字段作为Where条件查询结果集，如果对象的属性值都为null则返回全部数据等同于selectAll+分页
      *
      * @param t 实体对象
      * @param currentPage 分页参数查询第几页，默认1
      * @param pageSize 分页参数每页显示的条数，默认10
      * @param orderby 分页使用的排序，有序的Map结构{key(要排序的字段名),value(desc/asc)}
      * @param <T> 实体类型
      * @return PageResultCommand分页对象类型
      * @version 支持版本1.1.1.RELEASE
      */
     <T> PageResultCommand<T> search(T t, Integer currentPage, Integer pageSize,LinkedHashMap<String,String> orderby);
 
     /**
      * 根据实体对象的非Null字段作为Where条件查询结果集，如果对象的属性值都为null则返回全部数据等同于selectAll+分页
      * @param t 实体对象
      * @param <T> 实体对象类型
      * @return PageResultCommand分页对象类型
      * @version 支持版本1.1.1.RELEASE
      */
     <T> PageResultCommand<T> search(T t);

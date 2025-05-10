# 对象模型模式

## POJO (Plain Old Java Object) - 简单Java对象

```java
// 最基本的Java对象，不依赖任何框架
public class SimplePojo {
    private String name;
    private Integer age;
    
    // 构造器、getter、setter
}
```

## Query Object - 查询对象

```java
// 用于封装查询条件
public class UserQueryObject {
    private String username;
    private String email;
    private LocalDate registrationDateStart;
    private LocalDate registrationDateEnd;
    private UserStatus status;
    private Boolean isVip;
    
    // 构建查询条件
    public Specification<UserPO> toSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (username != null) {
                predicates.add(cb.like(root.get("username"), "%" + username + "%"));
            }
            if (email != null) {
                predicates.add(cb.equal(root.get("email"), email));
            }
            // ... 其他条件
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
```

## Form Object - 表单对象

```java
// 用于Web表单提交的对象
public class RegistrationForm {
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;
    
    @NotBlank
    private String confirmPassword;
    
    // 表单验证逻辑
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (!password.equals(confirmPassword)) {
            errors.add("Passwords do not match");
        }
        return errors;
    }
}
```

## DTO (Data Transfer Object) - 数据传输对象

```java
// 用于接收客户端请求的数据
public class CreateUserDTO {
    @NotBlank
    private String username;
    
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;
    
    @Email
    private String email;
    
    @Pattern(regexp = "^\\d{11}$")
    private String phoneNumber;
}

// 用于更新的DTO可能只包含部分字段
public class UpdateUserDTO {
    private String email;
    private String phoneNumber;
}
```

应用场景：

- 接口层数据传输
- 不同系统间的数据传输
- 可以包含数据验证注解
- 隐藏敏感字段
- 优化网络传输（只传输必要字段）

## DO (Domain Object) - 领域对象

```java
// 领域模型对象，包含业务逻辑和规则
public class OrderDO {
    private Long id;
    private BigDecimal amount;
    private OrderStatus status;
    private List<OrderItemDO> items;
    
    public void cancel() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }
    
    public void addItem(OrderItemDO item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        recalculateAmount();
    }
    
    private void recalculateAmount() {
        this.amount = items.stream()
            .map(OrderItemDO::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

## AO (Application Object) - 应用对象

```java
// 用于应用层的对象，可能组合多个领域对象
public class OrderProcessAO {
    private OrderDO order;
    private PaymentDO payment;
    private ShipmentDO shipment;
    
    public void processOrder() {
        // 处理订单逻辑
        validateOrder();
        processPayment();
        arrangeShipment();
    }
    
    private void validateOrder() {
        // 验证订单
    }
    
    private void processPayment() {
        // 处理支付
    }
    
    private void arrangeShipment() {
        // 安排发货
    }
}
```

## VO (View Object) - 视图对象

```java
// 返回给前端展示的数据对象
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private UserStatusEnum status;
    
    // 可能包含一些计算属性
    private Integer orderCount;
    private BigDecimal totalSpent;
}

// 列表页面的简化视图
public class UserListVO {
    private Long id;
    private String username;
    private String email;
    private UserStatusEnum status;
}

// 详情页面的详细视图
public class UserDetailVO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private UserStatusEnum status;
    private List<OrderVO> recentOrders;
    private AddressVO defaultAddress;
}
```

应用场景：

- 返回给前端展示的数据
- 可能组合多个实体的数据
- 针对不同视图场景定制数据结构

## DAO (Data Access Object) - 数据访问对象

```java
// 用于数据库访问的对象
public interface UserDAO {
    UserPO findById(Long id);
    List<UserPO> findByUsername(String username);
    void save(UserPO user);
    void delete(Long id);
}

@Repository
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public UserPO findById(Long id) {
        return entityManager.find(UserPO.class, id);
    }
    
    // 其他实现...
}
```

## PO (Persistent Object) - 持久化对象

```java
// 直接与数据库表映射的实体类
@Entity
@Table(name = "users")
public class UserPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isDeleted;
    
    // getters and setters
}
```

应用场景：

- 与数据库表结构一一对应
- 包含数据库相关的字段（如创建时间、更新时间等）
- 通常用在 Repository 层

## BO (Business Object) - 业务对象

```java
// 封装业务逻辑的对象
public class UserBO {
    private UserPO userPO;
    private List<OrderPO> orders;
    private AddressPO defaultAddress;
    
    public boolean isVipUser() {
        return orders.stream()
                .map(OrderPO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .compareTo(new BigDecimal("10000")) > 0;
    }
    
    public boolean canCreateOrder() {
        return !userPO.getIsLocked() && defaultAddress != null;
    }
}
```

应用场景：

- 封装业务逻辑
- 组合多个PO
- 包含业务计算方法

## CO (Command Object) - 命令对象

```java
// 用于封装命令操作的对象
public class CreateOrderCommand {
    private Long userId;
    private List<OrderItemCommand> items;
    private String couponCode;
    private AddressCommand shippingAddress;
    
    @Data
    public static class OrderItemCommand {
        private Long productId;
        private Integer quantity;
    }
    
    @Data
    public static class AddressCommand {
        private String street;
        private String city;
        private String zipCode;
    }
}
```

# 实际应用示例

实际应用示例一：

```java
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    // DTO -> PO -> BO -> VO 的转换过程
    public UserVO createUser(CreateUserDTO createUserDTO) {
        // DTO -> PO
        UserPO userPO = new UserPO();
        userPO.setUsername(createUserDTO.getUsername());
        userPO.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        userPO.setEmail(createUserDTO.getEmail());
        userPO = userRepository.save(userPO);
        
        // PO -> BO
        UserBO userBO = new UserBO();
        userBO.setUserPO(userPO);
        userBO.setOrders(orderRepository.findByUserId(userPO.getId()));
        
        // BO -> VO
        return convertToVO(userBO);
    }
    
    // 列表查询场景
    public Page<UserListVO> listUsers(Pageable pageable) {
        Page<UserPO> userPOs = userRepository.findAll(pageable);
        return userPOs.map(this::convertToListVO);
    }
    
    // 详情查询场景
    public UserDetailVO getUserDetail(Long userId) {
        UserPO userPO = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
            
        UserBO userBO = new UserBO();
        userBO.setUserPO(userPO);
        userBO.setOrders(orderRepository.findByUserId(userId));
        userBO.setDefaultAddress(addressRepository.findDefaultByUserId(userId));
        
        return convertToDetailVO(userBO);
    }
}

@RestController
@RequestMapping("/users")
public class UserController {
    
    @PostMapping
    public UserVO createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }
    
    @GetMapping
    public Page<UserListVO> listUsers(Pageable pageable) {
        return userService.listUsers(pageable);
    }
    
    @GetMapping("/{id}")
    public UserDetailVO getUserDetail(@PathVariable Long id) {
        return userService.getUserDetail(id);
    }
}
```

使用这些对象模型的好处：

1. 职责分离，每种对象有其特定用途
2. 提高安全性，避免敏感数据泄露
3. 优化性能，只传输必要数据
4. 提高代码可维护性
5. 方便接口版本控制和向后兼容

建议：

1. 使用对象映射工具（如 MapStruct）处理对象转换

2. 保持命名一致性

3. 根据实际需求选择合适的对象模型

4. 注意对象之间的转换性能

5. 合理使用继承和组合

实际应用示例二：

```java
@Service
public class OrderService {
    
    @Autowired
    private OrderDAO orderDAO;
    
    public OrderVO processOrder(CreateOrderCommand command) {
        // Command -> DO
        OrderDO orderDO = createOrderDO(command);
        
        // 应用层处理
        OrderProcessAO processAO = new OrderProcessAO(orderDO);
        processAO.processOrder();
        
        // DO -> PO
        OrderPO orderPO = convertToPO(orderDO);
        orderDAO.save(orderPO);
        
        // PO -> VO
        return convertToVO(orderPO);
    }
    
    public Page<OrderVO> searchOrders(OrderQueryObject queryObject, Pageable pageable) {
        Specification<OrderPO> spec = queryObject.toSpecification();
        Page<OrderPO> orders = orderDAO.findAll(spec, pageable);
        return orders.map(this::convertToVO);
    }
}

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @PostMapping
    public OrderVO createOrder(@Valid @RequestBody CreateOrderCommand command) {
        return orderService.processOrder(command);
    }
    
    @GetMapping
    public Page<OrderVO> searchOrders(OrderQueryObject queryObject, Pageable pageable) {
        return orderService.searchOrders(queryObject, pageable);
    }
}
```

这些不同类型的对象各自服务于特定的用途，帮助我们：

1. 更好地组织代码结构
2. 提高代码的可维护性
3. 实现关注点分离
4. 提供更好的代码重用性
5. 简化测试
6. 提高系统的可扩展性

选择使用哪些对象模型取决于：

1. 项目的复杂度
2. 团队的规模和经验
3. 性能要求
4. 维护成本考虑
5. 业务领域的特点

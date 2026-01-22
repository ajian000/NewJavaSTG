# Laser 激光类使用指南

## 激光类体系结构

```
Laser (激光基类)
├── LinearLaser (直线激光)
│   └── EnemyLinearLaser (敌人直线激光)
└── CurvedLaser (曲线激光)
    └── EnemyCurvedLaser (敌人曲线激光)

EnemyLaser (敌方激光基类)
├── EnemyLinearLaser
└── EnemyCurvedLaser
```

## 核心功能

### 1. 激光基类 Laser

所有激光的父类，提供以下功能：
- 基本属性：起点、角度、长度、宽度、颜色
- 预警线系统：可配置预警时间
- 碰撞检测：点到线段的距离检测
- 坐标转换：支持中心原点坐标系

#### 构造函数
```java
// 基本构造函数
Laser laser = new Laser(x, y, angle, length, width, color);

// 完整构造函数
Laser laser = new Laser(x, y, angle, length, width, color, warningTime, damage);
```

#### 主要方法
- `update()` - 更新激光状态
- `render(Graphics g)` - 渲染激光
- `checkCollision(float px, float py)` - 检测碰撞
- `setWarningOnly(boolean)` - 设置是否仅显示预警线

### 2. 直线激光 LinearLaser

直线激光，支持：
- 固定角度发射
- 旋转功能
- 可选的移动功能

#### 使用示例
```java
// 创建静态直线激光
LinearLaser laser = new LinearLaser(x, y, angle, length, width, Color.RED);

// 创建旋转直线激光（每帧旋转0.02弧度）
LinearLaser rotatingLaser = new LinearLaser(x, y, angle, length, width, Color.BLUE,
                                              60, 10, 0.02f);

// 创建移动直线激光
LinearLaser movingLaser = new LinearLaser(x, y, angle, length, width, Color.GREEN);
movingLaser.setMovement(2.0f, Math.PI / 4); // 速度2.0，向45度方向移动

// 设置旋转速度
laser.setRotationSpeed(0.01f);
```

### 3. 曲线激光 CurvedLaser

曲线激光，特点：
- 随时间移动并改变角度
- 可调节长度的拖尾系统
- 每个拖尾段都有独立的碰撞体积
- 渐变透明度效果

#### 构造函数
```java
// 基本构造函数
CurvedLaser curvedLaser = new CurvedLaser(x, y, angle, length, width, color,
                                           vx, vy, maxTrailLength);

// 完整构造函数
CurvedLaser curvedLaser = new CurvedLaser(x, y, angle, length, width, color,
                                           60, 10, vx, vy, maxTrailLength);
```

#### 使用示例
```java
// 创建曲线激光（速度1.5，拖尾长度60帧）
CurvedLaser laser = new CurvedLaser(x, y, Math.PI/2, 200, 5, Color.ORANGE,
                                     1.5f, 0, 60);

// 动态调整速度
laser.setSpeed(2.0f, Math.PI / 3); // 速度2.0，60度方向

// 动态调整拖尾长度
laser.setMaxTrailLength(120);

// 获取当前拖尾长度
int currentTrail = laser.getCurrentTrailLength();
```

### 4. 敌方激光类

#### EnemyLinearLaser
```java
// 创建敌人直线激光
EnemyLinearLaser enemyLaser = new EnemyLinearLaser(x, y, angle, length, width, color);

// 带旋转速度
EnemyLinearLaser rotatingLaser = new EnemyLinearLaser(x, y, angle, length, width, color,
                                                       60, 10, 0.03f);

// 使用与LinearLaser相同的方法
enemyLaser.setMovement(1.0f, Math.PI);
enemyLaser.setRotationSpeed(0.01f);
```

#### EnemyCurvedLaser
```java
// 创建敌人曲线激光
EnemyCurvedLaser enemyLaser = new EnemyCurvedLaser(x, y, angle, length, width, color,
                                                     vx, vy, maxTrailLength);

// 动态控制
enemyLaser.setSpeed(2.0f, Math.PI / 4);
enemyLaser.setMaxTrailLength(100);
enemyLaser.setTrailWidth(8.0f);
```

## 游戏循环中的使用

### 初始化激光列表
```java
private List<Laser> lasers = new ArrayList<>();

// 添加激光
lasers.add(new EnemyLinearLaser(x, y, angle, 300, 4, Color.RED));
```

### 更新激光
```java
public void update() {
    // 更新所有激光
    Iterator<Laser> iterator = lasers.iterator();
    while (iterator.hasNext()) {
        Laser laser = iterator.next();
        laser.update();

        // 移除超出边界的激光
        if (laser.isOutOfBounds(getWidth(), getHeight())) {
            iterator.remove();
        }
    }
}
```

### 渲染激光
```java
public void render(Graphics g) {
    for (Laser laser : lasers) {
        laser.setGameCanvas(this);
        laser.render(g);
    }
}
```

### 碰撞检测
```java
public void checkCollisions(Player player) {
    for (Laser laser : lasers) {
        if (laser.checkCollision(player.getX(), player.getY())) {
            player.takeDamage(laser.getDamage());
        }
    }
}
```

## 预警线功能

### 仅显示预警线
```java
// 创建预警线模式的激光（不会激活造成伤害）
Laser warningLaser = new Laser(x, y, angle, 300, 2, Color.YELLOW);
warningLaser.setWarningOnly(true);
```

### 调整预警时间
```java
// 预警120帧（约2秒）后激活
Laser laser = new Laser(x, y, angle, 300, 4, Color.RED);
laser.setWarningTime(120);
```

## 曲线激光拖尾系统

### 拖尾原理
- CurvedLaser每帧记录当前位置
- 拖尾长度由`maxTrailLength`控制
- 每个拖尾段都有独立的碰撞体积
- 渐变透明度效果（头部不透明，尾部透明）

### 拖尾碰撞检测
```java
// 检测玩家是否与曲线激光的任何部分碰撞
for (Laser laser : curvedLasers) {
    if (laser instanceof CurvedLaser) {
        CurvedLaser curved = (CurvedLaser) laser;
        if (curved.checkCollision(player.getX(), player.getY())) {
            // 碰撞处理
        }
    }
}
```

## 最佳实践

1. **性能优化**
   - 限制激光数量
   - 及时移除超出边界的激光
   - 合理设置拖尾长度（通常30-100帧）

2. **视觉设计**
   - 使用鲜艳颜色区分不同激光
   - 合理设置预警时间（30-120帧）
   - 宽度根据伤害值调整（伤害越高，激光越宽）

3. **碰撞处理**
   - 碰撞检测只在激光激活后进行
   - 考虑激光伤害冷却时间
   - 区分预警线和实际激光的视觉效果

4. **曲线激光使用**
   - 适用于追踪类激光
   - 拖尾长度影响视觉和碰撞范围
   - 可以动态改变速度创造复杂轨迹

## 完整示例

```java
// 在敌人类中使用激光
public class BossEnemy extends Enemy {
    private List<EnemyLaser> lasers = new ArrayList<>();
    private int laserTimer = 0;

    @Override
    public void update() {
        super.update();

        // 每60帧发射一次激光
        laserTimer++;
        if (laserTimer >= 60) {
            laserTimer = 0;

            // 发射旋转直线激光
            float angle = (float)(Math.random() * Math.PI * 2);
            EnemyLinearLaser laser = new EnemyLinearLaser(x, y, angle, 400, 6, Color.RED);
            laser.setRotationSpeed(0.01f); // 旋转
            laser.setWarningTime(90); // 90帧预警
            lasers.add(laser);

            // 发射曲线激光
            float curveAngle = (float)(Math.random() * Math.PI * 2);
            EnemyCurvedLaser curvedLaser = new EnemyCurvedLaser(
                x, y, curveAngle, 300, 4, Color.BLUE,
                2.0f, 0, 80  // 速度2.0，拖尾80帧
            );
            lasers.add(curvedLaser);
        }

        // 更新激光
        for (EnemyLaser laser : lasers) {
            laser.update();
        }

        // 移除超出边界的激光
        lasers.removeIf(l -> l.isOutOfBounds(gameCanvas.getWidth(), gameCanvas.getHeight()));
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        for (EnemyLaser laser : lasers) {
            laser.setGameCanvas(gameCanvas);
            laser.render(g);
        }
    }
}
```

## 注意事项

1. 坐标系统：所有激光使用中心原点坐标系，与游戏其他部分一致
2. 角度单位：使用弧度制，不是角度
3. 碰撞检测：曲线激光的拖尾每段都有碰撞体积
4. 预警功能：warningOnly=true的激光永远不会激活，只能作为预警线使用
5. 性能：过多的曲线激光和长拖尾会影响性能，合理使用

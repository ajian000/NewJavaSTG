# 子机系统文档

## 概述

子机系统参考东方正作设计，为玩家角色提供额外的火力支援。子机会跟随玩家移动，并自动发射子弹攻击敌人。

## 类结构

### 基类：Option

所有子机的基类，位于 `stg.game.player.Option`

**主要功能：**
- 跟随玩家移动（支持延迟跟随）
- 自动发射子弹
- 可配置的射击模式和参数

**核心属性：**
- `x, y`: 子机坐标
- `offsetX, offsetY`: 相对于玩家的偏移位置
- `followSpeed`: 跟随速度（0-1，越小越慢）
- `size`: 子机大小
- `color`: 子机颜色
- `shootInterval`: 射击间隔（帧数）
- `bulletDamage`: 子弹伤害值

**核心方法：**
- `update()`: 更新子机状态（位置、射击）
- `shoot()`: 发射子弹（抽象方法，子类实现）
- `render(Graphics2D g)`: 渲染子机
- `setShooting(boolean)`: 设置射击状态
- `setOffset(float, float)`: 设置相对偏移
- `reset()`: 重置子机状态

### 灵梦子机：ReimuOption

位于 `stg.game.player.ReimuOption`

**特点：**
- 发射追踪弹，自动瞄准最近的敌人
- 粉红色外观，带有红色边框和阴阳图案
- 射击间隔：2帧
- 子弹伤害：1
- 跟随速度：0.2

**构造函数：**
```java
public ReimuOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas)
```

**使用示例：**
```java
ReimuOption option = new ReimuOption(player, -25, 10, canvas);
player.addOption(option);
```

### 魔理沙子机：MarisaOption

位于 `stg.game.player.MarisaOption`

**特点：**
- 发射高威力集中弹
- 定期发射圆形扩散弹幕（增加攻击范围）
- 蓝色外观，带有金色边框和星星图案
- 射击间隔：3帧
- 子弹伤害：2
- 跟随速度：0.18
- 圆形弹幕间隔：15帧

**构造函数：**
```java
public MarisaOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas)
```

**使用示例：**
```java
MarisaOption option = new MarisaOption(player, -20, 15, canvas);
player.addOption(option);
```

## 玩家类集成

### Player 类

`Player` 基类已集成子机系统：

**新增属性：**
- `protected List<Option> options`: 子机列表

**新增方法：**
- `addOption(Option option)`: 添加子机
- `removeOption(Option option)`: 移除子机
- `getOptions()`: 获取子机列表
- `clearOptions()`: 清除所有子机

**自动处理：**
- `update()` 方法会自动更新所有子机
- `render()` 方法会自动渲染所有子机
- `reset()` 方法会自动重置所有子机

### ReimuPlayer 类

灵梦玩家类已集成子机初始化：

**初始化方法：**
```java
public void initializeOptions(GameCanvas canvas)
```

**默认配置：**
- 2个子机，位于玩家左右两侧
- 左子机偏移：(-25, 10)
- 右子机偏移：(25, 10)

### MarisaPlayer 类

魔理沙玩家类已集成子机初始化：

**初始化方法：**
```java
public void initializeOptions(GameCanvas canvas)
```

**默认配置：**
- 2个子机，位于玩家左右两侧
- 左子机偏移：(-20, 15)
- 右子机偏移：(20, 15)

## 使用方法

### 1. 创建自定义子机

继承 `Option` 类并实现 `shoot()` 方法：

```java
public class CustomOption extends Option {
    public CustomOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
        super(player, offsetX, offsetY, gameCanvas);
        // 自定义配置
        setSize(12.0f);
        setColor(new Color(100, 255, 100));
        setShootInterval(2);
        setBulletDamage(2);
        setFollowSpeed(0.15f);
    }

    @Override
    protected void shoot() {
        // 实现自定义射击逻辑
        SimpleBullet bullet = new SimpleBullet(x, y, 0, 50.0f, 4.0f, Color.GREEN);
        bullet.setGameCanvas(gameCanvas);
        bullet.setDamage(bulletDamage);
        gameCanvas.addBullet(bullet);
    }

    @Override
    public void render(Graphics2D g) {
        // 实现自定义渲染逻辑
        super.render(g);
        // 添加额外的视觉效果
    }
}
```

### 2. 在玩家类中使用子机

```java
public class MyPlayer extends Player {
    public MyPlayer(float spawnX, float spawnY) {
        super(spawnX, spawnY);
    }

    public void initializeOptions(GameCanvas canvas) {
        setGameCanvas(canvas);

        // 添加子机
        CustomOption option1 = new CustomOption(this, -30, 10, canvas);
        CustomOption option2 = new CustomOption(this, 30, 10, canvas);
        CustomOption option3 = new CustomOption(this, 0, 20, canvas);

        addOption(option1);
        addOption(option2);
        addOption(option3);
    }
}
```

### 3. 在 GameCanvas 中初始化子机

在 `GameCanvas.setPlayer()` 方法中自动初始化子机：

```java
public void setPlayer(PlayerType type, float spawnX, float spawnY) {
    PlayerFactory factory = PlayerFactory.getInstance();
    this.player = factory.createPlayer(type, spawnX, spawnY);
    this.players.clear();
    this.players.add(this.player);
    this.player.setGameCanvas(this);

    // 初始化子机
    if (player instanceof ReimuPlayer) {
        ((ReimuPlayer) player).initializeOptions(this);
    } else if (player instanceof MarisaPlayer) {
        ((MarisaPlayer) player).initializeOptions(this);
    }

    // 其他初始化...
}
```

## 子机行为模式

### 跟随模式

子机使用平滑跟随算法：

```java
// 更新目标位置
targetX = player.getX() + offsetX;
targetY = player.getY() + offsetY;

// 平滑跟随
float dx = targetX - x;
float dy = targetY - y;
x += dx * followSpeed;
y += dy * followSpeed;
```

**参数说明：**
- `followSpeed = 0.1`: 非常慢的跟随
- `followSpeed = 0.2`: 中等速度（默认）
- `followSpeed = 0.5`: 快速跟随
- `followSpeed = 1.0`: 瞬间跟随

### 射击模式

子机射击与玩家射击同步：

```java
// 在 Player.update() 中
for (Option option : options) {
    option.setShooting(shooting); // 同步射击状态
    option.update(); // 更新子机（包括射击）
}
```

## 设计参考

### 东方正作子机特点

**灵梦（博丽灵梦）：**
- 子机发射追踪弹
- 适合广域攻击
- 子机数量：2-4个
- 子机位置：左右对称

**魔理沙（雾雨魔理沙）：**
- 子机发射高威力集中弹
- 适合Boss战
- 子机数量：2-4个
- 子机位置：左右对称

**其他角色：**
- 咲夜：子机发射激光
- 妖梦：子机发射大威力弹
- 早苗：子机发射扩散弹

## 性能优化

1. **子机数量限制：** 建议不超过4个子机
2. **射击间隔：** 避免过快射击导致性能问题
3. **子弹复用：** 考虑使用对象池管理子弹
4. **渲染优化：** 子机渲染尽量简单

## 扩展功能

### 动态子机数量

可以根据游戏进度动态调整子机数量：

```java
public void updateOptionCount(int powerLevel) {
    clearOptions();
    
    int optionCount = Math.min(powerLevel / 2, 4);
    for (int i = 0; i < optionCount; i++) {
        float offsetX = (i % 2 == 0) ? -25 : 25;
        float offsetY = 10 + (i / 2) * 10;
        
        ReimuOption option = new ReimuOption(this, offsetX, offsetY, gameCanvas);
        addOption(option);
    }
}
```

### 子机升级

可以为子机添加升级系统：

```java
public void upgradeOption(int level) {
    for (Option option : options) {
        option.setBulletDamage(option.getBulletDamage() + level);
        option.setShootInterval(Math.max(1, option.getShootInterval() - level / 2));
    }
}
```

## 注意事项

1. **线程安全：** 子机更新在主游戏循环中进行，无需额外同步
2. **内存管理：** 及时清除不需要的子机
3. **坐标系统：** 子机使用与玩家相同的中心原点坐标系
4. **碰撞检测：** 子机通常不参与碰撞检测（仅用于攻击）

## 总结

子机系统为游戏增加了策略性和火力，参考东方正作的设计，提供了灵活的扩展接口。开发者可以轻松创建自定义子机，实现不同的攻击模式和行为。

# Task线程系统文档

## 概述

为所有游戏物体类（Bullet、Enemy、Laser）添加了独立的`task()`线程机制，使每个物体可以在自己的线程中控制生成后的状态和行为。

## 重要变更（@Time 2026-01-23）

从v2.0开始，`task()`方法改为**抽象方法**，强制所有子类必须实现。这意味着：

1. **基类变为抽象类**：Bullet、Enemy、Laser现在是抽象类，不能直接实例化
2. **必须实现task()**：所有子类必须实现`protected abstract void task()`方法
3. **提供默认实现**：创建了SimpleBullet、SimpleEnemy、SimpleLaser提供空实现供日常使用

## 核心机制

### 1. 自动启动

每个游戏物体在构造时自动启动独立的task线程：
```java
public Bullet(float x, float y, float vx, float vy, float size, Color color) {
    // ... 初始化代码 ...
    startTask(); // 自动启动task线程
}
```

### 2. 线程管理

每个物体维护自己的task线程：
- `taskThread`: 独立的Thread对象
- `taskRunning`: volatile布尔标志，控制线程运行
- 线程命名：`ClassName-Task-timestamp`便于调试

### 3. Task方法（抽象方法）

基类定义**抽象**的`task()`方法，子类必须实现：
```java
protected abstract void task();
```

子类实现示例：
```java
@Override
protected void task() {
    // 在独立线程中执行的逻辑
    while (isTaskRunning()) {
        try {
            // 自定义逻辑
            Thread.sleep(100);
        } catch (InterruptedException e) {
            break;
        }
    }
}
```

## 基类实现

### Bullet类

**新增字段：**
```java
private Thread taskThread;
private volatile boolean taskRunning = false;
```

**新增方法：**
- `startTask()` - 启动task线程（私有，构造函数调用）
- `stopTask()` - 停止task线程，清理资源
- `task()` - **抽象方法**，子类必须实现
- `isTaskRunning()` - 查询task状态

**注意**：Bullet类现在是抽象类，不能直接实例化。对于不需要特殊task行为的子弹，请使用SimpleBullet。

### Enemy类

**新增字段：**
```java
private Thread taskThread;
private volatile boolean taskRunning = false;
```

**新增方法：**
- `startTask()` - 启动task线程（私有，构造函数调用）
- `stopTask()` - 停止task线程，清理资源
- `task()` - **抽象方法**，子类必须实现
- `isTaskRunning()` - 查询task状态

**注意**：Enemy类现在是抽象类，不能直接实例化。对于不需要特殊task行为的敌人，请使用SimpleEnemy。

### Laser类

**新增字段：**
```java
private Thread taskThread;
private volatile boolean taskRunning = false;
```

**新增方法：**
- `startTask()` - 启动task线程（私有，构造函数调用）
- `stopTask()` - 停止task线程，清理资源
- `task()` - **抽象方法**，子类必须实现
- `isTaskRunning()` - 查询task状态

**注意**：Laser类现在是抽象类，不能直接实例化。对于不需要特殊task行为的激光，请使用SimpleLaser。

## 使用方法

### 1. 使用默认实现类（推荐用于简单场景）

如果不需要特殊的独立线程逻辑，使用默认实现类：

```java
// 创建简单子弹
SimpleBullet bullet = new SimpleBullet(x, y, vx, vy, size, color);

// 创建简单敌人
SimpleEnemy enemy = new SimpleEnemy(x, y, vx, vy, size, color, hp, gameCanvas);

// 创建简单激光
SimpleLaser laser = new SimpleLaser(x, y, angle, length, width, color);
```

默认实现类的task()方法为空实现，不会产生额外的线程开销。

### 2. 自定义task()方法（高级用法）

子类继承后**必须实现**`task()`方法：

```java
public class MyBullet extends Bullet {
    @Override
    protected void task() {
        while (isTaskRunning()) {
            try {
                // 自定义逻辑
                Thread.sleep(100); // 控制执行频率
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
```

### 2. 停止task线程

当物体被销毁或移除时，应调用`stopTask()`：
```java
bullet.stopTask(); // 停止task线程
```

### 3. 查询task状态

```java
if (bullet.isTaskRunning()) {
    System.out.println("Task is running");
}
```

## 应用场景

### 场景1：周期性行为

子弹/敌人每N秒执行一次动作：
```java
@Override
protected void task() {
    int count = 0;
    while (isTaskRunning() && isAlive()) {
        Thread.sleep(1000); // 每秒执行
        count++;

        // 每5秒执行一次特殊动作
        if (count % 5 == 0) {
            // 特殊行为
        }
    }
}
```

### 场景2：状态机

敌人/激光在不同状态间切换：
```java
@Override
protected void task() {
    int mode = 0;
    while (isTaskRunning() && isAlive()) {
        Thread.sleep(1000);

        switch (mode) {
            case 0: // 移动模式
                // 10秒后切换
                if (condition) mode = 1;
                break;
            case 1: // 攻击模式
                // 5秒后切换
                if (condition) mode = 2;
                break;
            case 2: // 追踪模式
                // 15秒后重置
                if (condition) mode = 0;
                break;
        }
    }
}
```

### 场景3：自动调整速度/方向

在独立线程中调整运动参数：
```java
@Override
protected void task() {
    while (isTaskRunning()) {
        Thread.sleep(500);

        // 自动加速
        float currentSpeed = getVelocitySpeed();
        if (currentSpeed < maxSpeed) {
            float angle = getVelocityAngle();
            accelerate(0.1f, angle);
        }

        // 自动转向
        if (shouldTurn) {
            turnBy(0.05f);
        }
    }
}
```

### 场景4：AI决策

敌人独立线程中做AI决策：
```java
@Override
protected void task() {
    while (isTaskRunning() && isAlive()) {
        Thread.sleep(1000);

        // 计算与玩家的距离
        float dist = distanceToPlayer();

        // 决策：靠近时攻击
        if (dist < attackRange) {
            attack();
        }

        // 决策：远离时追踪
        if (dist > trackingRange) {
            trackPlayer();
        }
    }
}
```

## 线程安全注意事项

### 1. Volatile标志

使用`volatile`关键字确保taskRunning标志的可见性：
```java
private volatile boolean taskRunning = false;
```

### 2. 循环条件

task线程的循环应该检查`isTaskRunning()`和对象存活状态：
```java
while (isTaskRunning() && isAlive()) {
    // 执行逻辑
}
```

### 3. 优雅停止

`stopTask()`使用`join()`等待线程结束：
```java
public void stopTask() {
    taskRunning = false;
    if (taskThread != null && taskThread.isAlive()) {
        try {
            taskThread.join(100); // 等待最多100ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### 4. 异常处理

task()方法应该正确处理异常：
```java
@Override
protected void task() {
    try {
        while (isTaskRunning()) {
            Thread.sleep(100);
            // 执行逻辑
        }
    } catch (InterruptedException e) {
        System.out.println("Task interrupted");
    } catch (Exception e) {
        System.err.println("Task error: " + e.getMessage());
        e.printStackTrace();
    } finally {
        // 清理资源
    }
}
```

## 默认实现类

### SimpleBullet

简单子弹实现，提供空的task()方法：
```java
public class SimpleBullet extends Bullet {
    public SimpleBullet(float x, float y, float vx, float vy, float size, Color color) {
        super(x, y, vx, vy, size, color);
    }

    @Override
    protected void task() {
        // 空实现，不需要特殊行为
    }
}
```

### SimpleEnemy

简单敌人实现，提供空的task()方法：
```java
public class SimpleEnemy extends Enemy {
    public SimpleEnemy(float x, float y, float vx, float vy, float size, Color color, int hp, GameCanvas gameCanvas) {
        super(x, y, vx, vy, size, color, hp, gameCanvas);
    }

    @Override
    protected void task() {
        // 空实现，不需要特殊行为
    }
}
```

### SimpleLaser

简单激光实现，提供空的task()方法：
```java
public class SimpleLaser extends Laser {
    public SimpleLaser(float x, float y, float angle, float length, float width, Color color) {
        super(x, y, angle, length, width, color);
    }

    @Override
    protected void task() {
        // 空实现，不需要特殊行为
    }
}
```

## 示例类

### TaskDemoBullet

演示子弹的task()方法（必须继承Bullet并实现task）：
- 每500ms执行一次
- 每2秒切换加速/减速
- 每5秒进行转向
- 自动控制速度在1-10范围内

### TaskDemoEnemy

演示敌人的task()方法（必须继承Enemy并实现task）：
- 每1秒执行一次AI决策
- 三种行为模式：移动、停留、追踪
- 模式间自动切换
- 30秒循环一次

## 性能考虑

### 线程数量

- 每个物体一个独立线程
- 大量物体时线程数量可能很高
- 建议：
  - 合理控制物体数量
  - 使用线程池（如需要）
  - 对于简单行为避免使用task()

### 执行频率

合理设置sleep时间：
- 100-1000ms（10-60fps）
- 过高：CPU占用高
- 过低：响应不及时

### 选择默认实现还是自定义实现

**使用Simple*类（默认实现）的场景：**
- 只需要基本的物理运动（update()方法处理）
- 不需要周期性行为
- 不需要AI决策
- 需要控制线程数量（减少线程创建）

**自定义task()的场景：**
- 需要周期性改变行为
- 需要状态机
- 需要AI决策
- 需要自动调整参数
- 需要独立于主循环的逻辑

### 资源清理

确保在以下情况调用`stopTask()`：
- 物体被移除
- 对象被销毁
- 游戏结束

## 最佳实践

1. **轻量级逻辑**：task()中只做必要计算
2. **避免阻塞**：不要在task()中执行耗时操作
3. **状态检查**：始终检查`isTaskRunning()`
4. **异常处理**：正确处理所有异常
5. **资源清理**：对象销毁时调用`stopTask()`
6. **合理频率**：根据需求调整执行频率
7. **调试日志**：添加适量日志便于调试
8. **线程命名**：线程名称包含对象类型和时间戳
9. **优先使用默认实现**：如果不需要特殊逻辑，使用Simple*类减少开销
10. **抽象方法实现**：自定义类必须实现task()方法，否则编译错误

## 与主循环的关系

### 主循环（GameLoop）
- 负责渲染和物理更新
- 调用所有物体的`update()`方法
- 60FPS固定频率

### Task线程
- 负责独立的行为逻辑
- 运行频率由`Thread.sleep()`控制
- 不依赖主循环

### 协作
- task()可以修改对象状态（位置、速度等）
- update()应用这些状态进行物理更新
- render()渲染当前状态

## 总结

Task线程系统为每个游戏物体提供了：
- ✅ 独立的行为控制线程
- ✅ 灵活的周期性逻辑
- ✅ 可扩展的AI决策能力
- ✅ 解耦的行为和渲染/物理

适用于：
- 周期性行为
- 状态机
- AI决策
- 自动调整

# Task系统快速参考

## 一行总结

每个游戏对象在构造时自动启动独立的task()线程，子类可重写task()方法实现自定义行为。

## 快速开始

### 1. 继承基类
```java
public class MyBullet extends Bullet {
    // ...
}

public class MyEnemy extends Enemy {
    // ...
}

public class MyLaser extends LinearLaser {
    // ...
}
```

### 2. 重写task()方法
```java
@Override
protected void task() {
    while (isTaskRunning()) {  // Bullet/Enemy: isTaskRunning()
        try {
            Thread.sleep(1000); // 控制执行频率
            // 自定义逻辑
        } catch (InterruptedException e) {
            break;
        }
    }
}
```

### 3. 清理资源
```java
// 对象销毁时调用
object.stopTask();
```

## 可用的状态查询

### Bullet类
- `isTaskRunning()` - task线程是否运行
- `getX()`, `getY()` - 位置
- `getVelocityX()`, `getVelocityY()` - 速度分量
- `getVelocitySpeed()` - 速度大小
- `getVelocityAngle()` - 速度角度
- `turnBy(angle)` - 旋转速度
- `accelerate(acc, angle)` - 按角度加速
- `accelerateByComponent(ax, ay)` - 按分量加速
- `setVelocity(speed, angle)` - 设置速度（角度模式）
- `setVelocityByComponent(vx, vy)` - 设置速度（分量模式）
- `moveTo(x, y)` - 移动到位置
- `moveOn(dx, dy)` - 增量移动

### Enemy类
- `isTaskRunning()` - task线程是否运行
- `isAlive()` - 是否存活
- `getHp()`, `getMaxHp()` - 生命值
- `getX()`, `getY()` - 位置
- `getSize()` - 大小
- `moveTo(x, y)` - 移动到位置
- `moveOn(dx, dy)` - 增量移动
- `setHp(hp)` - 设置生命值

### Laser类
- `isTaskRunning()` - task线程是否运行
- `isActive()` - 激光是否激活
- `isWarningOnly()` - 是否仅预警
- `getX()`, `getY()` - 起点
- `getAngle()` - 角度
- `getLength()` - 长度
- `getWidth()` - 宽度
- `getDamage()` - 伤害
- 各种setter方法

## 常见模式

### 周期性行为
```java
int count = 0;
while (isTaskRunning()) {
    Thread.sleep(1000);
    count++;
    if (count % 5 == 0) {  // 每5秒
        // 执行动作
    }
}
```

### 状态机
```java
int mode = 0;
while (isTaskRunning()) {
    Thread.sleep(1000);
    switch (mode) {
        case 0: if (condition) mode = 1; break;
        case 1: if (condition) mode = 2; break;
        case 2: if (condition) mode = 0; break;
    }
}
```

### 自动调整（Bullet）
```java
while (isTaskRunning()) {
    Thread.sleep(500);
    float speed = getVelocitySpeed();
    if (speed < 5) {
        float angle = getVelocityAngle();
        accelerate(0.1f, angle);  // 加速
    }
}
```

### 自动调整（Enemy/Laser - 直接修改vx/vy）
```java
while (isTaskRunning() && isAlive()) {
    Thread.sleep(500);
    float speed = (float)Math.sqrt(vx*vx + vy*vy);
    if (speed < 3) {
        vx *= 1.1f;  // 加速10%
        vy *= 1.1f;
    }
}
```

## 注意事项

1. **线程安全**: 循环中检查`isTaskRunning()`
2. **异常处理**: 捕获InterruptedException
3. **执行频率**: 合理设置sleep时间
4. **资源清理**: 对象销毁时调用`stopTask()`
5. **避免阻塞**: task()中不执行耗时操作

## 完整示例

参见：
- `TaskDemoBullet.java` - 子弹task()示例
- `TaskDemoEnemy.java` - 敌人task()示例
- `TASK_SYSTEM.md` - 完整文档

## 与主循环的关系

- **主循环**: 渲染 + 物理更新（60FPS）
- **task线程**: 独立行为逻辑（自定义频率）
- **协作**: task()修改状态 -> update()应用 -> render()渲染

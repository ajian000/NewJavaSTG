# Task机制使用文档

## 1. 概述

Task机制是JavaSTG游戏引擎中的行为管理系统，用于控制游戏对象（如敌人、玩家）的复杂行为序列。通过任务化的方式，开发者可以更灵活地定义和管理游戏对象的行为。

### 核心概念
- **Task**：任务接口，定义了任务的基本方法
- **BaseTask**：基础任务类，实现了Task接口的通用功能
- **TaskManager**：任务管理器，负责管理游戏对象的所有任务
- **组合任务**：如SequenceTask（序列执行）、ParallelTask（并行执行）
- **具体任务**：如MoveTask（移动）、WaitTask（等待）、ShootTask（射击）

## 2. 核心类说明

### 2.1 Task接口

```java
public interface Task {
    boolean update(int deltaTime);
    void start();
    void cancel();
    void reset();
    boolean isCompleted();
    boolean isCancelled();
}
```

- **update**：更新任务状态，返回任务是否完成
- **start**：开始任务
- **cancel**：取消任务
- **reset**：重置任务
- **isCompleted**：检查任务是否完成
- **isCancelled**：检查任务是否已取消

### 2.2 任务类型

| 任务类 | 描述 | 主要参数 |
|-------|------|--------|
| BaseTask | 基础任务类 | - |
| WaitTask | 等待指定帧数 | waitFrames |
| MoveTask | 移动到指定位置 | enemy, targetX, targetY, speed |
| ShootTask | 发射子弹 | enemy, gameCanvas, bulletSpeed, bulletCount |
| SequenceTask | 序列执行多个任务 | - |
| ParallelTask | 并行执行多个任务 | - |

### 2.3 TaskManager

```java
public class TaskManager {
    public void addTask(Task task);
    public void removeTask(Task task);
    public void clearTasks();
    public void update(int deltaTime);
    public int getTaskCount();
    public boolean hasTasks();
}
```

## 3. 使用示例

### 3.1 基本使用

```java
// 创建敌人
BasicEnemy enemy = new BasicEnemy(0, 300, 2, gameCanvas);

// 获取任务管理器
TaskManager taskManager = enemy.getTaskManager();

// 添加移动任务
MoveTask moveTask = new MoveTask(enemy, 200, 300, 2, true);
taskManager.addTask(moveTask);

// 添加等待任务
WaitTask waitTask = new WaitTask(60);
taskManager.addTask(waitTask);

// 添加射击任务
ShootTask shootTask = new ShootTask(enemy, gameCanvas, -10, 3, (float)Math.PI/4, 
        Color.CYAN, 5, 10);
taskManager.addTask(shootTask);
```

### 3.2 序列任务

```java
// 创建序列任务
SequenceTask sequenceTask = new SequenceTask();

// 添加任务序列
sequenceTask.addTask(new MoveTask(enemy, 200, 300, 2, true))
            .addTask(new WaitTask(60))
            .addTask(new MoveTask(enemy, -200, 300, 2, true))
            .addTask(new WaitTask(60))
            .addTask(new ShootTask(enemy, gameCanvas, -10, 3, (float)Math.PI/4, 
                    Color.CYAN, 5, 10));

// 添加到任务管理器
enemy.getTaskManager().addTask(sequenceTask);
```

### 3.3 自定义任务

```java
// 创建自定义任务
BaseTask customTask = new BaseTask() {
    @Override
    public boolean update(int deltaTime) {
        // 自定义任务逻辑
        System.out.println("Custom task executed!");
        return true; // 任务完成
    }
};

enemy.getTaskManager().addTask(customTask);
```

### 3.4 循环任务

```java
// 创建循环任务
SequenceTask loopTask = new SequenceTask();
loopTask.addTask(sequenceTask); // 添加主任务序列
loopTask.addTask(new BaseTask() {
    @Override
    public boolean update(int deltaTime) {
        // 重新执行任务
        if (enemy.getTaskManager() != null) {
            enemy.getTaskManager().clearTasks();
            enemy.getTaskManager().addTask(loopTask);
        }
        return true;
    }
});

enemy.getTaskManager().addTask(loopTask);
```

## 4. 集成到游戏对象

### 4.1 Enemy类集成

Enemy基类已经集成了TaskManager，子类可以通过重写`initTasks()`方法来添加初始任务：

```java
@Override
protected void initTasks() {
    // 创建任务序列
    SequenceTask mainTask = new SequenceTask();
    
    // 添加任务
    mainTask.addTask(new MoveTask(this, 200, getY(), moveSpeed, true))
            .addTask(new WaitTask(60))
            .addTask(new MoveTask(this, -200, getY(), moveSpeed, true))
            .addTask(new WaitTask(60))
            .addTask(new ShootTask(this, gameCanvas, -10, 3, (float)Math.PI/4, 
                    Color.CYAN, 5, 10));
    
    // 添加到任务管理器
    getTaskManager().addTask(mainTask);
}
```

### 4.2 其他游戏对象集成

对于其他游戏对象（如Player），可以参考以下步骤集成TaskManager：

1. 添加TaskManager成员变量
2. 在构造函数中初始化
3. 在update方法中更新任务
4. 提供获取和设置TaskManager的方法

## 5. 最佳实践

### 5.1 任务设计原则

1. **单一职责**：每个任务只负责一项功能
2. **组合优先**：通过组合简单任务来实现复杂行为
3. **可重用性**：设计可重用的任务类
4. **错误处理**：在任务中添加适当的错误处理

### 5.2 性能优化

1. **任务清理**：及时移除已完成或已取消的任务
2. **任务重用**：对于重复使用的任务，考虑重置而非创建新实例
3. **批量操作**：使用TaskManager的addTask和removeTask方法，避免在update方法中直接修改任务列表

### 5.3 调试技巧

1. **日志输出**：在任务的关键方法中添加日志输出
2. **任务状态检查**：定期检查任务的完成状态
3. **可视化调试**：考虑添加任务执行状态的可视化显示

## 6. 常见问题

### 6.1 任务不执行

**可能原因**：
- 任务未添加到TaskManager
- TaskManager的update方法未被调用
- 任务已被取消或完成

**解决方案**：
- 确保任务已添加到TaskManager
- 确保游戏对象的update方法调用了TaskManager的update方法
- 检查任务的状态和生命周期

### 6.2 任务执行异常

**可能原因**：
- 任务中的空指针异常
- 任务参数设置错误
- 任务执行顺序问题

**解决方案**：
- 在任务中添加适当的空指针检查
- 确保任务参数设置正确
- 检查任务的执行顺序和依赖关系

### 6.3 性能问题

**可能原因**：
- 任务数量过多
- 任务执行逻辑复杂
- 任务创建和销毁频繁

**解决方案**：
- 减少不必要的任务
- 优化任务执行逻辑
- 考虑任务重用而非频繁创建

## 7. 扩展指南

### 7.1 创建自定义任务

要创建自定义任务，只需继承BaseTask并实现update方法：

```java
public class CustomTask extends BaseTask {
    @Override
    public boolean update(int deltaTime) {
        // 自定义任务逻辑
        return true; // 任务完成
    }
}
```

### 7.2 扩展TaskManager

如果需要扩展TaskManager的功能，可以继承TaskManager类并添加新的方法：

```java
public class AdvancedTaskManager extends TaskManager {
    // 添加新功能
}
```

## 8. 示例场景

### 8.1 敌人巡逻模式

```java
// 创建巡逻任务
SequenceTask patrolTask = new SequenceTask();
patrolTask.addTask(new MoveTask(enemy, 200, 300, 2, true))
           .addTask(new WaitTask(120))
           .addTask(new MoveTask(enemy, -200, 300, 2, true))
           .addTask(new WaitTask(120))
           .addTask(new BaseTask() {
               @Override
               public boolean update(int deltaTime) {
                   // 重新开始巡逻
                   enemy.getTaskManager().clearTasks();
                   enemy.getTaskManager().addTask(patrolTask);
                   return true;
               }
           });

enemy.getTaskManager().addTask(patrolTask);
```

### 8.2 敌人攻击模式

```java
// 创建攻击任务
SequenceTask attackTask = new SequenceTask();
attackTask.addTask(new MoveTask(enemy, 0, 200, 3, true))
           .addTask(new WaitTask(30))
           .addTask(new ShootTask(enemy, gameCanvas, -10, 5, (float)Math.PI/2, 
                   Color.RED, 6, 15))
           .addTask(new WaitTask(60))
           .addTask(new MoveTask(enemy, 0, 400, 2, true));

enemy.getTaskManager().addTask(attackTask);
```

## 9. 总结

Task机制为JavaSTG游戏引擎提供了灵活的行为管理系统，通过任务化的方式，开发者可以更清晰、更灵活地定义游戏对象的行为。合理使用Task机制，可以大大提高游戏开发的效率和代码的可维护性。

---

**版本**：1.0
**更新日期**：2026-01-29
**适用引擎**：JavaSTG v1.0+

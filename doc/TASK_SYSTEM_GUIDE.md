# Task系统使用指南

## 概述

Task系统是NewJavaSTG游戏引擎的核心机制之一，用于控制游戏对象（特别是敌人）的行为。通过组合不同的Task，可以创建复杂的移动和攻击模式。

## 核心Task类型

### 1. BaseTask（基础任务）

所有Task的基类，提供任务生命周期管理：
- `start()` - 开始任务
- `update(deltaTime)` - 更新任务状态
- `cancel()` - 取消任务
- `reset()` - 重置任务
- `isCompleted()` - 检查是否完成
- `isCancelled()` - 检查是否已取消

### 2. MoveTask（移动任务）

控制游戏对象移动到指定位置。

```java
// 构造函数
MoveTask(Enemy enemy, float targetX, float targetY, float speed, boolean smooth)
```

**参数说明：**
- `enemy`: 要移动的敌人对象
- `targetX`, `targetY`: 目标坐标
- `speed`: 移动速度
- `smooth`: 是否平滑移动

**示例：**
```java
// 移动到(100, 200)，速度为2.0
MoveTask move = new MoveTask(enemy, 100, 200, 2.0f, true);
```

### 3. ShootTask（射击任务）

让敌人发射子弹。

```java
// 简化构造函数
ShootTask(Enemy enemy, GameCanvas gameCanvas, float bulletSpeed)

// 完整构造函数
ShootTask(Enemy enemy, GameCanvas gameCanvas, float bulletSpeed, int bulletCount,
          float angleSpread, Color bulletColor, float bulletSize, int bulletDamage)
```

**参数说明：**
- `enemy`: 发射子弹的敌人
- `gameCanvas`: 游戏画布
- `bulletSpeed`: 子弹速度
- `bulletCount`: 子弹数量
- `angleSpread`: 角度扩散范围（弧度）
- `bulletColor`: 子弹颜色
- `bulletSize`: 子弹大小
- `bulletDamage`: 子弹伤害

**示例：**
```java
// 发射单发子弹
ShootTask shoot = new ShootTask(enemy, gameCanvas, 10.0f);

// 发射扇形弹（5发，扩散角度0.5弧度）
ShootTask spread = new ShootTask(enemy, gameCanvas, 8.0f, 5, 0.5f, Color.RED, 6, 10);
```

### 4. WaitTask（等待任务）

等待指定的帧数。

```java
WaitTask(int waitFrames)
```

**示例：**
```java
// 等待60帧（约1秒）
WaitTask wait = new WaitTask(60);
```

### 5. SequenceTask（序列任务）

按顺序执行多个任务，只有前一个任务完成后才开始下一个。

```java
SequenceTask seq = new SequenceTask();
seq.addTask(task1);
seq.addTask(task2);
seq.addTask(task3);
```

**示例：**
```java
SequenceTask sequence = new SequenceTask();
sequence.addTask(new MoveTask(enemy, 100, 200, 2.0f, true));
sequence.addTask(new WaitTask(60));
sequence.addTask(new ShootTask(enemy, gameCanvas, 8.0f));
```

### 6. ParallelTask（并行任务）

同时执行多个任务，所有任务都完成才算完成。

```java
ParallelTask parallel = new ParallelTask();
parallel.addTask(task1);
parallel.addTask(task2);
```

**示例：**
```java
ParallelTask parallel = new ParallelTask();

// 同时移动和射击
parallel.addTask(new MoveTask(enemy, 150, 200, 2.0f, true));
parallel.addTask(new BaseTask() {
    private int frame = 0;
    @Override
    public boolean update(int deltaTime) {
        frame += deltaTime;
        if (frame >= 30) {
            // 发射子弹
            frame = 0;
        }
        return false; // 不完成，持续执行
    }
});
```

## 自定义Task

通过继承BaseTask，可以创建自定义任务。

### 示例1：循环移动任务

```java
Task circleMoveTask = new BaseTask() {
    private int frame = 0;
    private float centerX = 100;
    private float centerY = 200;
    private float radius = 80;

    @Override
    public boolean update(int deltaTime) {
        frame += deltaTime;
        float angle = (float)(frame * 0.05);
        float newX = centerX + (float)(Math.cos(angle) * radius);
        float newY = centerY + (float)(Math.sin(angle) * radius);
        enemy.setPosition(newX, newY);
        return false; // 不完成，持续执行
    }
};
```

### 示例2：定时射击任务

```java
Task periodicShootTask = new BaseTask() {
    private int frame = 0;
    private int interval = 60; // 每秒射击一次

    @Override
    public boolean update(int deltaTime) {
        frame += deltaTime;
        if (frame >= interval) {
            // 发射子弹
            shootBullet();
            frame = 0;
        }
        return false; // 持续执行
    }
};
```

### 示例3：螺旋移动任务

```java
Task spiralMoveTask = new BaseTask() {
    private int frame = 0;
    private float radius = 150;

    @Override
    public boolean update(int deltaTime) {
        frame += deltaTime;
        float angle = (float)(frame * 0.03);
        radius -= 0.1f * deltaTime;

        if (radius < 20) {
            radius = 150; // 重置半径
        }

        float newX = (float)(Math.cos(angle) * radius);
        float newY = (float)(Math.sin(angle) * radius) + 100;
        enemy.setPosition(newX, newY);
        return false;
    }
};
```

## 在Enemy中使用Task

### 初始化Task

重写`initTasks()`方法：

```java
@Override
protected void initTasks() {
    // 创建序列任务
    SequenceTask mainTask = new SequenceTask();

    // 添加移动任务
    mainTask.addTask(new MoveTask(this, 200, getY(), 2.0f, true));
    mainTask.addTask(new WaitTask(60));

    // 添加射击任务
    mainTask.addTask(new ShootTask(this, gameCanvas, 10.0f));

    // 添加到任务管理器
    getTaskManager().addTask(mainTask);
}
```

### 复杂组合示例

```java
@Override
protected void initTasks() {
    // 第一阶段：入场
    SequenceTask phase1 = new SequenceTask();
    phase1.addTask(new MoveTask(this, 0, 150, 3.0f, true));
    phase1.addTask(new WaitTask(30));

    // 第二阶段：组合攻击
    ParallelTask phase2 = new ParallelTask();

    // 左右移动
    Task moveTask = new BaseTask() {
        private int frame = 0;
        @Override
        public boolean update(int deltaTime) {
            frame += deltaTime;
            float x = (float)(Math.sin(frame * 0.05) * 150);
            setPosition(x, 150);
            return false;
        }
    };

    // 连续射击
    Task shootTask = new BaseTask() {
        private int frame = 0;
        @Override
        public boolean update(int deltaTime) {
            frame += deltaTime;
            if (frame >= 30) {
                shootSpread(5, 0.3f, Color.RED);
                frame = 0;
            }
            return false;
        }
    };

    phase2.addTask(moveTask);
    phase2.addTask(shootTask);

    // 组合所有阶段
    SequenceTask allPhases = new SequenceTask();
    allPhases.addTask(phase1);
    allPhases.addTask(phase2);

    getTaskManager().addTask(allPhases);
}
```

## TaskDemoEnemy行为模式

TaskDemoEnemy提供了5种预定义的行为模式：

### 1. sine（正弦波）
左右移动并发射扇形弹
- 移动轨迹：左→右→中心循环
- 攻击方式：扇形散开子弹
- 适用场景：基础敌人

### 2. circle（圆形）
绕圈移动并向四周发射子弹
- 移动轨迹：圆形
- 攻击方式：一圈子弹（12发）
- 适用场景：中等难度

### 3. spiral（螺旋）
从外向内螺旋移动并发射螺旋弹
- 移动轨迹：螺旋收缩
- 攻击方式：连续螺旋弹
- 适用场景：高难度

### 4. figure8（8字形）
走8字并发射跟踪弹
- 移动轨迹：8字形
- 攻击方式：跟踪玩家子弹
- 适用场景：高难度

### 5. complex（复杂）
结合多种任务类型的组合行为
- 第一阶段：入场
- 第二阶段：扇形弹→圆形弹→螺旋弹
- 第三阶段：左右移动+连续射击
- 适用场景：Boss或精英敌人

## 在关卡中使用TaskDemoEnemy

### JSON配置

```json
{
  "type": "TaskDemoEnemy",
  "x": 0,
  "y": 200,
  "speed": 2.0,
  "pattern": "sine",
  "frame": 60
}
```

**参数说明：**
- `type`: 必须是"TaskDemoEnemy"
- `x`, `y`: 初始位置
- `speed`: 移动速度参数
- `pattern`: 行为模式（sine/circle/spiral/figure8/complex）
- `frame`: 生成帧数

### 完整关卡示例

参见 `src/user/level_task_demo.json`

这个演示关卡包含：
- 6个波次
- 20个敌人
- 5种不同的行为模式
- 约1.5分钟的游戏时间

## Task系统的优势

1. **模块化**：每个Task都是独立的，可复用
2. **可组合**：通过SequenceTask和ParallelTask组合复杂行为
3. **易扩展**：轻松添加自定义Task
4. **易维护**：行为逻辑清晰，易于调试
5. **高性能**：任务管理器自动处理任务生命周期

## 最佳实践

1. **合理使用并行任务**：将独立的行为放在ParallelTask中
2. **避免任务嵌套过深**：保持层级清晰
3. **及时重置任务**：使用reset()实现循环行为
4. **利用自定义Task**：对于复杂行为，创建专用Task
5. **测试行为**：使用简单关卡测试新的行为模式

## 进阶技巧

### 创建循环行为

```java
// 方法1：使用自定义Task
Task loopTask = new BaseTask() {
    @Override
    public boolean update(int deltaTime) {
        // 执行行为
        doSomething();
        return false; // 持续执行
    }
};

// 方法2：重置任务
Task loopTask = new BaseTask() {
    @Override
    public boolean update(int deltaTime) {
        doSomething();
        complete(); // 标记完成
        return true;
    }
    
    @Override
    protected void onComplete() {
        reset(); // 重置自身
    }
};
```

### 动态调整参数

```java
Task adaptiveTask = new BaseTask() {
    @Override
    public boolean update(int deltaTime) {
        float healthPercent = (float)enemy.getHp() / enemy.getMaxHp();
        float speed = baseSpeed * (1.5f - healthPercent); // 血量越低速度越快
        // 使用调整后的速度
        return false;
    }
};
```

### 条件触发

```java
Task conditionalTask = new BaseTask() {
    @Override
    public boolean update(int deltaTime) {
        if (someCondition()) {
            // 执行特定行为
            executeSpecialMove();
        }
        return false;
    }
};
```

## 总结

Task系统为NewJavaSTG提供了强大而灵活的行为控制机制。通过组合不同的Task，可以轻松创建各种复杂的敌人和弹幕模式。关键在于：
1. 理解各种Task的特性
2. 合理组合SequenceTask和ParallelTask
3. 善用自定义Task
4. 多测试和调试

继续探索Task系统的无限可能！

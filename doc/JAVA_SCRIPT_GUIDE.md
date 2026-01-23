# Java脚本系统使用指南

## 概述

Java脚本系统允许你直接使用Java编写关卡脚本，无需依赖外部脚本语言。这提供了更好的类型安全、IDE支持和性能。

## 优势

### 相比其他脚本语言
1. **类型安全**：编译时检查，避免运行时错误
2. **IDE支持**：自动补全、重构、导航
3. **性能更好**：直接运行，无需解释器
4. **调试方便**：可以断点调试
5. **代码复用**：可以利用现有的Java代码库

## 快速开始

### 1. 创建关卡脚本类

实现`LevelScript`接口：

```java
package user.level;

import stg.util.script.LevelScript;
import stg.util.LevelData;

public class MyLevel implements LevelScript {
    @Override
    public String getLevelName() {
        return "My First Level";
    }

    @Override
    public void init(LevelData levelData) {
        // 初始化关卡
        System.out.println("Initializing level");
    }

    @Override
    public void loadEnemies(LevelData levelData) {
        // 添加敌人
        addBasicEnemy(levelData, 0, 300, 0, 0, 20, 100,
                     new int[]{0, 0, 255}, 60);
    }

    @Override
    public void loadEvents(LevelData levelData) {
        // 添加事件（可选）
    }
}
```

### 2. 使用关卡脚本

#### 方式1：通过LevelManager加载

```java
// 获取LevelManager实例
LevelManager levelManager = LevelManager.getInstance();

// 方法1：直接加载Java类
LevelData level = levelManager.loadJavaLevel("main", user.level.MyLevel.class);

// 方法2：通过类名字符串加载
LevelData level2 = levelManager.loadJavaLevel("level2", "user.level.MyLevel2");
```

#### 方式2：设置为默认加载方式

```java
// 设置为Java加载器（默认已经是Java）
LevelManager.getInstance().setScriptLanguage("java");

// 从user目录加载（会自动查找JavaLevelDemo）
LevelData level = LevelManager.getInstance().loadLevelFromUser();
```

## LevelScript接口详解

### 必须实现的方法

#### getLevelName()
返回关卡名称：
```java
@Override
public String getLevelName() {
    return "Level 1: Easy Start";
}
```

#### init(LevelData levelData)
关卡初始化，在加载敌人之前调用：
```java
@Override
public void init(LevelData levelData) {
    // 设置关卡元数据（如果需要）
    // 初始化一些变量
    System.out.println("Level initialized");
}
```

#### loadEnemies(LevelData levelData)
加载敌人数据，**必须**在此方法中添加敌人：
```java
@Override
public void loadEnemies(LevelData levelData) {
    // 添加敌人
    addBasicEnemy(levelData, 0, 300, 0, 0, 20, 100,
                 new int[]{0, 0, 255}, 60);
}
```

### 可选实现的方法

#### loadEvents(LevelData levelData)
加载事件数据（默认为空实现）：
```java
@Override
public void loadEvents(LevelData levelData) {
    // 添加事件
    Map<String, Object> event = new HashMap<>();
    event.put("type", "text");
    event.put("message", "Boss coming!");
    event.put("frame", 1000);
    levelData.addEvent(event);
}
```

## 辅助方法

LevelScript接口提供了多个辅助方法，方便添加敌人。

### addBasicEnemy()

添加基础敌人：
```java
EnemySpawnData enemy = addBasicEnemy(
    levelData,              // LevelData对象
    x, y,                 // 位置坐标
    vx, vy,                // X/Y方向速度
    size,                   // 敌人大小
    hp,                     // 生命值
    new int[]{R, G, B},     // 颜色（RGB数组）
    frame                   // 生成帧数
);
```

### addLaserShootingEnemy()

添加激光射击敌人：
```java
EnemySpawnData enemy = addLaserShootingEnemy(
    levelData,              // LevelData对象
    x, y,                 // 位置坐标
    speed,                  // 移动速度
    hp,                     // 生命值
    frame,                  // 生成帧数
    pattern                 // 攻击模式：0=直线激光, 1=曲线激光, 2=混合
);
```

### addCustomEnemy()

添加自定义敌人：
```java
EnemySpawnData enemy = addCustomEnemy(
    levelData,              // LevelData对象
    "MyCustomEnemy",       // 敌人类型（类名）
    x, y,                 // 位置坐标
    frame                   // 生成帧数
);
```

## 完整示例

### 示例1：简单关卡

```java
package user.level;

import stg.util.script.LevelScript;
import stg.util.LevelData;

public class SimpleLevel implements LevelScript {
    @Override
    public String getLevelName() {
        return "Simple Level";
    }

    @Override
    public void init(LevelData levelData) {
        System.out.println("Starting simple level");
    }

    @Override
    public void loadEnemies(LevelData levelData) {
        // 第一波：3个敌人
        addBasicEnemy(levelData, -100, 300, 2, 0, 20, 100,
                     new int[]{0, 0, 255}, 60);
        addBasicEnemy(levelData, 0, 300, 0, 0, 20, 100,
                     new int[]{0, 0, 255}, 90);
        addBasicEnemy(levelData, 100, 300, -2, 0, 20, 100,
                     new int[]{0, 0, 255}, 120);
    }
}
```

### 示例2：多波次关卡

```java
package user.level;

import stg.util.script.LevelScript;
import stg.util.LevelData;

public class MultiWaveLevel implements LevelScript {
    @Override
    public String getLevelName() {
        return "Multi Wave Level";
    }

    @Override
    public void init(LevelData levelData) {
    }

    @Override
    public void loadEnemies(LevelData levelData) {
        // Wave 1: 基础敌人
        for (int i = 0; i < 5; i++) {
            float x = -200 + i * 100;
            addBasicEnemy(levelData, x, 280, 0, 0, 18, 80,
                         new int[]{255, 100, 100}, 60 + i * 10);
        }

        // Wave 2: 激光射击敌人
        addLaserShootingEnemy(levelData, -100, 250, 1.5f, 200, 300, 0);
        addLaserShootingEnemy(levelData, 100, 250, 1.5f, 200, 320, 0);

        // Wave 3: 混合攻击
        for (int i = 0; i < 8; i++) {
            float x = -250 + i * 70;
            float vx = (float)(-1 + Math.random() * 2);
            addBasicEnemy(levelData, x, 220, vx, 0, 15, 60,
                         new int[]{255, 255, 0}, 500 + i * 10);
        }
    }
}
```

## 高级用法

### 自定义参数

添加自定义敌人后，可以额外设置参数：

```java
EnemySpawnData spawn = addCustomEnemy(levelData, "CustomEnemy", 0, 300, 60);
spawn.addParam("customParam", 123);
spawn.addParam("speed", 5.0f);
```

### 事件系统

事件可以用于触发关卡中的特定时刻：

```java
@Override
public void loadEvents(LevelData levelData) {
    // 100帧时显示文字
    Map<String, Object> event1 = new HashMap<>();
    event1.put("type", "text");
    event1.put("message", "Start!");
    event1.put("frame", 100);
    levelData.addEvent(event1);

    // 1000帧时警告
    Map<String, Object> event2 = new HashMap<>();
    event2.put("type", "text");
    event2.put("message", "Boss incoming!");
    event2.put("frame", 1000);
    levelData.addEvent(event2);
}
```

## 编译和运行

### 1. 编译Java脚本

将脚本类放到`src/user/level/`目录下，然后编译：

```bash
javac -encoding UTF-8 -d bin -cp "src" src/user/level/*.java
```

### 2. 打包JAR

```bash
jar -cef Main.Main NewJavaSTG.jar -C bin .
```

### 3. 运行游戏

```bash
java -jar NewJavaSTG.jar
```

## 文件组织

```
src/
├── user/
│   └── level/              # Java脚本目录
│       ├── MyLevel1.java
│       ├── MyLevel2.java
│       └── BossLevel.java
├── level.js               # JavaScript脚本（可选）
├── level.py              # Python脚本（可选）
└── level.json            # JSON脚本（可选）
```

## 切换脚本语言

```java
// 使用Java脚本（默认）
LevelManager.getInstance().setScriptLanguage("java");

// 使用JavaScript脚本
LevelManager.getInstance().setScriptLanguage("javascript");

// 使用Python脚本
LevelManager.getInstance().setScriptLanguage("python");

// 使用JSON脚本
LevelManager.getInstance().setScriptLanguage("json");
```

## 常见问题

### Q: Java脚本文件放在哪里？
A: 放在`src/user/level/`目录下，包名为`user.level`。

### Q: 如何添加自定义敌人类型？
A: 继承Enemy类，然后使用`addCustomEnemy()`。

### Q: 如何在脚本中使用游戏API？
A: 脚本主要定义关卡数据（敌人、事件），游戏逻辑在游戏引擎中处理。

### Q: 可以使用现有的Java类吗？
A: 可以！可以直接使用项目中的任何Java类，包括util包中的工具类。

## 总结

Java脚本系统提供了：
- ✅ 类型安全的关卡脚本
- ✅ 优秀的IDE支持
- ✅ 更好的性能
- ✅ 方便的调试
- ✅ 代码复用能力

推荐使用Java脚本编写关卡！

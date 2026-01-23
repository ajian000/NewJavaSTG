# Java脚本快速参考

## 基本结构

```java
package user.level;

import stg.util.script.LevelScript;
import stg.util.LevelData;

public class MyLevel implements LevelScript {

    @Override
    public String getLevelName() {
        return "Level Name";
    }

    @Override
    public void init(LevelData levelData) {
        // 初始化
    }

    @Override
    public void loadEnemies(LevelData levelData) {
        // 添加敌人
    }

    @Override
    public void loadEvents(LevelData levelData) {
        // 添加事件（可选）
    }
}
```

## 添加敌人

### 基础敌人
```java
addBasicEnemy(levelData, x, y, vx, vy, size, hp, color[], frame);
```

### 激光射击敌人
```java
addLaserShootingEnemy(levelData, x, y, speed, hp, frame, pattern);
// pattern: 0=直线激光, 1=曲线激光, 2=混合
```

### 自定义敌人
```java
addCustomEnemy(levelData, "EnemyClassName", x, y, speed, frame);
```

## 参数说明

### addBasicEnemy参数
- `levelData`: LevelData对象
- `x, y`: 位置（中心原点坐标系）
- `vx, vy`: X/Y方向速度
- `size`: 敌人大小
- `hp`: 生命值
- `color[]`: RGB颜色数组 [R, G, B]
- `frame`: 生成帧数

### addLaserShootingEnemy参数
- `levelData`: LevelData对象
- `x, y`: 位置
- `speed`: 移动速度
- `hp`: 生命值
- `frame`: 生成帧数
- `pattern`: 攻击模式

### 颜色示例
```java
new int[]{255, 0, 0}    // 红色
new int[]{0, 255, 0}    // 绿色
new int[]{0, 0, 255}    // 蓝色
new int[]{255, 255, 0}  // 黄色
new int[]{255, 0, 255}  // 紫色
new int[]{255, 255, 255} // 白色
```

## 使用方式

### 在代码中加载
```java
import stg.util.LevelManager;

// 方法1：使用类对象
LevelManager.getInstance().loadJavaLevel("level1", user.level.MyLevel.class);

// 方法2：使用类名字符串
LevelManager.getInstance().loadJavaLevel("level1", "user.level.MyLevel");
```

### 设置为默认
```java
LevelManager.getInstance().setScriptLanguage("java");
```

## 示例：简单关卡

```java
package user.level;

import stg.util.script.LevelScript;
import stg.util.LevelData;

public class DemoLevel implements LevelScript {

    @Override
    public String getLevelName() {
        return "Demo Level";
    }

    @Override
    public void init(LevelData levelData) {
        System.out.println("Level starting...");
    }

    @Override
    public void loadEnemies(LevelData levelData) {
        // Wave 1: 3个敌人
        addBasicEnemy(levelData, -100, 300, 2, 0, 20, 100,
                     new int[]{0, 0, 255}, 60);
        addBasicEnemy(levelData, 0, 300, 0, 0, 20, 100,
                     new int[]{0, 0, 255}, 90);
        addBasicEnemy(levelData, 100, 300, -2, 0, 20, 100,
                     new int[]{0, 0, 255}, 120);

        // Wave 2: 激光射击敌人
        addLaserShootingEnemy(levelData, 0, 250, 1.5f, 200, 180, 0);
    }
}
```

## 编译和运行

```bash
# 编译脚本
javac -encoding UTF-8 -d bin -cp "src" src/user/level/*.java

# 编译整个项目
javac -encoding UTF-8 -d bin -cp "src" src/**/*.java

# 打包JAR
jar -cef Main.Main NewJavaSTG.jar -C bin .

# 运行
java -jar NewJavaSTG.jar
```

## 文件位置

```
src/user/level/
├── MyLevel1.java
├── MyLevel2.java
└── ...
```

## 优势

- ✅ 类型安全
- ✅ IDE自动补全
- ✅ 编译时检查
- ✅ 更好的性能
- ✅ 方便调试

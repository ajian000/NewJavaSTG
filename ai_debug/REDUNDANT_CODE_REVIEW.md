# Java STG 项目冗余代码审查报告
**已处理**
**审查日期**: 2026-01-30  
**审查范围**: e:\Myproject\Game\JavaSTG\src  
**审查类型**: 冗余代码识别与优化建议

---

## 执行摘要

本次审查对 Java STG（弹幕射击游戏）项目进行了全面的代码质量分析，发现了多个层面的冗余代码和设计问题。主要问题包括：

- **严重问题**: 3项（坐标转换重复、抗锯齿设置重复、无效变量引用）
- **中等问题**: 3项（未使用的抽象方法、道具吸引逻辑重复、未使用的示例类）
- **轻微问题**: 3项（硬编码常量、构造函数重载过多、渲染逻辑重复）

**代码质量评分**:
- 代码重复度: 高
- 可维护性: 中等
- 可扩展性: 良好
- 性能: 良好

---

## 一、严重冗余问题（必须修复）

### 1.1 坐标转换代码严重重复

**严重程度**: 🔴 严重  
**影响范围**: 8个文件  
**重复次数**: 8次

#### 问题描述

在多个文件中重复实现相同的坐标转换逻辑，将游戏坐标转换为屏幕坐标。这些代码几乎完全相同，违反了 DRY（Don't Repeat Yourself）原则。

#### 影响文件

| 文件 | 行号 | 代码片段 |
|------|------|----------|
| [Obj.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\obj\Obj.java#L96-L101) | 96-101 | 坐标转换逻辑 |
| [Enemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\Enemy.java#L69-L73) | 69-73 | 坐标转换逻辑 |
| [Player.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\Player.java#L184-L185) | 184-185 | 坐标转换逻辑 |
| [Item.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\Item.java#L105-L109) | 105-109 | 坐标转换逻辑 |
| [PowerUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\PowerUp.java#L56-L60) | 56-60 | 坐标转换逻辑 |
| [LifeUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\LifeUp.java#L77-L81) | 77-81 | 坐标转换逻辑 |
| [ScorePoint.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\ScorePoint.java#L117-L121) | 117-121 | 坐标转换逻辑 |
| [Laser.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\laser\Laser.java#L161-L166) | 161-166 | 坐标转换逻辑 |

#### 冗余代码模式

```java
float screenX = x;
float screenY = y;

if (gameCanvas != null) {
    float[] coords = gameCanvas.getCoordinateSystem().toScreenCoords(x, y);
    screenX = coords[0];
    screenY = coords[1];
} else {
    screenX = x + 548 / 2.0f;
    screenY = 921 / 2.0f - y;
}
```

#### 优化建议

**方案1**: 在 `Obj.java` 基类中添加坐标转换方法

```java
protected float[] toScreenCoords(float worldX, float worldY) {
    if (gameCanvas != null) {
        return gameCanvas.getCoordinateSystem().toScreenCoords(worldX, worldY);
    }
    return new float[]{
        worldX + DEFAULT_CANVAS_WIDTH / 2.0f,
        DEFAULT_CANVAS_HEIGHT / 2.0f - worldY
    };
}

protected static final float DEFAULT_CANVAS_WIDTH = 548;
protected static final float DEFAULT_CANVAS_HEIGHT = 921;
```

**方案2**: 在 `CoordinateSystem` 中提供单例访问

```java
public class CoordinateSystem {
    private static CoordinateSystem defaultInstance;
    
    public static CoordinateSystem getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new CoordinateSystem(548, 921);
        }
        return defaultInstance;
    }
}
```

#### 预期收益

- 减少约 40 行重复代码
- 提高代码可维护性
- 统一坐标转换逻辑，降低错误风险
- 便于未来修改画布尺寸

---

### 1.2 抗锯齿设置重复

**严重程度**: 🔴 严重  
**影响范围**: 17个文件  
**重复次数**: 17次

#### 问题描述

在 17 个不同的文件中重复设置相同的抗锯齿渲染提示。这不仅造成代码冗余，还增加了维护成本。

#### 影响文件

| 文件 | 行号 |
|------|------|
| [Player.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\Player.java#L187) | 187 |
| [MarisaPlayer.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\MarisaPlayer.java#L57) | 57 |
| [ReimuPlayer.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\ReimuPlayer.java#L57) | 57 |
| [Option.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\Option.java#L91) | 91 |
| [MarisaOption.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\MarisaOption.java#L85) | 85 |
| [ReimuOption.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\ReimuOption.java#L46) | 46 |
| [CustomPlayer.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\CustomPlayer.java#L82) | 82 |
| [CustomOption.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\CustomOption.java#L109) | 109 |
| [Item.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\Item.java#L113) | 113 |
| [PowerUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\PowerUp.java#L65) | 65 |
| [LifeUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\LifeUp.java#L86) | 86 |
| [ScorePoint.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\ScorePoint.java#L126) | 126 |
| [BombUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\BombUp.java#L64) | 64 |
| [Laser.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\laser\Laser.java#L108) | 108 |
| [GameCanvas.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\ui\GameCanvas.java#L544) | 544 |
| [TitleScreen.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\ui\TitleScreen.java#L212) | 212 |
| [VirtualKeyboardPanel.java](file:///e:\Myproject\Game\JavaSTG\src\stg\base\VirtualKeyboardPanel.java#L49) | 49 |

#### 冗余代码

```java
g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
```

#### 优化建议

**方案1**: 创建 `RenderUtils` 工具类

```java
package stg.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RenderUtils {
    private RenderUtils() {}
    
    public static void enableAntiAliasing(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    
    public static void enableHighQualityRendering(Graphics2D g) {
        enableAntiAliasing(g);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
}
```

使用方式：
```java
@Override
public void render(Graphics2D g) {
    RenderUtils.enableAntiAliasing(g);
}
```

**方案2**: 在 `GameCanvas` 中统一设置

```java
public class GameCanvas extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderUtils.enableAntiAliasing(g2d);
        super.paintComponent(g2d);
    }
}
```

#### 预期收益

- 减少约 17 行重复代码
- 统一渲染质量设置
- 便于添加其他渲染优化（如文本抗锯齿）
- 提高代码可读性

---

### 1.3 无效的 alive 变量引用

**严重程度**: 🔴 严重  
**影响范围**: 12个文件  
**问题类型**: 编译错误

#### 问题描述

多个 Enemy 子类中使用了不存在的 `alive` 变量，导致编译错误。应该使用继承自 `Obj` 基类的 `isActive()` 方法。

#### 影响文件

| 文件 | 行号 | 问题代码 |
|------|------|----------|
| [BasicEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\BasicEnemy.java#L79) | 79 | `if (!alive \|\| gameCanvas == null)` |
| [BasicEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\BasicEnemy.java#L128) | 128 | `"alive=" + alive` |
| [TrackingEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\TrackingEnemy.java#L27) | 27 | `if (gameCanvas == null \|\| !alive)` |
| [SpiralEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\SpiralEnemy.java#L29) | 29 | `if (gameCanvas == null \|\| !alive)` |
| [LaserShootingEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\LaserShootingEnemy.java#L109) | 109 | `if (!alive \|\| gameCanvas == null)` |
| [OrbitEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\OrbitEnemy.java#L37) | 37 | `if (!alive)` |
| [OrbitEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\OrbitEnemy.java#L56) | 56 | `if (!alive \|\| gameCanvas == null)` |
| [RapidFireEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\RapidFireEnemy.java#L33) | 33 | `if (gameCanvas == null \|\| !alive)` |
| [RapidFireEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\RapidFireEnemy.java#L60) | 60 | `if (!alive \|\| gameCanvas == null)` |
| [SpreadEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\SpreadEnemy.java#L27) | 27 | `if (gameCanvas == null \|\| !alive)` |
| [SpreadEnemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\SpreadEnemy.java#L48) | 48 | `if (!alive \|\| gameCanvas == null)` |

#### 错误代码示例

```java
// 错误：使用不存在的 alive 变量
if (!alive || gameCanvas == null) return;

// 正确：使用继承的 isActive() 方法
if (!isActive() || gameCanvas == null) return;
```

#### 优化建议

**方案1**: 批量替换 `alive` 为 `isActive()`

在所有 Enemy 子类中：
```java
// 替换前
if (!alive || gameCanvas == null) return;

// 替换后
if (!isActive() || gameCanvas == null) return;
```

**方案2**: 在 `Enemy` 基类中添加便捷方法

```java
public class Enemy extends Obj {
    public boolean isAlive() {
        return isActive();
    }
}
```

#### 预期收益

- 修复编译错误
- 提高代码一致性
- 符合面向对象设计原则

---

## 二、中等冗余问题（应该修复）

### 2.1 未使用的抽象方法（onTaskStart/onTaskEnd）

**严重程度**: 🟡 中等  
**影响范围**: 4个基类  
**问题类型**: 设计问题

#### 问题描述

在 4 个基类中定义了抽象方法 `onTaskStart()` 和 `onTaskEnd()`，但这些方法几乎从未被实际调用。大部分子类提供空实现，造成不必要的抽象层次。

#### 影响文件

| 文件 | 行号 | 方法 |
|------|------|------|
| [Obj.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\obj\Obj.java#L367-L372) | 367-372 | `onTaskStart()`, `onTaskEnd()` |
| [Bullet.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\bullet\Bullet.java#L50-L55) | 50-55 | `onTaskStart()`, `onTaskEnd()` |
| [Enemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\Enemy.java#L173-L178) | 173-178 | `onTaskStart()`, `onTaskEnd()` |
| [Laser.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\laser\Laser.java#L272-L277) | 272-277 | `onTaskStart()`, `onTaskEnd()` |

#### 调用情况

- **实际调用**: 仅在 [StageSystemTest.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\stage\StageSystemTest.java) 中被调用
- **空实现**: [SimpleBullet](file:///e:\Myproject\Game\JavaSTG\src\stg\game\bullet\SimpleBullet.java)、[SimpleLaser](file:///e:\Myproject\Game\JavaSTG\src\stg\game\laser\SimpleLaser.java) 等多个子类提供空实现

#### 优化建议

**方案1**: 保留但添加文档说明

```java
/**
 * 任务开始时触发的方法
 * 注意：此方法仅在特定任务系统中使用，普通游戏对象通常不需要实现
 * @see StageSystemTest
 */
protected abstract void onTaskStart();
```

**方案2**: 改为空实现

```java
protected void onTaskStart() {
    // 默认空实现，子类可按需重写
}

protected void onTaskEnd() {
    // 默认空实现，子类可按需重写
}
```

**方案3**: 移除这些方法（如果确实不需要）

如果任务系统未完全实现，建议暂时移除这些方法，待功能完善后再添加。

#### 预期收益

- 减少不必要的抽象层次
- 降低子类实现负担
- 提高代码清晰度

---

### 2.2 道具吸引逻辑重复

**严重程度**: 🟡 中等  
**影响范围**: 3个文件  
**重复次数**: 3次

#### 问题描述

在多个 Item 子类中重复实现相同的道具吸引逻辑，仅在吸引距离和速度上有细微差异。

#### 影响文件

| 文件 | 行号 | 吸引距离 | 吸引速度 |
|------|------|----------|----------|
| [PowerUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\PowerUp.java#L36-L52) | 36-52 | 150.0f | 3.0f |
| [LifeUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\LifeUp.java#L49-L68) | 49-68 | 150.0f | 3.0f |
| [ScorePoint.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\ScorePoint.java#L70-L88) | 70-88 | 200.0f | 4.0f |

#### 冗余代码

```java
if (gameCanvas != null) {
    Player player = gameCanvas.getPlayer();
    if (player != null && player.isSlowMode()) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        if (distance < 150.0f) {  // 或 200.0f
            float attractionSpeed = 3.0f;  // 或 4.0f
            vx = (dx / distance) * attractionSpeed;
            vy = (dy / distance) * attractionSpeed;
        }
    }
}
```

#### 优化建议

**方案1**: 在 `Item` 基类中实现通用逻辑

```java
public abstract class Item extends Obj {
    protected float attractionDistance = 150.0f;
    protected float attractionSpeed = 3.0f;
    
    @Override
    protected void onUpdate() {
        applyAttraction();
    }
    
    protected void applyAttraction() {
        if (gameCanvas != null) {
            Player player = gameCanvas.getPlayer();
            if (player != null && player.isSlowMode()) {
                float dx = player.getX() - x;
                float dy = player.getY() - y;
                float distance = (float)Math.sqrt(dx * dx + dy * dy);
                
                if (distance < attractionDistance) {
                    vx = (dx / distance) * attractionSpeed;
                    vy = (dy / distance) * attractionSpeed;
                }
            }
        }
    }
    
    protected void setAttractionParams(float distance, float speed) {
        this.attractionDistance = distance;
        this.attractionSpeed = speed;
    }
}
```

子类使用：
```java
public class PowerUp extends Item {
    public PowerUp(float x, float y) {
        super(x, y, POWERUP_SIZE, POWERUP_COLOR);
        setAttractionParams(150.0f, 3.0f);
    }
}

public class ScorePoint extends Item {
    public ScorePoint(float x, float y) {
        super(x, y, SCORE_POINT_SIZE, SCORE_POINT_COLOR);
        setAttractionParams(200.0f, 4.0f);
    }
}
```

#### 预期收益

- 减少约 30 行重复代码
- 统一道具吸引行为
- 便于调整吸引参数
- 提高代码可维护性

---

### 2.3 未使用的 Example 类

**严重程度**: 🟡 中等  
**影响范围**: 6个文件  
**问题类型**: 代码清理

#### 问题描述

6个 Example 类从未被实际使用，仅作为示例代码存在。这些类占用代码库空间，可能误导开发者。

#### 影响文件

| 文件 | 行数 | 用途 |
|------|------|------|
| [BasicEnemyExample.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\BasicEnemyExample.java) | 74 | 敌人行为示例 |
| [BasicPlayerExample.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\BasicPlayerExample.java) | 118 | 玩家行为示例 |
| [StraightBulletExample.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\bullet\StraightBulletExample.java) | - | 子弹示例 |
| [PowerUpExample.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\PowerUpExample.java) | - | 道具示例 |
| [StraightLaserExample.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\laser\StraightLaserExample.java) | - | 激光示例 |
| [ResourceLoaderExample.java](file:///e:\Myproject\Game\JavaSTG\src\stg\util\ResourceLoaderExample.java) | - | 资源加载示例 |

#### 优化建议

**方案1**: 移动到 `examples/` 目录

```
src/
├── stg/
│   └── game/
└── examples/
    ├── BasicEnemyExample.java
    ├── BasicPlayerExample.java
    ├── StraightBulletExample.java
    ├── PowerUpExample.java
    ├── StraightLaserExample.java
    └── ResourceLoaderExample.java
```

**方案2**: 移动到 `doc/` 目录并添加说明

在 `doc/` 目录下创建 `EXAMPLES.md`，说明这些示例的用途。

**方案3**: 删除不需要的示例

如果这些示例已经过时或不再需要，直接删除。

#### 预期收益

- 清理代码库
- 避免误导开发者
- 减少编译时间
- 提高代码库整洁度

---

## 三、轻微冗余问题（建议修复）

### 3.1 硬编码的画布尺寸

**严重程度**: 🟢 轻微  
**影响范围**: 5个文件  
**问题类型**: 可维护性

#### 问题描述

画布尺寸 548x921 在多处硬编码，不利于未来修改画布尺寸。

#### 影响位置

| 文件 | 行号 | 硬编码值 |
|------|------|----------|
| [Obj.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\obj\Obj.java#L100) | 100 | 548, 921 |
| [Enemy.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\enemy\Enemy.java#L72) | 72 | 548, 921 |
| [Player.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\Player.java#L185) | 185 | 548, 921 |
| [Item.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\Item.java#L108) | 108 | 548, 921 |
| [Laser.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\laser\Laser.java#L166) | 166 | 548, 921 |

#### 优化建议

**方案1**: 在 `GameCanvas` 中定义常量

```java
public class GameCanvas extends JPanel {
    public static final int DEFAULT_WIDTH = 548;
    public static final int DEFAULT_HEIGHT = 921;
}
```

**方案2**: 在 `CoordinateSystem` 中定义

```java
public class CoordinateSystem {
    public static final int DEFAULT_CANVAS_WIDTH = 548;
    public static final int DEFAULT_CANVAS_HEIGHT = 921;
}
```

**方案3**: 使用配置文件

创建 `config.properties`:
```properties
canvas.width=548
canvas.height=921
```

---

### 3.2 重复的构造函数重载

**严重程度**: 🟢 轻微  
**影响范围**: 4个文件  
**问题类型**: API 设计

#### 问题描述

多个类提供了多个构造函数重载，但功能相似，造成 API 复杂化。

#### 影响文件

| 文件 | 构造函数数量 | 问题 |
|------|-------------|------|
| [Item.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\Item.java) | 5个 | 参数组合重复 |
| [ScorePoint.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\ScorePoint.java) | 12个 | 严重过度重载 |
| [PowerUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\PowerUp.java) | 3个 | 可接受 |
| [LifeUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\LifeUp.java) | 3个 | 可接受 |

#### 优化建议

**方案1**: 使用 Builder 模式

```java
public class ScorePoint extends Item {
    public static class Builder {
        private float x;
        private float y;
        private float vx = 0;
        private float vy = 0;
        private int scoreValue = 100;
        private boolean isLarge = false;
        private GameCanvas gameCanvas;
        
        public Builder(float x, float y) {
            this.x = x;
            this.y = y;
        }
        
        public Builder velocity(float vx, float vy) {
            this.vx = vx;
            this.vy = vy;
            return this;
        }
        
        public Builder scoreValue(int value) {
            this.scoreValue = value;
            return this;
        }
        
        public Builder large(boolean isLarge) {
            this.isLarge = isLarge;
            return this;
        }
        
        public Builder gameCanvas(GameCanvas canvas) {
            this.gameCanvas = canvas;
            return this;
        }
        
        public ScorePoint build() {
            return new ScorePoint(x, y, vx, vy, scoreValue, isLarge, gameCanvas);
        }
    }
}
```

使用方式：
```java
ScorePoint point1 = new ScorePoint.Builder(100, 200).build();
ScorePoint point2 = new ScorePoint.Builder(100, 200)
    .velocity(1.0f, 2.0f)
    .scoreValue(500)
    .large(true)
    .gameCanvas(canvas)
    .build();
```

**方案2**: 使用静态工厂方法

```java
public class ScorePoint extends Item {
    public static ScorePoint createSmall(float x, float y) {
        return new ScorePoint(x, y, SCORE_VALUE, false, null);
    }
    
    public static ScorePoint createLarge(float x, float y) {
        return new ScorePoint(x, y, SCORE_VALUE * 10, true, null);
    }
    
    public static ScorePoint createCustom(float x, float y, int scoreValue, GameCanvas canvas) {
        boolean isLarge = scoreValue >= SCORE_VALUE * 10;
        return new ScorePoint(x, y, scoreValue, isLarge, canvas);
    }
}
```

---

### 3.3 空的钩子方法实现

**严重程度**: 🟢 轻微  
**影响范围**: 7个文件  
**问题类型**: 设计模式

#### 问题描述

多个类中存在空的 `onUpdate()` 和 `onMove()` 方法实现，这些方法在基类中已经有空实现。

#### 影响文件

| 文件 | 空方法 |
|------|--------|
| [Obj.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\obj\Obj.java#L36-L44) | `onUpdate()`, `onMove()` |
| [Player.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\player\Player.java#L124-L130) | `onUpdate()`, `onMove()` |
| [SimpleLaser.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\laser\SimpleLaser.java#L42-L50) | `onUpdate()`, `onMove()` |
| [LinearLaser.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\laser\LinearLaser.java#L70-L78) | `onUpdate()`, `onMove()` |
| [Item.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\Item.java#L42-L51) | `onUpdate()`, `onMove()` |
| [LifeUp.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\LifeUp.java#L47-L76) | `onUpdate()`, `onMove()` |
| [ScorePoint.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\item\ScorePoint.java#L68-L94) | `onUpdate()`, `onMove()` |

#### 优化建议

移除不必要的空方法重写，只在需要时才重写。

---

## 四、性能相关问题

### 4.1 频繁的 Math.cos/sin 调用

**严重程度**: 🟡 中等  
**影响范围**: [Obj.java](file:///e:\Myproject\Game\JavaSTG\src\stg\game\obj\Obj.java#L193-L197)  
**问题类型**: 性能优化

#### 问题描述

在 `turnBy()` 方法中每次都计算三角函数，如果此方法被频繁调用，可能影响性能。

#### 代码位置

```java
public void turnBy(float angle) {
    float cosAngle = (float)Math.cos(angle);
    float sinAngle = (float)Math.sin(angle);
    float newVx = vx * cosAngle - vy * sinAngle;
    float newVy = vx * sinAngle + vy * cosAngle;
    this.vx = newVx;
    this.vy = newVy;
}
```

#### 优化建议

**方案1**: 使用查找表

```java
public class TrigTable {
    private static final int TABLE_SIZE = 3600; // 0.1度精度
    private static final float[] sinTable = new float[TABLE_SIZE];
    private static final float[] cosTable = new float[TABLE_SIZE];
    
    static {
        for (int i = 0; i < TABLE_SIZE; i++) {
            double angle = Math.PI * 2 * i / TABLE_SIZE;
            sinTable[i] = (float)Math.sin(angle);
            cosTable[i] = (float)Math.cos(angle);
        }
    }
    
    public static float sin(float angle) {
        int index = (int)((angle % (Math.PI * 2)) / (Math.PI * 2) * TABLE_SIZE);
        if (index < 0) index += TABLE_SIZE;
        return sinTable[index % TABLE_SIZE];
    }
    
    public static float cos(float angle) {
        int index = (int)((angle % (Math.PI * 2)) / (Math.PI * 2) * TABLE_SIZE);
        if (index < 0) index += TABLE_SIZE;
        return cosTable[index % TABLE_SIZE];
    }
}
```

**方案2**: 缓存常用角度

如果某些角度被频繁使用，可以缓存计算结果。

---

## 五、重构优先级与建议

### 5.1 优先级分类

#### 高优先级（立即修复）

1. ✅ 修复无效的 `alive` 变量引用（编译错误）
2. ✅ 统一坐标转换逻辑
3. ✅ 创建 `RenderUtils` 工具类

#### 中优先级（近期修复）

4. ⚠️ 评估 `onTaskStart/onTaskEnd` 方法的必要性
5. ⚠️ 提取道具吸引逻辑到基类
6. ⚠️ 移动或删除未使用的 Example 类

#### 低优先级（计划修复）

7. 📋 提取硬编码的画布尺寸
8. 📋 简化构造函数重载
9. 📋 优化渲染方法重复逻辑

### 5.2 重构步骤建议

#### 第一阶段：修复编译错误

```bash
# 1. 替换所有 alive 为 isActive()
# 2. 确保项目可以正常编译
javac -cp src src/**/*.java
```

#### 第二阶段：消除严重冗余

```bash
# 1. 创建 RenderUtils 工具类
# 2. 在 Obj.java 中添加 toScreenCoords() 方法
# 3. 批量替换重复代码
```

#### 第三阶段：优化中等问题

```bash
# 1. 提取道具吸引逻辑
# 2. 处理未使用的 Example 类
# 3. 评估抽象方法的必要性
```

#### 第四阶段：改进轻微问题

```bash
# 1. 提取硬编码常量
# 2. 简化构造函数
# 3. 性能优化
```

### 5.3 预期收益总结

| 类别 | 减少代码行数 | 提高可维护性 | 降低错误风险 |
|------|-------------|-------------|-------------|
| 坐标转换重复 | ~40行 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| 抗锯齿设置重复 | ~17行 | ⭐⭐⭐⭐ | ⭐⭐⭐ |
| 道具吸引逻辑重复 | ~30行 | ⭐⭐⭐⭐ | ⭐⭐⭐ |
| 构造函数简化 | ~50行 | ⭐⭐⭐ | ⭐⭐ |
| **总计** | **~137行** | **⭐⭐⭐⭐** | **⭐⭐⭐** |

---

## 六、代码质量指标

### 6.1 当前状态

| 指标 | 评分 | 说明 |
|------|------|------|
| 代码重复度 | ⭐⭐ | 存在多处重复代码 |
| 可维护性 | ⭐⭐⭐ | 结构清晰但有冗余 |
| 可扩展性 | ⭐⭐⭐⭐ | 继承层次清晰 |
| 性能 | ⭐⭐⭐⭐ | 无明显性能瓶颈 |
| 代码规范 | ⭐⭐⭐ | 基本遵循规范 |
| 文档完整性 | ⭐⭐⭐ | 有注释但不够详细 |

### 6.2 目标状态（重构后）

| 指标 | 当前评分 | 目标评分 |
|------|---------|---------|
| 代码重复度 | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| 可维护性 | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 可扩展性 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 性能 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 代码规范 | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| 文档完整性 | ⭐⭐⭐ | ⭐⭐⭐⭐ |

---

## 七、附录

### 7.1 文件清单

#### 审查的文件总数: 72 个 Java 文件

#### 按包分类

| 包 | 文件数 | 问题数 |
|------|--------|--------|
| stg.game.bullet | 10 | 2 |
| stg.game.enemy | 11 | 3 |
| stg.game.item | 7 | 4 |
| stg.game.laser | 9 | 2 |
| stg.game.obj | 1 | 2 |
| stg.game.player | 11 | 3 |
| stg.game.stage | 5 | 1 |
| stg.game.ui | 3 | 1 |
| stg.util | 5 | 0 |
| 其他 | 10 | 0 |

### 7.2 参考资源

- [Effective Java](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
- [Clean Code](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
- [Refactoring](https://refactoring.guru/)

### 7.3 术语表

| 术语 | 说明 |
|------|------|
| DRY | Don't Repeat Yourself - 不要重复自己 |
| API | Application Programming Interface - 应用程序编程接口 |
| Builder Pattern | 建造者模式 - 一种创建型设计模式 |
| Template Method | 模板方法模式 - 一种行为型设计模式 |

---

## 八、审查结论

本次审查识别了 Java STG 项目中的主要冗余代码问题。通过系统性的重构，可以：

1. **减少约 137 行重复代码**
2. **修复 12 处编译错误**
3. **提高代码可维护性和可读性**
4. **降低未来维护成本**
5. **为功能扩展奠定良好基础**

建议按照优先级逐步进行重构，优先修复编译错误和严重冗余问题，然后逐步优化中等问题。

---

**报告生成时间**: 2026-01-30  
**审查人**: Code Reviewer  
**版本**: 1.0

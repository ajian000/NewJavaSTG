# Item系统使用指南

## 概述

Item系统是游戏内所有物品的基类，提供了统一的物品管理接口。所有道具、掉落物、特殊物品等都应继承自`Item`类。

## 基类：Item

### 位置
`src/stg/game/item/Item.java`

### 主要功能
- 物品位置和速度管理
- 物品渲染（使用中心原点坐标系）
- 碰撞检测支持
- 边界检测
- 物品激活状态管理
- 物品拾取处理

### 核心方法

#### 构造函数
```java
// 基本构造函数
public Item(float x, float y, float size, Color color)

// 带速度的构造函数
public Item(float x, float y, float vx, float vy, float size, Color color)

// 完整构造函数
public Item(float x, float y, float vx, float vy, float size, Color color, GameCanvas gameCanvas)
```

#### 更新方法
```java
public void update()
```
更新物品状态，包括位置更新和边界检测。子类可重写此方法添加特定行为。

#### 渲染方法
```java
public void render(Graphics2D g)
```
渲染物品到屏幕。子类可重写此方法实现自定义渲染。

#### 拾取处理
```java
public void onCollect()
```
物品被玩家拾取时的处理逻辑。子类应重写此方法实现特定效果。

#### 其他常用方法
- `getX()`, `getY()` - 获取坐标
- `getSize()` - 获取大小
- `getHitboxRadius()`, `setHitboxRadius()` - 碰撞判定半径
- `isActive()`, `setActive()` - 激活状态
- `setPosition(float x, float y)` - 设置位置
- `setVelocity(float vx, float vy)` - 设置速度
- `getFrame()` - 获取帧计数器

## 内置物品类型

### 1. PowerUp（强化道具）

**位置**: `src/stg/game/item/PowerUp.java`

**功能**: 增加玩家火力

**特点**:
- 显示"P"标志
- 低速模式下自动向玩家移动
- 金黄色外观

**使用示例**:
```java
// 创建强化道具
PowerUp powerUp = new PowerUp(x, y, gameCanvas);

// 添加到游戏画布
gameCanvas.addItem(powerUp);
```

### 2. LifeUp（生命道具）

**位置**: `src/stg/game/item/LifeUp.java`

**功能**: 恢复玩家生命

**特点**:
- 显示心形图案
- 低速模式下自动向玩家移动
- 绿色外观

**使用示例**:
```java
// 创建生命道具
LifeUp lifeUp = new LifeUp(x, y, gameCanvas);

// 添加到游戏画布
gameCanvas.addItem(lifeUp);
```

### 3. BombUp（炸弹道具）

**位置**: `src/stg/game/item/BombUp.java`

**功能**: 增加玩家炸弹数量

**特点**:
- 显示"B"标志
- 低速模式下自动向玩家移动
- 红色外观

**使用示例**:
```java
// 创建炸弹道具
BombUp bombUp = new BombUp(x, y, gameCanvas);

// 添加到游戏画布
gameCanvas.addItem(bombUp);
```

## 创建自定义物品

### 步骤1：继承Item类

```java
package stg.game.item;

import java.awt.*;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

public class CustomItem extends Item {
    // 定义物品常量
    private static final float ITEM_SIZE = 10.0f;
    private static final Color ITEM_COLOR = Color.CYAN;

    public CustomItem(float x, float y) {
        super(x, y, ITEM_SIZE, ITEM_COLOR);
    }

    public CustomItem(float x, float y, GameCanvas gameCanvas) {
        super(x, y, ITEM_SIZE, ITEM_COLOR, gameCanvas);
    }
}
```

### 步骤2：重写update方法（可选）

```java
@Override
public void update() {
    super.update(); // 调用父类update方法

    // 添加自定义行为
    // 例如：旋转效果、特殊移动模式等
    float angle = frame * 0.1f;
    vx = (float)Math.sin(angle) * 2.0f;
    vy = (float)Math.cos(angle) * 2.0f;
}
```

### 步骤3：重写render方法（可选）

```java
@Override
public void render(Graphics2D g) {
    if (!active) return;

    float screenX = x;
    float screenY = y;

    if (gameCanvas != null) {
        float[] coords = gameCanvas.getCoordinateSystem().toScreenCoords(x, y);
        screenX = coords[0];
        screenY = coords[1];
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // 自定义渲染
    g.setColor(color);
    g.fillRect((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));
}
```

### 步骤4：重写onCollect方法（必需）

```java
@Override
public void onCollect() {
    super.onCollect(); // 调用父类onCollect方法

    if (gameCanvas != null) {
        Player player = gameCanvas.getPlayer();
        if (player != null) {
            // 实现拾取效果
            System.out.println("CustomItem collected!");
            // 可以在这里修改玩家属性
        }
    }
}
```

## 在GameCanvas中集成Item系统

### 1. 添加Item列表

```java
import stg.game.item.Item;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends JPanel {
    private List<Item> items;

    public GameCanvas() {
        items = new ArrayList<>();
        // ... 其他初始化代码
    }
}
```

### 2. 更新Item列表

```java
public void update() {
    // ... 其他更新代码

    // 更新所有物品
    Iterator<Item> itemIterator = items.iterator();
    while (itemIterator.hasNext()) {
        Item item = itemIterator.next();
        item.update();

        // 移除不活跃的物品
        if (!item.isActive()) {
            itemIterator.remove();
        }
    }
}
```

### 3. 渲染Item列表

```java
public void render(Graphics2D g) {
    // ... 其他渲染代码

    // 渲染所有物品
    for (Item item : items) {
        item.render(g);
    }
}
```

### 4. 添加物品管理方法

```java
public void addItem(Item item) {
    item.setGameCanvas(this);
    items.add(item);
}

public void removeItem(Item item) {
    items.remove(item);
}

public List<Item> getItems() {
    return items;
}

public void clearItems() {
    items.clear();
}
```

### 5. 检测玩家拾取物品

```java
public void checkItemCollection() {
    Player player = getPlayer();
    if (player == null) return;

    Iterator<Item> itemIterator = items.iterator();
    while (itemIterator.hasNext()) {
        Item item = itemIterator.next();
        if (!item.isActive()) continue;

        // 检查碰撞
        float dx = item.getX() - player.getX();
        float dy = item.getY() - player.getY();
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if (distance < item.getHitboxRadius() + player.getSize()) {
            item.onCollect();
            itemIterator.remove();
        }
    }
}
```

## 敌人死亡时掉落物品

```java
public class Enemy {
    // ... 其他代码

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            onDeath();
        }
    }

    private void onDeath() {
        alive = false;

        // 随机掉落物品
        if (gameCanvas != null) {
            float dropChance = 0.3f; // 30%掉落率
            if (Math.random() < dropChance) {
                int itemType = (int)(Math.random() * 3);
                Item item;

                switch (itemType) {
                    case 0:
                        item = new PowerUp(x, y, gameCanvas);
                        break;
                    case 1:
                        item = new LifeUp(x, y, gameCanvas);
                        break;
                    case 2:
                        item = new BombUp(x, y, gameCanvas);
                        break;
                    default:
                        item = new PowerUp(x, y, gameCanvas);
                }

                gameCanvas.addItem(item);
            }
        }
    }
}
```

## 最佳实践

1. **继承设计**: 所有物品都应继承自`Item`类，保持一致性
2. **重写关键方法**: 根据需求重写`update()`、`render()`和`onCollect()`方法
3. **使用常量**: 定义物品大小、颜色等常量，便于统一管理
4. **碰撞检测**: 使用`hitboxRadius`进行精确的碰撞检测
5. **性能优化**: 及时移除不活跃的物品，避免内存泄漏
6. **视觉效果**: 利用`frame`计数器创建动画效果

## 注意事项

1. **坐标系统**: 物品使用中心原点坐标系，与游戏其他部分保持一致
2. **激活状态**: 使用`active`标志管理物品状态，不要直接从列表中删除
3. **边界检测**: 物品超出边界后会自动设置为不活跃状态
4. **游戏画布**: 记得调用`setGameCanvas()`方法设置画布引用
5. **拾取逻辑**: 在`onCollect()`方法中实现拾取效果，不要在碰撞检测中直接处理

## 扩展建议

1. **更多物品类型**: 可以创建更多种类的物品，如分数道具、特殊道具等
2. **物品动画**: 利用`frame`计数器创建旋转、缩放等动画效果
3. **物品组合**: 创建组合道具，同时提供多种效果
4. **物品等级**: 实现物品等级系统，不同等级提供不同效果
5. **物品特效**: 添加粒子效果、发光效果等视觉特效

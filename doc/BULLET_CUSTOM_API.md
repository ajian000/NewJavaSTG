# 子弹自定义功能 API 文档

## 概述

为 `Bullet` 基类添加了丰富的自定义功能，支持移动控制、轨迹编辑和速度管理。

## 新增功能

### 1. 位置控制

#### `moveTo(float x, float y)`
直接设置子弹位置到指定坐标。

**参数：**
- `x` - 目标X坐标
- `y` - 目标Y坐标

**示例：**
```java
Bullet bullet = new Bullet(0, 0, 2, 2, 5, Color.RED);
bullet.moveTo(100, 50); // 子弹瞬移到 (100, 50)
```

#### `moveOn(float dx, float dy)`
在当前位置基础上移动指定增量。

**参数：**
- `dx` - X方向增量
- `dy` - Y方向增量

**示例：**
```java
Bullet bullet = new Bullet(0, 0, 2, 2, 5, Color.RED);
bullet.moveOn(10, 20); // 子弹移动到 (10, 20)
```

### 2. 轨迹编辑 - 两套模式

#### `turnBy(float angle)`
旋转速度方向，改变子弹的运动轨迹。

**参数：**
- `angle` - 转向角度（弧度）
  - 逆时针旋转：正角度（如 Math.PI/4 = 45°）
  - 顺时针旋转：负角度（如 -Math.PI/6 = -30°）

**示例：**
```java
Bullet bullet = new Bullet(0, 0, 2, 0, 5, Color.RED); // 向右移动
bullet.turnBy((float)(Math.PI / 4)); // 逆时针旋转45度，向右上移动
bullet.turnBy((float)(-Math.PI / 6)); // 顺时针旋转30度
```

**应用场景：**
- 创建弯曲弹道
- 制作追踪弹幕
- 实现螺旋运动

### 3. 加速度控制 - 两套模式

#### 【模式1】`accelerate(float acceleration, float angle)`
在指定方向上增加速度（绝对值+角度模式）。

**参数：**
- `acceleration` - 加速度值（像素/帧）
- `angle` - 加速度方向角度（弧度）
  - x轴正轴为0
  - 向第一象限（右上）为正角度
  - 使用 `Math.PI` 表示180度

**示例：**
```java
Bullet bullet = new Bullet(0, 0, 2, 0, 5, Color.RED);

// 在x轴正方向加速1像素/帧
bullet.accelerate(1.0f, 0); // 速度从2增加到3

// 在y轴正方向（向上）加速1像素/帧
bullet.accelerate(1.0f, (float)(Math.PI / 2)); // 速度变为 (3, 1)

// 在当前运动方向加速
float currentAngle = bullet.getVelocityAngle();
bullet.accelerate(0.2f, currentAngle);
```

**适用场景：**
- 径向加速（从中心向外加速）
- 追踪加速（朝目标加速）
- 任意方向加速

#### 【模式2】`accelerateByComponent(float ax, float ay)`
按x和y分量直接增加速度（分量模式）。

**参数：**
- `ax` - X方向加速度（像素/帧²）
- `ay` - Y方向加速度（像素/帧²）

**示例：**
```java
Bullet bullet = new Bullet(0, 0, 2, 0, 5, Color.RED);

// x轴方向加速1
bullet.accelerateByComponent(1.0f, 0); // 速度从2增加到3

// y轴方向加速1（向上）
bullet.accelerateByComponent(0, 1.0f); // 速度变为 (3, 1)

// 模拟重力（向下）
bullet.accelerateByComponent(0, -0.15f); // 每帧向下加速

// 模拟风力（向右）
bullet.accelerateByComponent(0.05f, 0); // 每帧向右加速
```

**适用场景：**
- 重力弹幕（y轴持续变化）
- 风/水流效果（单方向持续影响）
- 物理模拟（精确控制每个轴）
- 摆动运动（单轴变化）

### 4. 速度查询 - 两套模式

#### 【模式1】`getVelocityAngle()`
获取当前速度方向角度。

**返回值：** 速度角度（弧度），x轴正轴为0，向第一象限为正

**示例：**
```java
float angle = bullet.getVelocityAngle();
double degrees = Math.toDegrees(angle);
System.out.println("速度角度: " + degrees + "°");
```

#### 【模式2】`getVelocityX()`
获取X轴速度分量。

**返回值：** X轴速度（像素/帧）

**示例：**
```java
float vx = bullet.getVelocityX();
System.out.println("X轴速度: " + vx + " 像素/帧");
```

#### 【模式2】`getVelocityY()`
获取Y轴速度分量。

**返回值：** Y轴速度（像素/帧）

**示例：**
```java
float vy = bullet.getVelocityY();
System.out.println("Y轴速度: " + vy + " 像素/帧");
```

#### 【模式1】`getVelocitySpeed()`
获取当前速度大小。

**返回值：** 速度大小（像素/帧）

**示例：**
```java
float speed = bullet.getVelocitySpeed();
System.out.println("速度: " + speed + " 像素/帧");
```

### 5. 速度设置 - 两套模式

#### 【模式1】`setVelocity(float speed, float angle)`
设置子弹的速度大小和方向（绝对值+角度模式）。

**参数：**
- `speed` - 速度大小（像素/帧）
- `angle` - 速度方向角度（弧度），x轴正轴为0，向第一象限为正

**示例：**
```java
// 设置速度为5，方向135度（第二象限）
bullet.setVelocity(5, (float)(3 * Math.PI / 4));

// 瞬间停止
bullet.setVelocity(0, 0);

// 径向弹幕：向8个方向发射
for (int i = 0; i < 8; i++) {
    float angle = (float)(i * Math.PI / 4); // 0, 45, 90, 135度...
    Bullet b = new Bullet(0, 0, 0, 0, 5, Color.RED);
    b.setVelocity(5, angle);
}
```

#### 【模式2】`setVelocityByComponent(float vx, float vy)`
设置子弹速度的x和y分量（分量模式）。

**参数：**
- `vx` - X方向速度（像素/帧）
- `vy` - Y方向速度（像素/帧）

**示例：**
```java
// 设置速度分量
bullet.setVelocityByComponent(3, 4);

// 瞬间停止
bullet.setVelocityByComponent(0, 0);

// 反弹
float vx = bullet.getVelocityX();
float vy = bullet.getVelocityY();
bullet.setVelocityByComponent(-vx, -vy); // 反向

// 仅改变x轴速度
bullet.setVelocityByComponent(5, bullet.getVelocityY());
```

### 6. 轨迹编辑

#### `turnBy(float angle)`
旋转速度方向，改变子弹的运动轨迹。

**参数：**
- `angle` - 转向角度（弧度）
  - 逆时针旋转：正角度（如 Math.PI/4 = 45°）
  - 顺时针旋转：负角度（如 -Math.PI/6 = -30°）

**示例：**
```java
Bullet bullet = new Bullet(0, 0, 2, 0, 5, Color.RED); // 向右移动
bullet.turnBy((float)(Math.PI / 4)); // 逆时针旋转45度，向右上移动
bullet.turnBy((float)(-Math.PI / 6)); // 顺时针旋转30度
```

**应用场景：**
- 创建弯曲弹道
- 制作追踪弹幕
- 实现螺旋运动

### 3. 加速度控制

#### `accelerate(float acceleration, float angle)`
在指定方向上增加速度。

**参数：**
- `acceleration` - 加速度值（像素/帧）
- `angle` - 加速度方向角度（弧度）
  - x轴正轴为0
  - 向第一象限（右上）为正角度
  - 使用 `Math.PI` 表示180度

**示例：**
```java
Bullet bullet = new Bullet(0, 0, 2, 0, 5, Color.RED);

// 在x轴正方向加速1像素/帧
bullet.accelerate(1.0f, 0); // 速度从2增加到3

// 在y轴正方向（向上）加速1像素/帧
bullet.accelerate(1.0f, (float)(Math.PI / 2)); // 速度变为 (3, 1)

// 在当前运动方向加速
float currentAngle = bullet.getVelocityAngle();
bullet.accelerate(0.2f, currentAngle);
```

**应用场景：**
- 加速弹幕
- 重力效果
- 变速攻击

### 4. 速度查询

#### `getVelocityAngle()`
获取当前速度方向角度。

**返回值：** 速度角度（弧度），x轴正轴为0，向第一象限为正

**示例：**
```java
float angle = bullet.getVelocityAngle();
double degrees = Math.toDegrees(angle);
System.out.println("速度角度: " + degrees + "°");
```

#### `getVelocitySpeed()`
获取当前速度大小。

**返回值：** 速度大小（像素/帧）

**示例：**
```java
float speed = bullet.getVelocitySpeed();
System.out.println("速度: " + speed + " 像素/帧");
```

### 5. 速度设置

#### `setVelocity(float speed, float angle)`
设置子弹的速度大小和方向。

**参数：**
- `speed` - 速度大小（像素/帧）
- `angle` - 速度方向角度（弧度），x轴正轴为0，向第一象限为正

**示例：**
```java
// 设置速度为5，方向135度（第二象限）
bullet.setVelocity(5, (float)(3 * Math.PI / 4));

// 瞬间停止
bullet.setVelocity(0, 0);
```

## 综合应用示例

### 示例1: 弯曲弹道
```java
Bullet bullet = new Bullet(-100, 0, 3, 0, 5, Color.BLUE);

// 每帧轻微转向，创建平滑的弯曲弹道
for (int frame = 0; frame < 60; frame++) {
    bullet.turnBy(0.05f); // 每帧逆时针转向
    bullet.update();
}
```

### 示例2: 加速弹幕
```java
Bullet bullet = new Bullet(-100, 0, 1, 1, 5, Color.GREEN);

// 在运动方向持续加速
for (int frame = 0; frame < 60; frame++) {
    float currentAngle = bullet.getVelocityAngle();
    bullet.accelerate(0.1f, currentAngle); // 每帧加速0.1
    bullet.update();
}
```

### 示例3: 螺旋运动
```java
Bullet bullet = new Bullet(0, -200, 0, 3, 5, Color.MAGENTA);

// 螺旋弹道：持续转向 + 定期加速
for (int frame = 0; frame < 120; frame++) {
    bullet.turnBy(-0.15f); // 顺时针转向

    // 每隔5帧加速一次
    if (frame % 5 == 0) {
        float currentAngle = bullet.getVelocityAngle();
        bullet.accelerate(0.3f, currentAngle);
    }

    bullet.update();
}
```

### 示例4: 追踪弹幕
```java
Bullet bullet = new Bullet(enemyX, enemyY, 3, 0, 5, Color.RED);
Player player = gameCanvas.getPlayer();

// 每帧调整方向指向玩家
for (int frame = 0; frame < 60; frame++) {
    // 计算指向玩家的角度
    float dx = player.getX() - bullet.getX();
    float dy = player.getY() - bullet.getY();
    float targetAngle = (float)Math.atan2(dy, dx);

    // 获取当前速度角度
    float currentAngle = bullet.getVelocityAngle();

    // 计算角度差（转向量）
    float angleDiff = targetAngle - currentAngle;

    // 限制每帧转向速度
    float maxTurn = 0.1f; // 每帧最多转向约5.7度
    if (angleDiff > maxTurn) angleDiff = maxTurn;
    if (angleDiff < -maxTurn) angleDiff = -maxTurn;

    // 应用转向
    bullet.turnBy(angleDiff);
    bullet.update();
}
```

### 示例5: 重力弹幕
```java
Bullet bullet = new Bullet(playerX, playerY, 5, 8, 6, Color.ORANGE);

// 模拟重力效果（y轴负方向持续减速）
for (int frame = 0; frame < 120; frame++) {
    // 重力加速度（向下）
    bullet.accelerate(0.15f, (float)(-Math.PI / 2));

    bullet.update();
}
```

## 角度系统说明

### 角度表示
- 使用弧度制（radians），不是角度制（degrees）
- 转换：`弧度 = 角度 × π / 180`，`角度 = 弧度 × 180 / π`

### 角度参考系
```
    y+ (上)
    |
    |
    +-------- x+ (右)
   /
  /
y- (下)
```

- 0度：x轴正方向（向右）
- π/2（90度）：y轴正方向（向上）
- π（180度）：x轴负方向（向左）
- 3π/2（270度）：y轴负方向（向下）

### 常用角度值
```java
// 0度（向右）
float angle0 = 0;

// 45度（向右上）
float angle45 = (float)(Math.PI / 4);

// 90度（向上）
float angle90 = (float)(Math.PI / 2);

// 135度（向左上）
float angle135 = (float)(3 * Math.PI / 4);

// 180度（向左）
float angle180 = (float)(Math.PI);

// -45度（向右下）
float angleNeg45 = (float)(-Math.PI / 4);
```

## 注意事项

1. **坐标系：** 游戏使用中心原点坐标系，x轴向右为正，y轴向上为正
2. **弧度制：** 所有角度参数使用弧度，不是角度
3. **帧率：** 加速度单位是"像素/帧"，基于60FPS的更新频率
4. **性能：** 复杂的轨迹计算（如追踪弹幕）可能影响性能，注意优化
5. **碰撞检测：** 修改速度后不影响碰撞检测，子弹仍然按新的轨迹移动
6. **模式选择：** 根据实际需求选择合适的模式，两套模式可以混合使用

## 模式选择指南

### 【模式1: 绝对值+角度】适用场景

适合需要"速度大小"和"方向"的场景：

| 场景 | 说明 | 推荐方法 |
|------|------|----------|
| **径向弹幕** | 从中心向四周发射 | `setVelocity(speed, angle)` |
| **追踪弹幕** | 计算指向目标的角度 | `accelerate(acc, angle)` + `getVelocityAngle()` |
| **螺旋弹幕** | 持续转向 | `turnBy(angle)` + `setVelocity(speed, angle)` |
| **圆环弹幕** | 多个角度同时发射 | 循环调用 `setVelocity(speed, i * angle)` |
| **任意方向加速** | 不规则的加速方向 | `accelerate(acc, angle)` |

**优点：**
- 直观易懂（大小+方向）
- 适合径向和环形弹幕
- 计算角度方便

**缺点：**
- 需要进行三角函数计算
- x/y轴独立控制稍显复杂

### 【模式2: 分量】适用场景

适合需要精确控制x/y轴独立运动的场景：

| 场景 | 说明 | 推荐方法 |
|------|------|----------|
| **重力弹幕** | y轴持续加速 | `accelerateByComponent(0, -gravity)` |
| **风/水流** | 单方向持续影响 | `accelerateByComponent(ax, 0)` 或 `(0, ay)` |
| **摆动/震荡** | 单轴正弦变化 | `accelerateByComponent(ax, 0)` |
| **反弹** | 翻转速度分量 | `setVelocityByComponent(-vx, -vy)` |
| **物理模拟** | 力、加速度、速度 | `accelerateByComponent(ax, ay)` |
| **精确控制** | 需要精确数值 | `setVelocityByComponent(vx, vy)` |

**优点：**
- 精确控制每个轴
- 适合物理模拟
- 避免三角函数计算
- 代码更简洁

**缺点：**
- 不够直观（x/y分量）
- 径向弹幕需要计算分量

### 两套模式混合使用

**最佳实践：** 根据场景需求灵活组合两套模式

```java
// 示例: 用模式1设置初始方向,用模式2添加物理效果
Bullet bullet = new Bullet(0, 0, 0, 0, 5, Color.RED);

// 模式1: 设置初始方向（径向弹幕）
bullet.setVelocity(5, (float)(Math.PI / 4)); // 45度

// 模式2: 添加重力效果
for (int frame = 0; frame < 60; frame++) {
    bullet.accelerateByComponent(0, -0.1f); // 向下加速
    bullet.update();
}

// 示例: 用模式1追踪,用模式2添加风力
for (int frame = 0; frame < 60; frame++) {
    // 模式1: 计算追踪角度
    float targetAngle = (float)Math.atan2(targetY - bullet.getY(), targetX - bullet.getX());
    bullet.turnBy(targetAngle - bullet.getVelocityAngle());

    // 模式2: 添加风向右的风力
    bullet.accelerateByComponent(0.05f, 0);

    bullet.update();
}
```

### 性能考虑

- **三角函数开销：** 模式1涉及三角函数计算，若频繁使用可考虑模式2
- **混合使用：** 设置初始值用模式1，后续更新用模式2，既直观又高效
- **预计算：** 对于固定角度，可预计算cos/sin值

## 运行示例

编译并运行示例代码：
```bash
# 模式1和模式2示例
javac -encoding UTF-8 -d bin -sourcepath src src/stg/game/bullet/BulletTwoModesExample.java
java -cp bin stg.game.bullet.BulletTwoModesExample

# 基础功能示例
javac -encoding UTF-8 -d bin -sourcepath src src/stg/game/bullet/BulletExample.java
java -cp bin stg.game.bullet.BulletExample
```

示例程序会演示所有新功能的使用方法和效果。


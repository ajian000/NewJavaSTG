# 子机系统使用示例

## 快速开始

### 1. 使用现有的子机类

#### 灵梦子机（追踪弹）

```java
import stg.game.player.ReimuPlayer;
import stg.game.player.ReimuOption;

// 创建灵梦玩家
ReimuPlayer player = new ReimuPlayer(0, -200);

// 初始化子机（会自动创建2个追踪弹子机）
player.initializeOptions(gameCanvas);
```

#### 魔理沙子机（高威力集中弹）

```java
import stg.game.player.MarisaPlayer;
import stg.game.player.MarisaOption;

// 创建魔理沙玩家
MarisaPlayer player = new MarisaPlayer(0, -200);

// 初始化子机（会自动创建2个高威力子机）
player.initializeOptions(gameCanvas);
```

### 2. 创建自定义子机

```java
import stg.game.player.Option;
import stg.game.bullet.SimpleBullet;
import stg.game.ui.GameCanvas;
import java.awt.*;

public class MyOption extends Option {
    public MyOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
        super(player, offsetX, offsetY, gameCanvas);
        
        // 配置子机参数
        setSize(10.0f);
        setColor(new Color(255, 100, 100));
        setShootInterval(2);
        setBulletDamage(2);
        setFollowSpeed(0.2f);
    }

    @Override
    protected void shoot() {
        // 实现射击逻辑
        SimpleBullet bullet = new SimpleBullet(x, y, 0, 50.0f, 4.0f, Color.RED);
        bullet.setGameCanvas(gameCanvas);
        bullet.setDamage(bulletDamage);
        gameCanvas.addBullet(bullet);
    }
}
```

### 3. 在自定义玩家中使用子机

```java
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

public class MyPlayer extends Player {
    public MyPlayer(float spawnX, float spawnY) {
        super(spawnX, spawnY);
        // 配置玩家参数
        setSpeed(5.0f);
        setSpeedSlow(2.0f);
        setSize(18.0f);
    }

    public void initializeOptions(GameCanvas canvas) {
        setGameCanvas(canvas);
        
        // 添加子机
        MyOption option1 = new MyOption(this, -25, 10, canvas);
        MyOption option2 = new MyOption(this, 25, 10, canvas);
        
        addOption(option1);
        addOption(option2);
    }
}
```

## 高级示例

### 示例1：多模式子机

```java
public class MultiModeOption extends Option {
    private int currentMode = 0;
    private int modeTimer = 0;
    
    public MultiModeOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
        super(player, offsetX, offsetY, gameCanvas);
        setShootInterval(2);
        setBulletDamage(2);
    }
    
    @Override
    protected void shoot() {
        modeTimer++;
        if (modeTimer >= 120) { // 每2秒切换模式
            modeTimer = 0;
            currentMode = (currentMode + 1) % 3;
        }
        
        switch (currentMode) {
            case 0:
                shootStraight(); // 直线弹
                break;
            case 1:
                shootSpread(); // 扩散弹
                break;
            case 2:
                shootCircular(); // 圆形弹
                break;
        }
    }
    
    private void shootStraight() {
        SimpleBullet bullet = new SimpleBullet(x, y, 0, 50.0f, 4.0f, Color.BLUE);
        bullet.setGameCanvas(gameCanvas);
        bullet.setDamage(bulletDamage);
        gameCanvas.addBullet(bullet);
    }
    
    private void shootSpread() {
        for (int i = -2; i <= 2; i++) {
            float vx = i * 2.0f;
            SimpleBullet bullet = new SimpleBullet(x, y, vx, 45.0f, 3.5f, Color.GREEN);
            bullet.setGameCanvas(gameCanvas);
            bullet.setDamage(bulletDamage);
            gameCanvas.addBullet(bullet);
        }
    }
    
    private void shootCircular() {
        int count = 8;
        float angleStep = (float)(2 * Math.PI / count);
        for (int i = 0; i < count; i++) {
            float angle = i * angleStep;
            float vx = (float)Math.sin(angle) * 40.0f;
            float vy = (float)Math.cos(angle) * 40.0f;
            SimpleBullet bullet = new SimpleBullet(x, y, vx, vy, 3.0f, Color.YELLOW);
            bullet.setGameCanvas(gameCanvas);
            bullet.setDamage(bulletDamage - 1);
            gameCanvas.addBullet(bullet);
        }
    }
}
```

### 示例2：动态子机布局

```java
public class DynamicPlayer extends Player {
    private int powerLevel = 1;
    
    public DynamicPlayer(float spawnX, float spawnY) {
        super(spawnX, spawnY);
    }
    
    public void updateOptions() {
        clearOptions();
        
        // 根据能量等级调整子机数量
        int optionCount = Math.min(powerLevel, 4);
        
        for (int i = 0; i < optionCount; i++) {
            float offsetX;
            float offsetY;
            
            if (optionCount == 1) {
                offsetX = 0;
                offsetY = 15;
            } else if (optionCount == 2) {
                offsetX = (i == 0) ? -25 : 25;
                offsetY = 10;
            } else if (optionCount == 3) {
                if (i == 0) {
                    offsetX = -30; offsetY = 10;
                } else if (i == 1) {
                    offsetX = 30; offsetY = 10;
                } else {
                    offsetX = 0; offsetY = 25;
                }
            } else {
                // 4个子机：菱形布局
                if (i == 0) {
                    offsetX = -30; offsetY = 10;
                } else if (i == 1) {
                    offsetX = 30; offsetY = 10;
                } else if (i == 2) {
                    offsetX = -15; offsetY = 25;
                } else {
                    offsetX = 15; offsetY = 25;
                }
            }
            
            MyOption option = new MyOption(this, offsetX, offsetY, gameCanvas);
            addOption(option);
        }
    }
    
    public void setPowerLevel(int level) {
        this.powerLevel = level;
        updateOptions();
    }
}
```

### 示例3：子机升级系统

```java
public class UpgradeableOption extends Option {
    private int upgradeLevel = 0;
    
    public UpgradeableOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
        super(player, offsetX, offsetY, gameCanvas);
        setShootInterval(3);
        setBulletDamage(1);
    }
    
    public void upgrade() {
        upgradeLevel++;
        
        // 升级效果
        setBulletDamage(bulletDamage + 1);
        setShootInterval(Math.max(1, shootInterval - 1));
        
        System.out.println("子机升级到等级 " + upgradeLevel);
    }
    
    @Override
    protected void shoot() {
        // 根据等级发射不同数量的子弹
        int bulletCount = 1 + upgradeLevel;
        float spreadAngle = 0.1f;
        
        for (int i = 0; i < bulletCount; i++) {
            float angle = (i - (bulletCount - 1) / 2.0f) * spreadAngle;
            float vx = (float)Math.sin(angle) * 50.0f;
            float vy = 50.0f;
            
            SimpleBullet bullet = new SimpleBullet(x, y, vx, vy, 4.0f, Color.ORANGE);
            bullet.setGameCanvas(gameCanvas);
            bullet.setDamage(bulletDamage);
            gameCanvas.addBullet(bullet);
        }
    }
}
```

## 集成到游戏系统

### 在 PlayerFactory 中注册自定义玩家

```java
import stg.game.player.PlayerType;

public class PlayerFactory {
    // ... 现有代码 ...
    
    public Player createPlayer(PlayerType type, float spawnX, float spawnY) {
        switch (type) {
            case REIMU:
                return createReimuPlayer(spawnX, spawnY);
            case MARISA:
                return createMarisaPlayer(spawnX, spawnY);
            case CUSTOM: // 新增自定义类型
                return new CustomPlayer(spawnX, spawnY);
            case DEFAULT:
            default:
                return new Player(spawnX, spawnY);
        }
    }
}
```

### 在 GameCanvas 中初始化子机

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
    } else if (player instanceof CustomPlayer) {
        ((CustomPlayer) player).initializeOptions(this);
    }
    
    // ... 其他初始化 ...
}
```

## 性能优化建议

1. **限制子机数量**：建议不超过4个子机
2. **优化射击间隔**：避免过快射击导致性能下降
3. **使用对象池**：对于频繁创建的子弹，考虑使用对象池
4. **简化渲染**：子机渲染尽量简单，避免复杂的图形操作

## 调试技巧

### 查看子机状态

```java
// 在 Option 子类中添加调试输出
@Override
public void update() {
    super.update();
    
    // 输出子机位置
    System.out.println("子机位置: (" + x + ", " + y + ")");
    System.out.println("目标位置: (" + targetX + ", " + targetY + ")");
}
```

### 可视化子机范围

```java
@Override
public void render(Graphics2D g) {
    super.render(g);
    
    // 绘制子机攻击范围（调试用）
    float screenX = x + gameCanvas.getWidth() / 2.0f;
    float screenY = gameCanvas.getHeight() / 2.0f - y;
    
    g.setColor(new Color(255, 255, 255, 50));
    g.drawOval((int)(screenX - 50), (int)(screenY - 50), 100, 100);
}
```

## 常见问题

### Q: 子机不跟随玩家？
A: 检查是否正确调用了 `player.addOption(option)` 和 `player.setGameCanvas(canvas)`。

### Q: 子机不发射子弹？
A: 确保：
1. 子机的 `shoot()` 方法已正确实现
2. `setShooting(true)` 被调用（通常由玩家自动同步）
3. 子弹正确添加到 gameCanvas

### Q: 子机位置不对？
A: 检查 `offsetX` 和 `offsetY` 的设置，以及 `followSpeed` 参数。

## 总结

子机系统为游戏增加了丰富的策略性和可玩性。通过继承 `Option` 类，你可以轻松创建各种类型的子机，实现不同的攻击模式和行为。参考东方正作的设计，子机系统可以极大地提升游戏体验。

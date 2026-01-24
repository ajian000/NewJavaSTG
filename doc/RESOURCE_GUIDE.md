# 资源管理使用指南

## 目录结构

```
resources/
├── images/           # 图片资源目录
│   ├── player.png    # 玩家图片
│   ├── enemy.png     # 敌人图片
│   ├── bullet.png    # 子弹图片
│   └── background.png # 背景图片
├── audio/            # 音频资源目录
│   ├── music/       # 背景音乐
│   │   ├── bgm_title.wav      # 标题画面音乐
│   │   ├── bgm_stage1.wav     # 第一关音乐
│   │   └── bgm_boss.wav      # Boss战音乐
│   └── sfx/         # 音效
│       ├── shoot.wav           # 射击音效
│       ├── explosion.wav       # 爆炸音效
│       ├── hit.wav            # 命中音效
│       └── powerup.wav        # 道具音效
└── data/             # 数据文件目录
    └── levels.json   # 关卡数据
```

## ResourceManager 使用方法

### 基本用法

```java
// 获取资源管理器实例
ResourceManager resourceManager = ResourceManager.getInstance();

// 加载图片（从 resources/ 目录）
BufferedImage playerImage = resourceManager.loadImage("player.png");

// 加载图片（从子目录）
BufferedImage enemyImage = resourceManager.loadImage("enemy.png", "images");

// 检查图片是否已加载
if (resourceManager.hasImage("player.png")) {
    BufferedImage image = resourceManager.getImage("player.png");
}

// 卸载单个图片
resourceManager.unloadImage("player.png");

// 清空所有图片
resourceManager.clearImages();
```

### 在游戏中使用

```java
// 在Player类中
public class Player {
    private BufferedImage playerImage;
    
    public Player() {
        ResourceManager resourceManager = ResourceManager.getInstance();
        playerImage = resourceManager.loadImage("player.png");
    }
    
    public void render(Graphics2D g) {
        if (playerImage != null) {
            g.drawImage(playerImage, x, y, null);
        }
    }
}
```

## AudioManager 使用方法

### 基本用法

```java
// 获取音频管理器实例
AudioManager audioManager = AudioManager.getInstance();

// 播放背景音乐（循环播放）
audioManager.playMusic("bgm_stage1.wav", true);

// 播放背景音乐（单次播放）
audioManager.playMusic("bgm_title.wav", false);

// 播放音效
audioManager.playSoundEffect("shoot.wav");
audioManager.playSoundEffect("explosion.wav");

// 停止音乐
audioManager.stopMusic();

// 暂停音乐
audioManager.pauseMusic();

// 恢复音乐
audioManager.resumeMusic();
```

### 音量控制

```java
// 设置音乐音量 (0.0 - 1.0)
audioManager.setMusicVolume(0.7f);

// 设置音效音量 (0.0 - 1.0)
audioManager.setSoundVolume(0.8f);

// 获取当前音量
float musicVolume = audioManager.getMusicVolume();
float soundVolume = audioManager.getSoundVolume();
```

### 资源管理

```java
// 检查音乐是否已加载
if (audioManager.isMusicLoaded("bgm_stage1.wav")) {
    // 音乐已加载
}

// 检查音效是否已加载
if (audioManager.isSoundEffectLoaded("shoot.wav")) {
    // 音效已加载
}

// 卸载单个音乐
audioManager.unloadMusic("bgm_stage1.wav");

// 卸载单个音效
audioManager.unloadSoundEffect("shoot.wav");

// 清空所有音乐
audioManager.clearMusic();

// 清空所有音效
audioManager.clearSoundEffects();

// 清空所有音频资源
audioManager.clearAll();
```

### 在游戏中使用

```java
// 在GameCanvas类中
public class GameCanvas {
    private AudioManager audioManager;
    
    public GameCanvas() {
        audioManager = AudioManager.getInstance();
    }
    
    // 玩家射击时播放音效
    public void onPlayerShoot() {
        audioManager.playSoundEffect("shoot.wav");
    }
    
    // 敌人死亡时播放音效
    public void onEnemyDeath() {
        audioManager.playSoundEffect("explosion.wav");
    }
    
    // 关卡开始时播放音乐
    public void onStageStart() {
        audioManager.playMusic("bgm_stage1.wav", true);
    }
    
    // 游戏暂停时暂停音乐
    public void onGamePause() {
        audioManager.pauseMusic();
    }
    
    // 游戏恢复时恢复音乐
    public void onGameResume() {
        audioManager.resumeMusic();
    }
}
```

## 支持的音频格式

Java Sound API 支持的音频格式：
- WAV (推荐)
- AU
- AIFF
- MIDI

**注意**：MP3 格式需要额外的库支持，建议使用 WAV 格式。

## 支持的图片格式

Java ImageIO 支持的图片格式：
- PNG (推荐，支持透明度)
- JPEG
- GIF
- BMP

**注意**：对于游戏精灵，建议使用 PNG 格式以支持透明背景。

## 性能优化建议

1. **预加载资源**：在游戏开始前加载所有需要的资源
2. **资源复用**：使用 ResourceManager 的缓存机制，避免重复加载
3. **及时释放**：关卡结束时释放不再需要的资源
4. **音频格式**：使用压缩的 WAV 格式以减少文件大小
5. **图片格式**：使用适当的图片尺寸，避免过大的图片文件

## 常见问题

### Q: 图片加载失败怎么办？
A: 检查文件路径是否正确，确保图片文件在 resources/ 目录下。

### Q: 音频播放没有声音？
A: 检查音量设置，确保系统音量已开启。

### Q: 如何自定义资源路径？
A: 使用 `setResourcePath()` 方法设置自定义路径：
```java
resourceManager.setResourcePath("custom_resources/");
audioManager.setResourcePath("custom_resources/audio/");
```

### Q: 如何处理资源加载失败？
A: 资源管理器会在加载失败时输出错误信息，可以添加备用资源：
```java
BufferedImage image = resourceManager.loadImage("player.png");
if (image == null) {
    // 使用备用图片或绘制默认图形
    image = createDefaultPlayerImage();
}
```

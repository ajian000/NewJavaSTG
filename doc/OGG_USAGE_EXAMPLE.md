# OGG 音频支持使用示例

## 基本使用

```java
import stg.util.AudioManager;
import stg.util.OGGAudioSupport;

public class GameAudioExample {
    private AudioManager audioManager;
    
    public GameAudioExample() {
        audioManager = AudioManager.getInstance();
    }
    
    public void playBackgroundMusic() {
        // 检查是否支持 OGG
        if (OGGAudioSupport.supportsOGG()) {
            // 使用 OGG 格式播放
            OGGAudioSupport.playOGGMusic(
                audioManager,
                "resources/audio/",
                "bgm_stage1.ogg",
                true,
                0.7f
            );
        } else {
            // 使用 WAV 格式播放
            audioManager.playMusic("bgm_stage1.wav", true);
        }
    }
    
    public void playShootSound() {
        if (OGGAudioSupport.supportsOGG()) {
            OGGAudioSupport.playOGGSoundEffect(
                audioManager,
                "resources/audio/",
                "shoot.ogg",
                0.8f
            );
        } else {
            audioManager.playSoundEffect("shoot.wav");
        }
    }
}
```

## 在游戏中使用

### 1. 关卡开始时播放音乐

```java
public class GameCanvas {
    private AudioManager audioManager;
    
    public void onStageStart() {
        audioManager = AudioManager.getInstance();
        
        if (OGGAudioSupport.supportsOGG()) {
            OGGAudioSupport.playOGGMusic(
                audioManager,
                "resources/audio/",
                "bgm_stage1.ogg",
                true,
                0.7f
            );
        } else {
            audioManager.playMusic("bgm_stage1.wav", true);
        }
    }
}
```

### 2. 玩家射击时播放音效

```java
public class Player {
    public void shoot() {
        // 发射子弹
        gameCanvas.addBullet(bullet);
        
        // 播放音效
        AudioManager audioManager = AudioManager.getInstance();
        if (OGGAudioSupport.supportsOGG()) {
            OGGAudioSupport.playOGGSoundEffect(
                audioManager,
                "resources/audio/",
                "shoot.ogg",
                0.8f
            );
        } else {
            audioManager.playSoundEffect("shoot.wav");
        }
    }
}
```

### 3. 敌人死亡时播放音效

```java
public class Enemy {
    public void onDeath() {
        AudioManager audioManager = AudioManager.getInstance();
        
        if (OGGAudioSupport.supportsOGG()) {
            OGGAudioSupport.playOGGSoundEffect(
                audioManager,
                "resources/audio/",
                "explosion.ogg",
                0.8f
            );
        } else {
            audioManager.playSoundEffect("explosion.wav");
        }
    }
}
```

## 自动选择格式

创建一个统一的音频播放工具类：

```java
package stg.util;

/**
 * 统一音频播放工具 - 自动选择 OGG 或 WAV
 * @Time 2026-01-24
 */
public class AudioPlayer {
    private AudioManager audioManager;
    private boolean useOGG;
    
    public AudioPlayer() {
        this.audioManager = AudioManager.getInstance();
        this.useOGG = OGGAudioSupport.supportsOGG();
    }
    
    public void playMusic(String filename, boolean loop) {
        if (useOGG && filename.endsWith(".ogg")) {
            OGGAudioSupport.playOGGMusic(
                audioManager,
                "resources/audio/",
                filename,
                loop,
                audioManager.getMusicVolume()
            );
        } else {
            audioManager.playMusic(filename, loop);
        }
    }
    
    public void playSoundEffect(String filename) {
        if (useOGG && filename.endsWith(".ogg")) {
            OGGAudioSupport.playOGGSoundEffect(
                audioManager,
                "resources/audio/",
                filename,
                audioManager.getSoundVolume()
            );
        } else {
            audioManager.playSoundEffect(filename);
        }
    }
    
    public boolean supportsOGG() {
        return useOGG;
    }
}
```

使用示例：

```java
AudioPlayer audioPlayer = new AudioPlayer();

// 自动选择格式播放音乐
audioPlayer.playMusic("bgm_stage1.ogg", true);
audioPlayer.playMusic("bgm_stage1.wav", true);

// 自动选择格式播放音效
audioPlayer.playSoundEffect("shoot.ogg");
audioPlayer.playSoundEffect("shoot.wav");
```

## 文件命名规范

### 推荐命名

```
音乐文件：
- bgm_title.ogg/wav      - 标题画面
- bgm_stage1.ogg/wav     - 第一关
- bgm_stage2.ogg/wav     - 第二关
- bgm_boss.ogg/wav       - Boss战
- bgm_clear.ogg/wav      - 通关
- bgm_gameover.ogg/wav    - 游戏结束

音效文件：
- shoot.ogg/wav          - 射击
- explosion.ogg/wav       - 爆炸
- hit.ogg/wav            - 命中
- enemy_shoot.ogg/wav     - 敌人射击
- item_get.ogg/wav        - 获取道具
- damage.ogg/wav          - 受伤
- powerup.ogg/wav         - 强化
```

## 性能优化

### 1. 预加载音频

```java
public class Game {
    private AudioPlayer audioPlayer;
    
    public void initialize() {
        audioPlayer = new AudioPlayer();
        
        // 预加载音乐
        audioPlayer.playMusic("bgm_stage1.ogg", false);
        audioPlayer.stopMusic();
        
        // 预加载常用音效
        audioPlayer.playSoundEffect("shoot.ogg");
        audioPlayer.playSoundEffect("explosion.ogg");
    }
}
```

### 2. 使用合适的格式

- **背景音乐**：使用 OGG（文件小）
- **音效**：使用 WAV（加载快）
- **混合使用**：根据场景选择

### 3. 音量控制

```java
// 设置全局音量
audioManager.setMusicVolume(0.7f);
audioManager.setSoundVolume(0.8f);

// 暂停/恢复音乐
audioManager.pauseMusic();
audioManager.resumeMusic();
```

## 常见问题

### Q: 如何知道是否支持 OGG？

```java
if (OGGAudioSupport.supportsOGG()) {
    System.out.println("OGG 格式已支持");
} else {
    System.out.println("OGG 格式未支持，将使用 WAV");
}
```

### Q: OGG 和 WAV 可以混用吗？

A: 可以！AudioPlayer 类会自动选择合适的格式。

### Q: 如何批量转换音频？

A: 使用 FFmpeg：
```bash
# OGG 转 WAV
ffmpeg -i input.ogg -acodec pcm_s16le -ar 44100 output.wav

# WAV 转 OGG
ffmpeg -i input.wav -acodec libvorbis -aq 6 output.ogg
```

## 总结

- **OGGAudioSupport** 提供了 OGG 格式支持
- **AudioManager** 提供了 WAV 等原生格式支持
- **AudioPlayer** 提供了统一的播放接口
- 自动检测 OGG 库是否安装
- 自动选择合适的音频格式
- 支持音乐和音效播放
- 支持音量控制

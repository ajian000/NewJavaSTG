# OGG 音频支持指南

## 概述

Java 原生不支持 OGG Vorbis 格式，需要添加 JOrbis 库来实现 OGG 支持。

## 安装 JOrbis 库

### 方法一：使用 Maven

在 `pom.xml` 中添加依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.jcraft</groupId>
        <artifactId>jorbis</artifactId>
        <version>0.0.17</version>
    </dependency>
</dependencies>
```

### 方法二：使用 Gradle

在 `build.gradle` 中添加依赖：

```gradle
dependencies {
    implementation 'org.jcraft:jorbis:0.0.17'
}
```

### 方法三：手动下载 JAR

1. 下载 JOrbis JAR：https://github.com/jcraft/jorbis/releases
2. 将 JAR 文件放入 `lib/` 目录
3. 添加到 classpath：
   ```bash
   javac -cp lib/jorbis-0.0.17.jar -d bin src/**/*.java
   java -cp bin:lib/jorbis-0.0.17.jar Main.Main
   ```

## 使用 EnhancedAudioManager

### 基本用法

```java
import stg.util.EnhancedAudioManager;

public class Game {
    private EnhancedAudioManager audioManager;
    
    public Game() {
        audioManager = new EnhancedAudioManager();
        
        // 检查 OGG 支持
        if (audioManager.supportsOGG()) {
            System.out.println("OGG 格式已支持");
        } else {
            System.out.println("OGG 格式未支持，请安装 JOrbis 库");
        }
    }
    
    public void playMusic() {
        // 播放 OGG 背景音乐
        audioManager.playOGGMusic("bgm_stage1.ogg", true);
    }
    
    public void playSound() {
        // 播放 OGG 音效
        audioManager.playOGGSoundEffect("shoot.ogg");
    }
}
```

### 播放 OGG 音乐

```java
// 播放背景音乐（循环）
audioManager.playOGGMusic("bgm_stage1.ogg", true);

// 播放背景音乐（单次）
audioManager.playOGGMusic("bgm_title.ogg", false);

// 停止音乐
audioManager.stopMusic();

// 暂停音乐
audioManager.pauseMusic();

// 恢复音乐
audioManager.resumeMusic();
```

### 播放 OGG 音效

```java
// 播放射击音效
audioManager.playOGGSoundEffect("shoot.ogg");

// 播放爆炸音效
audioManager.playOGGSoundEffect("explosion.ogg");

// 播放命中音效
audioManager.playOGGSoundEffect("hit.ogg");
```

### 音量控制

```java
// 设置音乐音量（0.0 - 1.0）
audioManager.setMusicVolume(0.7f);

// 设置音效音量（0.0 - 1.0）
audioManager.setSoundVolume(0.8f);

// 获取当前音量
float musicVolume = audioManager.getMusicVolume();
float soundVolume = audioManager.getSoundVolume();
```

## 支持的音频格式

使用 `getSupportedFormats()` 方法查看支持的格式：

```java
String[] formats = audioManager.getSupportedFormats();
for (String format : formats) {
    System.out.println(format);
}
```

输出示例：
```
WAV (原生支持)
AU (原生支持)
AIFF (原生支持)
OGG (已支持)
```

## OGG vs WAV 对比

| 特性 | OGG | WAV |
|------|------|-----|
| 文件大小 | 小（压缩） | 大（未压缩） |
| 音质 | 好（有损压缩） | 优秀（无损） |
| 兼容性 | 需要额外库 | 原生支持 |
| 加载速度 | 稍慢 | 快 |
| 适用场景 | 背景音乐 | 音效、快速加载 |

## 推荐使用场景

### 使用 OGG 格式
- 背景音乐（文件较大）
- 需要节省存储空间
- 音质要求不是极高

### 使用 WAV 格式
- 音效（需要快速加载）
- 需要最高音质
- 兼容性要求高

## 文件命名规范

### 背景音乐
```
bgm_title.ogg      - 标题画面音乐
bgm_stage1.ogg    - 第一关音乐
bgm_stage2.ogg    - 第二关音乐
bgm_boss.ogg      - Boss战音乐
bgm_clear.ogg     - 通关音乐
bgm_gameover.ogg  - 游戏结束音乐
```

### 音效
```
shoot.ogg         - 射击音效
explosion.ogg     - 爆炸音效
hit.ogg           - 命中音效
enemy_shoot.ogg   - 敌人射击音效
item_get.ogg      - 获取道具音效
damage.ogg        - 受伤音效
powerup.ogg       - 强化音效
```

## 错误处理

### OGG 库未安装

```
【音频加载失败】OGG 支持库未安装
```

**解决方案**：安装 JOrbis 库

### 文件不存在

```
【音频加载失败】文件不存在: resources/audio/music/bgm_stage1.ogg
```

**解决方案**：检查文件路径和文件名

### 格式不支持

```
【音频播放失败】无法播放 OGG: bgm_stage1.ogg
```

**解决方案**：确认文件是有效的 OGG 格式

## 性能优化建议

1. **预加载音频**
   ```java
   // 在游戏开始时预加载所有音频
   audioManager.loadMusic("bgm_stage1.ogg");
   audioManager.loadSoundEffect("shoot.ogg");
   ```

2. **使用合适的采样率**
   - 背景音乐：44.1kHz
   - 音效：22.05kHz 或 44.1kHz

3. **控制文件大小**
   - 背景音乐：< 10MB
   - 音效：< 500KB

4. **使用流式播放**（大文件）
   - 对于非常大的音频文件，考虑使用 SourceDataLine 而非 Clip

## 测试代码

```java
import stg.util.EnhancedAudioManager;

public class OGGTest {
    public static void main(String[] args) {
        EnhancedAudioManager audioManager = new EnhancedAudioManager();
        
        System.out.println("=== OGG 音频支持测试 ===\n");
        
        // 检查 OGG 支持
        System.out.println("1. 检查 OGG 支持");
        if (audioManager.supportsOGG()) {
            System.out.println("   ✓ OGG 格式已支持");
        } else {
            System.out.println("   ✗ OGG 格式未支持");
            System.out.println("   提示：请安装 JOrbis 库");
        }
        
        // 显示支持的格式
        System.out.println("\n2. 支持的音频格式");
        String[] formats = audioManager.getSupportedFormats();
        for (String format : formats) {
            System.out.println("   - " + format);
        }
        
        // 测试播放（如果有 OGG 文件）
        System.out.println("\n3. 测试播放");
        System.out.println("   提示：将 OGG 文件放在 resources/audio/ 目录下");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
```

## 常见问题

### Q: 为什么 OGG 文件无法播放？
A: 
1. 确认已安装 JOrbis 库
2. 检查文件路径是否正确
3. 确认文件是有效的 OGG 格式

### Q: OGG 和 WAV 哪个更好？
A: 
- OGG：文件小，适合背景音乐
- WAV：兼容性好，适合音效
- 建议：混合使用，根据场景选择

### Q: 如何批量转换 OGG 到 WAV？
A: 使用 FFmpeg：
```bash
ffmpeg -i input.ogg -acodec pcm_s16le -ar 44100 output.wav
```

### Q: OGG 音质会损失吗？
A: OGG Vorbis 是有损压缩，但在合理比特率下音质损失很小。对于游戏音频，通常可以接受。

## 总结

- **EnhancedAudioManager** 提供了完整的 OGG 支持
- 继承自 **AudioManager**，保持原有功能
- 自动检测 OGG 库是否安装
- 提供统一的 API 接口
- 支持音乐和音效的 OGG 格式播放

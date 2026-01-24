# 音效资源占位文件

请将游戏音效文件放在此目录下。

## 推荐音效文件

- `shoot.wav` - 玩家射击音效
- `explosion.wav` - 爆炸音效
- `hit.wav` - 命中音效
- `enemy_shoot.wav` - 敌人射击音效
- `item_get.wav` - 获取道具音效
- `damage.wav` - 受伤音效
- `powerup.wav` - 强化音效

## 音频格式

支持的格式：WAV（推荐）、AU、AIFF、MIDI

## 使用示例

```java
AudioManager audioManager = AudioManager.getInstance();

// 播放音效
audioManager.playSoundEffect("shoot.wav");
audioManager.playSoundEffect("explosion.wav");
audioManager.playSoundEffect("hit.wav");
```

## 音量控制

```java
// 设置音效音量（0.0 - 1.0）
audioManager.setSoundVolume(0.8f);
```

## 音效特点

- 音效不循环播放
- 多个音效可以同时播放
- 音效与背景音乐音量独立控制

# 背景音乐资源占位文件

请将游戏背景音乐文件放在此目录下。

## 推荐音乐文件

- `bgm_title.wav` - 标题画面音乐
- `bgm_stage1.wav` - 第一关音乐
- `bgm_stage2.wav` - 第二关音乐
- `bgm_boss.wav` - Boss战音乐

## 音频格式

支持的格式：WAV（推荐）、AU、AIFF、MIDI

## 使用示例

```java
AudioManager audioManager = AudioManager.getInstance();

// 播放背景音乐（循环播放）
audioManager.playMusic("bgm_stage1.wav", true);

// 停止音乐
audioManager.stopMusic();

// 暂停音乐
audioManager.pauseMusic();

// 恢复音乐
audioManager.resumeMusic();
```

## 音量控制

```java
// 设置音乐音量（0.0 - 1.0）
audioManager.setMusicVolume(0.7f);
```

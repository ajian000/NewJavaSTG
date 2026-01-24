# 资源目录结构说明

本游戏使用以下目录结构来组织资源文件：

## 目录结构

```
JavaSTG/
├── resources/              # 资源根目录
│   ├── images/           # 图片资源
│   │   ├── player.png    # 玩家图片
│   │   ├── enemy.png     # 敌人图片
│   │   ├── bullet.png    # 子弹图片
│   │   └── background.png # 背景图片
│   ├── audio/            # 音频资源
│   │   ├── music/       # 背景音乐
│   │   │   ├── bgm_title.wav
│   │   │   ├── bgm_stage1.wav
│   │   │   └── bgm_boss.wav
│   │   └── sfx/         # 音效
│   │       ├── shoot.wav
│   │       ├── explosion.wav
│   │       ├── hit.wav
│   │       └── powerup.wav
│   └── data/             # 数据文件
│       └── levels.json
└── src/                  # 源代码
```

## 使用方法

### 加载图片

```java
ResourceManager resourceManager = ResourceManager.getInstance();

// 从 resources/ 加载
BufferedImage playerImage = resourceManager.loadImage("player.png");

// 从 resources/images/ 加载
BufferedImage enemyImage = resourceManager.loadImage("enemy.png", "images");
```

### 加载音频

```java
AudioManager audioManager = AudioManager.getInstance();

// 播放背景音乐
audioManager.playMusic("bgm_stage1.wav", true);

// 播放音效
audioManager.playSoundEffect("shoot.wav");
```

## 支持的格式

### 图片格式
- PNG (推荐，支持透明度)
- JPEG
- GIF
- BMP

### 音频格式
- WAV (推荐)
- AU
- AIFF
- MIDI

## 注意事项

1. 资源文件必须放在 `resources/` 目录下
2. 音频文件建议使用 WAV 格式以确保兼容性
3. 图片文件建议使用 PNG 格式以支持透明背景
4. 资源文件名区分大小写
5. 资源会被缓存，重复加载同一文件不会重复读取

## 示例代码

详细的使用示例请参考：
- [ResourceManager.java](../src/stg/util/ResourceManager.java)
- [AudioManager.java](../src/stg/util/AudioManager.java)
- [ResourceLoaderExample.java](../src/stg/util/ResourceLoaderExample.java)
- [RESOURCE_GUIDE.md](RESOURCE_GUIDE.md)

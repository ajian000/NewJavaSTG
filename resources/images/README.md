# 图片资源占位文件

请将游戏图片文件放在此目录下。

## 推荐图片文件

- `player.png` - 玩家角色图片
- `enemy.png` - 敌人图片
- `bullet.png` - 子弹图片
- `item.png` - 道具图片
- `background.png` - 背景图片

## 图片格式

支持的格式：PNG（推荐）、JPEG、GIF、BMP

## 使用示例

```java
ResourceManager resourceManager = ResourceManager.getInstance();

// 从 images/ 目录加载图片
BufferedImage playerImage = resourceManager.loadImage("player.png", "images");
BufferedImage enemyImage = resourceManager.loadImage("enemy.png", "images");
```

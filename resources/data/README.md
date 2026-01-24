# 数据文件占位文件

请将游戏数据文件放在此目录下。

## 推荐数据文件

- `levels.json` - 关卡配置文件
- `enemies.json` - 敌人数据配置
- `bullets.json` - 子弹数据配置
- `items.json` - 道具数据配置
- `settings.json` - 游戏设置

## 数据格式

推荐使用 JSON 格式，易于解析和编辑。

## 关卡配置示例

```json
{
  "levelName": "Stage 1",
  "totalFrames": 7200,
  "waves": [
    {
      "startFrame": 0,
      "endFrame": 1200,
      "enemies": [
        {
          "type": "BasicEnemy",
          "spawnX": -200,
          "spawnY": 300,
          "moveSpeed": 2.0,
          "spawnFrame": 60
        }
      ]
    }
  ]
}
```

## 使用示例

```java
// 读取关卡配置
String levelData = Files.readString(Paths.get("resources/data/levels.json"));
JSONObject levelConfig = new JSONObject(levelData);

// 解析配置
String levelName = levelConfig.getString("levelName");
int totalFrames = levelConfig.getInt("totalFrames");
```

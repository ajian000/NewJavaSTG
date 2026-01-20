# 关卡脚本系统使用指南

## 概述

本项目支持使用JavaScript或Python脚本来自定义关卡,实现关卡制作的灵活性。

**重要**: 所有关卡脚本必须放置在`src/user/`目录下,并命名为`index.js`或`index.py`。

## 架构

```
src/
├── user/
│   ├── index.js          # JavaScript关卡定义(使用时选择此文件)
│   └── index.py          # Python关卡定义(使用时选择此文件)
└── stg/util/
    ├── LevelLoader.java         # 关卡加载器接口
    ├── LevelData.java          # 关卡数据结构
    ├── LevelManager.java       # 关卡管理器
    ├── JavaScriptLevelLoader.java # JavaScript实现
    └── PythonLevelLoader.java   # Python实现
```

## 使用JavaScript创建关卡

### 1. 在src/user/目录下创建index.js

```javascript
function createLevel() {
    return {
        name: "Level 1 - Introduction",
        enemies: [
            {
                type: "BasicEnemy",
                x: 0,
                y: 200,
                speed: 2.0,
                frame: 60  // 在第60帧生成
            }
        ],
        events: [
            {
                type: "message",
                frame: 300,
                content: "Boss approaching!"
            }
        ]
    };
}

createLevel();
```

### 2. 在Java代码中加载

```java
LevelManager levelManager = LevelManager.getInstance();
levelManager.setScriptLanguage("javascript");
LevelData level = levelManager.loadLevelFromUser();  // 自动从src/user/index.js加载
```

## 使用Python创建关卡

### 1. 在src/user/目录下创建index.py

```python
#!/usr/bin/env python3
import json

def create_level():
    level_data = {
        "name": "Level 1 - Introduction",
        "enemies": [
            {
                "type": "BasicEnemy",
                "x": 0,
                "y": 200,
                "speed": 2.0,
                "frame": 60
            }
        ],
        "events": [
            {
                "type": "message",
                "frame": 300,
                "content": "Boss approaching!"
            }
        ]
    }
    
    print(json.dumps(level_data))

if __name__ == "__main__":
    create_level()
```

### 2. 在Java代码中加载

```java
LevelManager levelManager = LevelManager.getInstance();
levelManager.setScriptLanguage("python");
LevelData level = levelManager.loadLevelFromUser();  // 自动从src/user/index.py加载
```

## 关卡数据结构

### LevelData

- `name`: 关卡名称 (String)
- `enemies`: 敌人生成列表
- `events`: 关卡事件列表

### EnemySpawnData

- `type`: 敌人类型 (String) - 例如 "BasicEnemy"
- `x`: 初始X坐标 (float)
- `y`: 初始Y坐标 (float)
- `speed`: 移动速度 (float)
- `frame`: 生成帧数 (int)
- `params`: 自定义参数 (Map<String, Object>)

## 高级用法

### 动态计算

可以使用脚本进行动态计算:

```javascript
function createLevel() {
    return {
        name: "Dynamic Level",
        enemies: [
            // 使用表达式计算位置
            {
                type: "BasicEnemy",
                x: Math.random() * 200 - 100,  // -100到100随机
                y: 200,
                speed: 2.0,
                frame: 60
            }
        ]
    };
}
```

### 条件判断

```javascript
function createLevel() {
    var difficulty = "hard"; // 可以从外部传入
    
    var enemyCount = difficulty === "hard" ? 10 : 5;
    var enemies = [];
    
    for (var i = 0; i < enemyCount; i++) {
        enemies.push({
            type: "BasicEnemy",
            x: (i - enemyCount/2) * 50,
            y: 200,
            speed: 2.0,
            frame: 60 + i * 30
        });
    }
    
    return {
        name: "Dynamic Level",
        enemies: enemies
    };
}
```

## 运行示例

```bash
# 编译
javac src/stg/util/LevelExample.java

# 运行示例
java stg.util.LevelExample
```

## 注意事项

1. **JavaScript引擎**: 项目使用Nashorn引擎(Java 8+)
   - 如果使用Java 11+,Nashorn已被移除,需要使用GraalVM或其他JS引擎
   
2. **Python依赖**: Python加载器需要系统安装Python
   - 支持python、python3、py等命令
   - Python脚本必须输出JSON格式数据

3. **路径问题**: 脚本文件使用相对路径时,确保工作目录正确

4. **错误处理**: 脚本执行失败时会输出错误信息,不会中断程序

## 扩展开发

要添加新的敌人类型,需要:

1. 在`stg.game`包中创建新的Enemy子类
2. 在脚本中使用对应的type名称
3. 在GameCanvas中添加类型映射和实例化逻辑

示例:
```java
// 在脚本中
{
    type: "AdvancedEnemy",
    x: 0,
    y: 200,
    speed: 5.0,
    frame: 60
}
```

## 性能考虑

- 建议预加载关卡,避免运行时解析
- 脚本执行有性能开销,避免在循环中频繁调用
- 考虑使用JSON文件而非脚本,如果不需要动态计算

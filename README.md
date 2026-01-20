# NewJavaSTG

基于Java开发的弹幕射击游戏引擎

## 项目简介

NewJavaSTG是一个使用Java Swing框架开发的弹幕射击(STG)游戏引擎,提供基础的游戏循环、渲染系统、输入处理等功能,是作为游戏引擎开发的学习项目。

经过两年的沉淀与积累,我们在原有基础上重新审视、重构,以更成熟的技术视角和更清晰的架构设计重启了本项目。这两年间,"我们"不断学习游戏开发相关知识,深入理解引擎架构,如今带着全新的思路和更扎实的代码功底,重新踏上STG游戏引擎的开发之路。

## 功能特性

- **三面板布局**: 窗口分为左中右三个面板,比例为1:1.5:1
  - 左侧面板: 信息显示和虚拟键盘
  - 中间面板: 游戏主区域
  - 右侧面板: 信息显示区域

- **标题界面**:
  - 主菜单: 开始游戏、退出游戏
  - 角色选择: 灵梦、魔理沙
  - 动画效果和颜色预览

- **玩家系统**:
  - 键盘控制移动(上下左右方向键)
  - Z键发射子弹
  - Shift键切换低速模式
  - 多角色支持(灵梦、魔理沙)
  - 同时按下相反方向键时保持静止

- **子弹系统**:
  - 多种子弹类型(圆形、曲线、扇形)
  - 自动清理越界子弹
  - 低速模式发射更大子弹
  - 敌方子弹系统

- **敌人系统**:
  - 敌人AI
  - 波次管理
  - 关卡加载(JSON/JS/Python)

- **暂停菜单**:
  - ESC键暂停/恢复
  - 继续游戏、重新开始、返回主菜单

- **渲染系统**:
  - 双缓冲渲染
  - 抗锯齿支持
  - 60FPS游戏循环
  - 中心原点坐标系

- **数学工具**:
  - 2D向量运算
  - 碰撞检测
  - 随机数生成
  - 角度/弧度转换

## 项目结构

```
JavaSTG/
├── src/
│   ├── Main/
│   │   └── Main.java              # 程序入口
│   └── stg/
│       ├── base/                   # 基础组件
│       │   ├── Window.java          # 主窗口类
│       │   └── VirtualKeyboardPanel.java  # 虚拟键盘面板
│       ├── game/                  # 游戏核心
│       │   ├── GameLoop.java       # 游戏循环
│       │   ├── ui/               # 用户界面
│       │   │   ├── GameCanvas.java     # 游戏画布
│       │   │   └── TitleScreen.java    # 标题界面
│       │   ├── player/            # 玩家系统
│       │   │   ├── Player.java         # 玩家基类
│       │   │   ├── PlayerFactory.java   # 玩家工厂
│       │   │   ├── PlayerType.java     # 玩家类型枚举
│       │   │   ├── ReimuPlayer.java    # 灵梦玩家
│       │   │   └── MarisaPlayer.java   # 魔理沙玩家
│       │   ├── bullet/            # 子弹系统
│       │   │   ├── Bullet.java         # 子弹基类
│       │   │   ├── CircularBullet.java  # 圆形弹
│       │   │   ├── CurvingBullet.java   # 曲线弹
│       │   │   └── SpreadBullet.java    # 扇形弹
│       │   └── enemy/             # 敌人系统
│       │       ├── Enemy.java         # 敌人基类
│       │       ├── EnemyBullet.java   # 敌方子弹
│       │       └── BasicEnemy.java    # 基础敌人
│       └── util/                   # 工具类
│           ├── CoordinateSystem.java # 坐标系统
│           ├── LevelData.java      # 关卡数据
│           ├── LevelManager.java   # 关卡管理器
│           ├── EnemySpawnData.java # 敌人生成数据
│           ├── LevelLoader.java    # 关卡加载器接口
│           ├── JsonLevelLoader.java   # JSON关卡加载
│           ├── JavaScriptLevelLoader.java # JS关卡加载
│           ├── PythonLevelLoader.java   # Python关卡加载
│           ├── SimpleJsonLoader.java   # 简易JSON加载
│           └── math/             # 数学工具
│               ├── Vector2.java         # 2D向量类
│               ├── MathUtils.java       # 数学工具类
│               └── RandomGenerator.java # 随机数生成器
├── bin/                           # 编译输出目录
└── README.md
```

## 操作说明

|| 按键 | 功能 |
||------|------|
|| ↑ ↓ | 切换菜单选项 |
|| ← → | 切换角色 |
|| Z/Enter | 确认选择 |
|| ESC | 返回/暂停 |
|| ↑ | 向上移动 |
|| ↓ | 向下移动 |
|| ← | 向左移动 |
|| → | 向右移动 |
|| Z | 发射子弹 |
|| Shift | 低速模式 |
|| X | 预留按键 |

## 编译运行

### 编译项目
```bash
javac -encoding UTF-8 -d bin -sourcepath src src/Main/Main.java src/stg/**/*.java
```

### 运行游戏
```bash
java -cp bin Main.Main
```

## 技术栈

- **语言**: Java
- **GUI框架**: Swing
- **渲染**: Java 2D API
- **版本控制**: Git

## 开发计划

- [x] 添加敌人系统
- [x] 实现多种子弹模式
- [x] 添加碰撞检测
- [ ] 实现Boss战系统
- [ ] 添加音效和背景音乐
- [x] 实现关卡系统

## 后续优化方向

随着项目的发展,"我们"计划引入更先进的渲染框架来提升游戏性能和视觉效果。目前主要使用Java Swing和Java 2D API,虽然足以应对基础需求,但在大规模弹幕、复杂特效和高帧率渲染方面仍有提升空间。

**计划探索的渲染方案**:
- LWJGL (Lightweight Java Game Library) - OpenGL绑定
- LibGDX - 跨平台游戏开发框架
- jMonkeyEngine - Java 3D游戏引擎(可降维用于2D)

由于"我们"在这些现代渲染框架方面缺乏技术背景和实践经验,恳请有相关经验的高人不吝赐教,提供技术指导和最佳实践建议。任何关于渲染框架选型、架构设计、性能优化等方面的帮助都将不胜感激。

## 许可证

MIT License

## 作者

NewJavaSTG开发团队

---

*这是一个学习项目,欢迎提出建议和改进意见,期待更多志同道合的伙伴加入"我们"*

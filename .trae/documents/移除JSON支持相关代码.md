# 移除JSON支持相关代码计划

## 分析现状

经过代码分析，我发现以下与JSON支持相关的文件和代码：

1. **核心JSON加载器类**：
   - `src/stg/util/script/JsonLevelLoader.java` - JSON关卡加载器（委托给SimpleJsonLoader）
   - `src/stg/util/script/SimpleJsonLoader.java` - 简易JSON解析器，包含完整的JSON解析逻辑

2. **使用JSON加载器的文件**：
   - `src/stg/util/LevelManager.java` - 关卡管理器，默认使用JSON加载器
   - `src/stg/game/stage/Stage.java` - 关卡类，直接使用JsonLevelLoader加载关卡数据

3. **依赖关系**：
   - `LevelLoader` 接口定义了加载关卡和执行脚本的方法
   - `LevelData` 类存储关卡数据，不直接依赖JSON

## 移除计划

### 步骤1：移除JSON加载器类
- 删除 `src/stg/util/script/JsonLevelLoader.java`
- 删除 `src/stg/util/script/SimpleJsonLoader.java`

### 步骤2：修改LevelManager.java
- 移除对JSON加载器的导入
- 移除 `setScriptLanguage` 方法中的JSON相关逻辑
- 修改构造函数，不再初始化JSON加载器
- 移除 `determineScriptFile` 方法中的JSON文件路径返回
- 考虑如何处理 `loadLevel` 和 `executeScript` 方法

### 步骤3：修改Stage.java
- 移除对JsonLevelLoader的导入
- 修改 `loadLevelData` 和 `loadLevelDataFromDefault` 方法，移除JSON加载逻辑

### 步骤4：调整LevelLoader接口（可选）
- 考虑是否需要修改LevelLoader接口，因为它原本设计为支持脚本和JSON

### 步骤5：清理相关文件和代码
- 检查是否有其他文件引用了JSON相关代码
- 清理不再使用的导入和方法

## 注意事项

1. **关卡加载功能**：移除JSON支持后，关卡加载功能将不可用。需要考虑是否需要提供替代方案，或者完全移除关卡加载功能。

2. **向后兼容性**：如果有其他代码依赖于现有的关卡加载功能，可能会受到影响。

3. **代码完整性**：确保移除JSON相关代码后，剩余的代码仍然能够正常编译和运行。
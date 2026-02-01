@echo off
chcp 65001 >nul
echo 正在编译JavaSTG项目...
echo.

cd /d "%~dp0"

echo 清理旧的编译文件...
if exist bin (
    del /Q /F /S bin\*.class 2>nul
)

echo 编译基础类...
javac -d bin -encoding UTF-8 -sourcepath src src/stg/util/math/*.java

javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/util/*.java

javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/obj/*.java

javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/bullet/*.java
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/item/*.java
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/laser/*.java
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/enemy/*.java
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/player/*.java
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/stage/*.java
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/event/*.java

echo 编译核心游戏类...
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/*.java

echo 编译基础UI类...
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/base/*.java

echo 编译game UI类...
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/stg/game/ui/*.java

echo 编译user包...
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/user/bullet/CircularBullet.java src/user/bullet/CurvingBullet.java src/user/bullet/PlayerTrackingBullet.java src/user/bullet/SimpleBullet.java src/user/bullet/SpiralBullet.java src/user/bullet/SpreadBullet.java
javac -d bin -cp "bin" -encoding UTF-8 src/user/enemy/EnemyBullet.java src/user/enemy/BasicEnemy.java src/user/enemy/SpiralEnemy.java src/user/enemy/TrackingEnemy.java src/user/enemy/OrbitEnemy.java src/user/enemy/RapidFireEnemy.java src/user/enemy/SpreadEnemy.java src/user/enemy/LaserShootingEnemy.java
javac -d bin -cp "bin" src/user/laser/Laser.java src/user/laser/EnemyLaser.java src/user/laser/CurvedLaser.java src/user/laser/EnemyCurvedLaser.java src/user/laser/LinearLaser.java src/user/laser/EnemyLinearLaser.java src/user/laser/SimpleLaser.java
javac -d bin -cp "bin" -encoding UTF-8 src/user/player/CustomOption.java src/user/player/CustomPlayer.java src/user/player/MarisaOption.java src/user/player/MarisaPlayer.java src/user/player/Option.java src/user/player/Player.java src/user/player/PlayerFactory.java src/user/player/PlayerType.java src/user/player/PlayerWithImage.java src/user/player/ReimuOption.java src/user/player/ReimuPlayer.java
javac -d bin -cp "bin" -encoding UTF-8 src/user/stage/AdvancedStageGroup.java src/user/stage/BeginnerStageGroup.java src/user/stage/ExpertStageGroup.java src/user/stage/IntermediateStageGroup.java src/user/stage/SimpleStage.java src/user/stage/Stage.java src/user/stage/StageCompletionCondition.java src/user/stage/StageGroup.java src/user/stage/StageGroupManager.java src/user/stage/StageState.java src/user/stage/WaveBasedStage.java
javac -d bin -cp "bin" -encoding UTF-8 src/user/item/BombUp.java src/user/item/LifeUp.java src/user/item/PowerUp.java src/user/item/ScorePoint.java src/user/item/IItem.java

echo 编译Main类...
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/Main/*.java
javac -d bin -cp "bin" -encoding UTF-8 -sourcepath src src/user/*.java

echo.
echo 编译完成！
echo 正在运行程序...
java -cp "bin" Main.Main

pause

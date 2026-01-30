package stg.game.stage;

import stg.game.ui.GameCanvas;

/**
 * Stage系统测试类
 * @since 2026-01-30
 */
public class StageTest {

    public static void main(String[] args) {
        // 创建一个模拟的GameCanvas
        GameCanvas mockCanvas = new GameCanvas() {
            @Override
            public void initialize() {
            }

            @Override
            public void reset() {
            }

            @Override
            public void update() {
            }

            @Override
            public void render(java.awt.Graphics2D g) {
            }

            @Override
            public void setPlayer(stg.game.player.Player player) {
            }

            @Override
            public stg.game.player.Player getPlayer() {
                return null;
            }

            @Override
            public void addEnemy(stg.game.enemy.Enemy enemy) {
            }

            @Override
            public void removeEnemy(stg.game.enemy.Enemy enemy) {
            }

            @Override
            public void addItem(stg.game.item.Item item) {
            }

            @Override
            public void addBullet(stg.game.bullet.Bullet bullet) {
            }

            @Override
            public void addLaser(stg.game.laser.Laser laser) {
            }

            @Override
            public void setGameStatus(stg.game.ui.GameStatusPanel gameStatus) {
            }

            @Override
            public stg.game.ui.GameStatusPanel getGameStatus() {
                return null;
            }

            @Override
            public void setTitleScreen(stg.game.ui.TitleScreen titleScreen) {
            }

            @Override
            public stg.game.ui.TitleScreen getTitleScreen() {
                return null;
            }

            @Override
            public void showTitleScreen() {
            }

            @Override
            public void hideTitleScreen() {
            }

            @Override
            public void gameOver() {
            }

            @Override
            public void gameClear() {
            }

            @Override
            public void resetGame() {
            }

            @Override
            public void startGame() {
            }

            @Override
            public boolean isGameActive() {
                return true;
            }

            @Override
            public boolean isTitleScreenActive() {
                return false;
            }

            @Override
            public void setGameActive(boolean active) {
            }

            @Override
            public void setTitleScreenActive(boolean active) {
            }

            @Override
            public void setPlayerType(stg.game.player.PlayerType playerType) {
            }

            @Override
            public stg.game.player.PlayerType getPlayerType() {
                return null;
            }

            @Override
            public stg.util.CoordinateSystem getCoordinateSystem() {
                return new stg.util.CoordinateSystem() {
                    @Override
                    public float[] toScreenCoords(float x, float y) {
                        return new float[]{x + getWidth() / 2, getHeight() / 2 - y};
                    }

                    @Override
                    public float[] toWorldCoords(float screenX, float screenY) {
                        return new float[]{screenX - getWidth() / 2, getHeight() / 2 - screenY};
                    }
                };
            }

            @Override
            public void setLevelData(stg.util.LevelData levelData) {
            }

            @Override
            public stg.util.LevelData getLevelData() {
                return null;
            }
        };

        // 创建关卡组
        StageGroup stageGroup = new StageGroup("Test Stage Group", mockCanvas);

        // 添加关卡
        stageGroup.addStage(new SimpleStage(1, "Stage 1", mockCanvas));
        stageGroup.addStage(new SimpleStage(2, "Stage 2", mockCanvas));
        stageGroup.addStage(new SimpleStage(3, "Stage 3", mockCanvas));

        // 开始关卡组
        System.out.println("Starting stage group: " + stageGroup.getGroupName());
        stageGroup.start();

        // 模拟游戏循环
        try {
            for (int i = 0; i < 60; i++) { // 运行60秒
                Thread.sleep(1000);
                stageGroup.update();

                // 检查关卡组是否完成
                if (stageGroup.isCompleted()) {
                    System.out.println("All stages completed!");
                    break;
                }

                // 打印当前关卡状态
                Stage currentStage = stageGroup.getCurrentStage();
                if (currentStage != null) {
                    System.out.println("Current stage: " + currentStage.getStageName() + ", Enemies: " + currentStage.getEnemies().size());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 清理资源
            stageGroup.cleanup();
            System.out.println("Stage group cleaned up.");
        }
    }
}

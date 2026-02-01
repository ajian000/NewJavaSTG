package user.stage;

import stg.game.ui.GameCanvas;
import stg.game.stage.Stage;
import stg.game.enemy.Enemy;
import stg.game.item.Item;
import stg.game.bullet.Bullet;
import stg.game.laser.Laser;

/**
 * Stage系统测试类
 * @since 2026-01-30
 */
public class StageTest {

    public static void main(String[] args) {
        // 创建一个模拟的GameCanvas
        GameCanvas mockCanvas = new GameCanvas() {
            public void initialize() {
            }

            public void reset() {
            }

            public void update() {
            }

            public void render(java.awt.Graphics2D g) {
            }

            public void setPlayer(user.player.Player player) {
            }

            public user.player.Player getPlayer() {
                return null;
            }

            public void addEnemy(Enemy enemy) {
            }

            public void removeEnemy(Enemy enemy) {
            }

            public void addItem(Item item) {
            }

            public void addBullet(Bullet bullet) {
            }

            public void addLaser(Laser laser) {
            }

            public void setGameStatus(stg.game.ui.GameStatusPanel gameStatus) {
            }

            public stg.game.ui.GameStatusPanel getGameStatus() {
                return null;
            }

            public void setTitleScreen(stg.game.ui.TitleScreen titleScreen) {
            }

            public stg.game.ui.TitleScreen getTitleScreen() {
                return null;
            }

            public void showTitleScreen() {
            }

            public void hideTitleScreen() {
            }

            public void gameOver() {
            }

            public void gameClear() {
            }

            public void resetGame() {
            }

            public void startGame() {
            }

            public boolean isGameActive() {
                return true;
            }

            public boolean isTitleScreenActive() {
                return false;
            }

            public void setGameActive(boolean active) {
            }

            public void setTitleScreenActive(boolean active) {
            }

            public void setPlayerType(user.player.PlayerType playerType) {
            }

            public user.player.PlayerType getPlayerType() {
                return null;
            }

            public stg.util.CoordinateSystem getCoordinateSystem() {
                return new stg.util.CoordinateSystem(getWidth(), getHeight()) {
                    public float[] toScreenCoords(float x, float y) {
                        return new float[]{x + getWidth() / 2, getHeight() / 2 - y};
                    }

                    public float[] toWorldCoords(float screenX, float screenY) {
                        return new float[]{screenX - getWidth() / 2, getHeight() / 2 - screenY};
                    }
                };
            }


        };

        // 创建关卡组
        StageGroup stageGroup = new StageGroup("Test Stage Group", "Test stage group description", StageGroup.Difficulty.NORMAL, mockCanvas);

        // 添加关卡
        SimpleStage stage1 = new SimpleStage(1, "Stage 1", mockCanvas);
        SimpleStage stage2 = new SimpleStage(2, "Stage 2", mockCanvas);
        SimpleStage stage3 = new SimpleStage(3, "Stage 3", mockCanvas);
        
        stageGroup.addStage(stage1);
        stageGroup.addStage(stage2);
        stageGroup.addStage(stage3);

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

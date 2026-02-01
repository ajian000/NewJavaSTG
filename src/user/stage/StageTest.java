package user.stage;

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
            public void setPlayer(user.player.Player player) {
            }

            @Override
            public user.player.Player getPlayer() {
                return null;
            }

            @Override
            public void addEnemy(user.enemy.Enemy enemy) {
            }

            @Override
            public void removeEnemy(user.enemy.Enemy enemy) {
            }

            @Override
            public void addItem(user.item.Item item) {
            }

            @Override
            public void addBullet(user.bullet.Bullet bullet) {
            }

            @Override
            public void addLaser(user.laser.Laser laser) {
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
            public void setPlayerType(user.player.PlayerType playerType) {
            }

            @Override
            public user.player.PlayerType getPlayerType() {
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


        };

        // 创建关卡组
        StageGroup stageGroup = new StageGroup("Test Stage Group", mockCanvas);

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

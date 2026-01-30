package stg.util;

import java.awt.image.BufferedImage;

/**
 * 资源加载示例 - 演示如何使用ResourceManager和AudioManager
 * @Time 2026-01-24
 */
public class ResourceLoaderExample {
	
	public static void main(String[] args) {
		ResourceManager resourceManager = ResourceManager.getInstance();
		AudioManager audioManager = AudioManager.getInstance();
		
		System.out.println("=== 资源加载示例 ===\n");
		
		System.out.println("1. 加载图片资源");
		BufferedImage playerImage = resourceManager.loadImage("player.png");
		BufferedImage enemyImage = resourceManager.loadImage("enemy.png", "images");
		
		if (playerImage != null) {
			System.out.println("   ✓ 玩家图片加载成功: " + playerImage.getWidth() + "x" + playerImage.getHeight());
		}
		if (enemyImage != null) {
			System.out.println("   ✓ 敌人图片加载成功: " + enemyImage.getWidth() + "x" + enemyImage.getHeight());
		}
		
		System.out.println("\n2. 加载背景音乐");
		audioManager.playMusic("bgm_stage1.wav", true);
		if (audioManager.isMusicPlaying()) {
			System.out.println("   ✓ 背景音乐播放中");
		}
		
		System.out.println("\n3. 播放音效");
		audioManager.playSoundEffect("shoot.wav");
		audioManager.playSoundEffect("explosion.wav");
		System.out.println("   ✓ 音效播放完成");
		
		System.out.println("\n4. 调整音量");
		audioManager.setMusicVolume(0.5f);
		audioManager.setSoundVolume(0.8f);
		System.out.println("   ✓ 音乐音量: " + audioManager.getMusicVolume());
		System.out.println("   ✓ 音效音量: " + audioManager.getSoundVolume());
		
		System.out.println("\n5. 暂停/恢复音乐");
		audioManager.pauseMusic();
		System.out.println("   ✓ 音乐已暂停");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		audioManager.resumeMusic();
		System.out.println("   ✓ 音乐已恢复");
		
		System.out.println("\n6. 停止音乐");
		audioManager.stopMusic();
		System.out.println("   ✓ 音乐已停止");
		
		System.out.println("\n=== 示例完成 ===");
	}
}

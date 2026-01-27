package stg.util;

/**
 * OGG 音频播放测试程序
 * @Time 2026-01-24
 */
public class OGGAudioTest {
	public static void main(String[] args) {
		System.out.println("=== OGG 音频播放测试 ===\n");
		
		AudioManager audioManager = AudioManager.getInstance();
		
		System.out.println("1. 检查 OGG 支持");
		if (OGGAudioSupport.supportsOGG()) {
			System.out.println("   ✓ OGG 格式已支持");
		} else {
			System.out.println("   ✗ OGG 格式未支持");
			System.out.println("   提示：请先运行 compile.bat 编译 JOrbis 库");
			return;
		}
		
		System.out.println("\n2. 设置音量");
		audioManager.setMusicVolume(0.7f);
		audioManager.setSoundVolume(0.8f);
		System.out.println("   音乐音量: " + audioManager.getMusicVolume());
		System.out.println("   音效音量: " + audioManager.getSoundVolume());
		
		System.out.println("\n3. 测试音频播放");
		System.out.println("   提示：将 OGG 文件放在 resources/audio/ 目录下");
		
		System.out.println("\n4. 播放背景音乐");
		System.out.println("   文件路径: resources/audio/music/");
		System.out.println("   示例文件: bgm_stage1.ogg, player_score.ogg, spellcard.ogg");
		
		System.out.println("\n5. 播放音效");
		System.out.println("   文件路径: resources/audio/sfx/");
		
		System.out.println("\n6. 使用示例代码");
		System.out.println("   ```java");
		System.out.println("   // 播放背景音乐");
		System.out.println("   OGGAudioSupport.playOGGMusic(");
		System.out.println("       audioManager,");
		System.out.println("       \"resources/audio/\",");
		System.out.println("       \"bgm_stage1.ogg\",");
		System.out.println("       true,");
		System.out.println("       0.7f");
		System.out.println("   );");
		System.out.println();
		System.out.println("   // 播放音效");
		System.out.println("   OGGAudioSupport.playOGGSoundEffect(");
		System.out.println("       audioManager,");
		System.out.println("       \"resources/audio/\",");
		System.out.println("       \"shoot.ogg\",");
		System.out.println("       0.8f");
		System.out.println("   );");
		System.out.println("   ```");
		
		System.out.println("\n=== 测试完成 ===");
		System.out.println("\n提示：");
		System.out.println("- 将 OGG 文件放在对应的 resources/audio/ 目录下");
		System.out.println("- 使用 playOGGMusic() 播放背景音乐");
		System.out.println("- 使用 playOGGSoundEffect() 播放音效");
		System.out.println("- 原生支持的格式：WAV, AU, AIFF");
		System.out.println("- OGG 格式需要 JOrbis 库支持");
		System.out.println("\n当前可用的 OGG 文件：");
		
		java.io.File musicDir = new java.io.File("resources/audio/music");
		if (musicDir.exists()) {
			java.io.File[] oggFiles = musicDir.listFiles((dir, name) -> name.endsWith(".ogg"));
			if (oggFiles != null && oggFiles.length > 0) {
				for (java.io.File file : oggFiles) {
					System.out.println("  - " + file.getName());
				}
			} else {
				System.out.println("  (无 OGG 文件)");
			}
		} else {
			System.out.println("  (目录不存在)");
		}
	}
}

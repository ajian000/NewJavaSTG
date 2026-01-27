package stg.util;

/**
 * OGG 音频支持测试程序
 * @Time 2026-01-24
 */
public class OGGSupportTest {
	public static void main(String[] args) {
		System.out.println("=== OGG 音频支持测试 ===\n");
		
		System.out.println("1. 检查 OGG 支持");
		if (OGGAudioSupport.supportsOGG()) {
			System.out.println("   ✓ OGG 格式已支持");
		} else {
			System.out.println("   ✗ OGG 格式未支持");
			System.out.println("   提示：请安装 JOrbis 库");
			System.out.println("   下载：https://github.com/jcraft/jorbis/releases");
		}
		
		System.out.println("\n2. 支持的音频格式");
		String[] formats = OGGAudioSupport.getSupportedFormats();
		for (String format : formats) {
			System.out.println("   - " + format);
		}
		
		System.out.println("\n3. OGG 音频功能");
		System.out.println("   - loadOGG(resourcePath, filename)       加载 OGG 文件");
		System.out.println("   - playOGGMusic(audioManager, ...)      播放 OGG 背景音乐");
		System.out.println("   - playOGGSoundEffect(audioManager, ...) 播放 OGG 音效");
		
		System.out.println("\n4. 文件路径");
		System.out.println("   音乐文件: resources/audio/music/");
		System.out.println("   音效文件: resources/audio/sfx/");
		
		System.out.println("\n5. 使用示例");
		System.out.println("   AudioManager audioManager = AudioManager.getInstance();");
		System.out.println("   OGGAudioSupport.playOGGMusic(audioManager, \"resources/audio/\", \"bgm_stage1.ogg\", true, 0.7f);");
		System.out.println("   OGGAudioSupport.playOGGSoundEffect(audioManager, \"resources/audio/\", \"shoot.ogg\", 0.8f);");
		
		System.out.println("\n=== 测试完成 ===");
		System.out.println("\n提示：");
		System.out.println("- 将 OGG 文件放在对应的 resources/audio/ 目录下");
		System.out.println("- 使用 playOGGMusic() 播放背景音乐");
		System.out.println("- 使用 playOGGSoundEffect() 播放音效");
		System.out.println("- 原生支持的格式：WAV, AU, AIFF");
		System.out.println("- OGG 格式需要 JOrbis 库支持");
		System.out.println("- 如果未安装 JOrbis，将自动使用 WAV 格式");
	}
}

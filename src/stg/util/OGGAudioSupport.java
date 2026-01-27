package stg.util;

import java.io.*;
import javax.sound.sampled.*;

/**
 * OGG 音频支持工具类
 * @Time 2026-01-24 添加 OGG Vorbis 支持
 */
public class OGGAudioSupport {
	
	/**
	 * 检查是否支持 OGG 格式
	 * @return 是否支持 OGG
	 */
	public static boolean supportsOGG() {
		try {
			Class.forName("com.jcraft.jogg.Packet");
			Class.forName("com.jcraft.jorbis.VorbisFile");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * 加载 OGG 格式的音频文件
	 * @param resourcePath 资源路径
	 * @param filename OGG 文件名
	 * @return 音频输入流
	 */
	public static AudioInputStream loadOGG(String resourcePath, String filename) {
		if (!supportsOGG()) {
			System.err.println("【音频加载失败】OGG 支持库未安装");
			return null;
		}
		
		try {
			File file = new File(resourcePath + filename);
			if (!file.exists()) {
				System.err.println("【音频加载失败】文件不存在: " + file.getAbsolutePath());
				return null;
			}
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			System.out.println("【资源】OGG 音频加载成功: " + filename);
			return audioStream;
			
		} catch (Exception e) {
			System.err.println("【音频加载失败】无法加载 OGG: " + filename);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 播放 OGG 格式的背景音乐
	 * @param audioManager 音频管理器
	 * @param resourcePath 资源路径
	 * @param filename OGG 文件名
	 * @param loop 是否循环播放
	 * @param volume 音量（0.0 - 1.0）
	 */
	public static void playOGGMusic(AudioManager audioManager, String resourcePath, String filename, boolean loop, float volume) throws Exception {
		if (!supportsOGG()) {
			throw new Exception("OGG 支持库未安装");
		}
		
		audioManager.stopMusic();
		
		File file = new File(resourcePath + "music/" + filename);
		if (!file.exists()) {
			throw new Exception("文件不存在: " + file.getAbsolutePath());
		}
		
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		AudioFormat format = audioStream.getFormat();
		
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(audioStream);
		
		if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		}
		
		if (loop) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.start();
		}
		
		System.out.println("【音频】OGG 音乐播放: " + filename);
	}
	
	/**
	 * 播放 OGG 格式的音效
	 * @param audioManager 音频管理器
	 * @param resourcePath 资源路径
	 * @param filename OGG 文件名
	 * @param volume 音量（0.0 - 1.0）
	 */
	public static void playOGGSoundEffect(AudioManager audioManager, String resourcePath, String filename, float volume) {
		if (!supportsOGG()) {
			System.err.println("【音频播放失败】OGG 支持库未安装");
			return;
		}
		
		try {
			File file = new File(resourcePath + "sfx/" + filename);
			if (!file.exists()) {
				System.err.println("【音频播放失败】文件不存在: " + file.getAbsolutePath());
				return;
			}
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = audioStream.getFormat();
			
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(audioStream);
			
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
				gainControl.setValue(dB);
			}
			
			clip.setFramePosition(0);
			clip.start();
			
			System.out.println("【音频】OGG 音效播放: " + filename);
			
		} catch (Exception e) {
			System.err.println("【音频播放失败】无法播放 OGG: " + filename);
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取支持的音频格式列表
	 * @return 格式列表
	 */
	public static String[] getSupportedFormats() {
		String[] formats = new String[4];
		formats[0] = "WAV (原生支持)";
		formats[1] = "AU (原生支持)";
		formats[2] = "AIFF (原生支持)";
		formats[3] = "OGG " + (supportsOGG() ? "(已支持)" : "(需要 JOrbis 库)");
		return formats;
	}
}

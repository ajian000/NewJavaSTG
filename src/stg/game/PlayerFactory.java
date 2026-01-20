package stg.game;

/**
 * 自机工厂类
 * 负责根据类型创建不同的自机实例
 */
public class PlayerFactory {
	private static PlayerFactory instance;

	private PlayerFactory() {
	}

	/**
	 * 获取工厂单例
	 */
	public static PlayerFactory getInstance() {
		if (instance == null) {
			instance = new PlayerFactory();
		}
		return instance;
	}

	/**
	 * 根据类型创建自机
	 * @param type 自机类型
	 * @param spawnX 初始X坐标
	 * @param spawnY 初始Y坐标
	 * @return 自机实例
	 */
	public Player createPlayer(PlayerType type, float spawnX, float spawnY) {
		switch (type) {
			case REIMU:
				return createReimuPlayer(spawnX, spawnY);
			case MARISA:
				return createMarisaPlayer(spawnX, spawnY);
			case SAKUYA:
				return createSakuyaPlayer(spawnX, spawnY);
			case YOUMU:
				return createYoumuPlayer(spawnX, spawnY);
			case SUIKA:
				return createSuikaPlayer(spawnX, spawnY);
			case DEFAULT:
			default:
				return new Player(spawnX, spawnY);
		}
	}

	/**
	 * 创建灵梦自机
	 */
	private Player createReimuPlayer(float spawnX, float spawnY) {
		return new ReimuPlayer(spawnX, spawnY);
	}

	/**
	 * 创建魔理沙自机
	 */
	private Player createMarisaPlayer(float spawnX, float spawnY) {
		return new MarisaPlayer(spawnX, spawnY);
	}

	/**
	 * 创建咲夜自机
	 */
	private Player createSakuyaPlayer(float spawnX, float spawnY) {
		return new SakuyaPlayer(spawnX, spawnY);
	}

	/**
	 * 创建妖梦自机
	 */
	private Player createYoumuPlayer(float spawnX, float spawnY) {
		return new YoumuPlayer(spawnX, spawnY);
	}

	/**
	 * 创建萃香自机
	 */
	private Player createSuikaPlayer(float spawnX, float spawnY) {
		return new SuikaPlayer(spawnX, spawnY);
	}
}

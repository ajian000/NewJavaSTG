package user.player;

/**
 * 自机工厂�? * 负责根据类型创建不同的自机实�? */
public class PlayerFactory {
	private static final PlayerFactory instance = new PlayerFactory();

	private PlayerFactory() {
	}

	/**
	 * 获取工厂单例
	 */
	public static PlayerFactory getInstance() {
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
	 * 创建魔理沙自�?	 */
	private Player createMarisaPlayer(float spawnX, float spawnY) {
		return new MarisaPlayer(spawnX, spawnY);
	}
}


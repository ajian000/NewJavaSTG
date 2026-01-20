package stg.util.math;

import java.util.Random;

/**
 * 随机数生成器类
 */
public class RandomGenerator {
	private Random random;

	public RandomGenerator() {
		this.random = new Random();
	}

	public RandomGenerator(long seed) {
		this.random = new Random(seed);
	}

	/**
	 * 生成指定类型的随机数
	 * @param valueType 值类型
	 * @param minX 最小X值/最小值/范围（可选）
	 * @param minY 最小Y值/最大值/范围（可选）
	 * @return 根据类型返回不同结果
	 */
	public Object next(String valueType, Float minX, Float minY) {
		switch (valueType.toLowerCase()) {
			case "int":
			case "integer":
				if (minX != null && minY != null) {
					return randomInt(minX.intValue(), minY.intValue());
				} else if (minX != null) {
					return randomInt(minX.intValue());
				} else {
					return random.nextInt();
				}

			case "float":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return random.nextFloat();
				}

			case "double":
				if (minX != null && minY != null) {
					return (double)randomRange(minX, minY);
				} else if (minX != null) {
					return (double)randomFloat(minX);
				} else {
					return random.nextDouble();
				}

			case "long":
				if (minX != null && minY != null) {
					return randomLong(minX.longValue(), minY.longValue());
				} else if (minX != null) {
					return (long)(minX * random.nextDouble());
				} else {
					return random.nextLong();
				}

			case "boolean":
			case "bool":
				return random.nextBoolean();

			case "vector2":
			case "vec2":
			case "velocity":
			case "speed":
				if (minX != null && minY != null) {
					return new Vector2(minX, minY);
				} else if (minX != null) {
					float range = minX;
					return new Vector2(
						randomRange(-range, range),
						randomRange(-range, range)
					);
				} else {
					return new Vector2(
						random.nextFloat() * 100 - 50,
						random.nextFloat() * 100 - 50
					);
				}

			case "angle":
			case "direction":
				// 生成角度（度），minX为最小角度，minY为最大角度
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomRange(-minX, minX);
				} else {
					return randomRange(0, 360);
				}

			case "radians":
				// 生成弧度
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomRange(-minX, minX);
				} else {
					return randomRange(0, (float)(2 * Math.PI));
				}

			case "position":
			case "pos":
			case "coord":
				// 生成位置，minX和minY分别表示X和Y的范围
				if (minX != null && minY != null) {
					return new Vector2(
						randomRange(-minX, minX),
						randomRange(-minY, minY)
					);
				} else if (minX != null) {
					return new Vector2(
						randomRange(-minX, minX),
						randomRange(-minX, minX)
					);
				} else {
					return new Vector2(
						randomRange(-100, 100),
						randomRange(-100, 100)
					);
				}

			case "size":
			case "radius":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomFloat(10);
				}

			case "scale":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(0.5f, 2.0f);
				}

			case "alpha":
			case "opacity":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(0, 1);
				}

			case "color":
			case "rgb":
				return new java.awt.Color(
					random.nextInt(256),
					random.nextInt(256),
					random.nextInt(256)
				);

			case "color_alpha":
			case "rgba":
				return new java.awt.Color(
					random.nextInt(256),
					random.nextInt(256),
					random.nextInt(256),
					random.nextInt(256)
				);

			case "hsl":
				// HSL颜色，minX为色相范围，minY为饱和度/亮度范围
				return java.awt.Color.getHSBColor(
					random.nextFloat(),
					minX != null ? Math.min(1.0, Math.max(0.0, minX / 100.0)) : 0.7f,
					minY != null ? Math.min(1.0, Math.max(0.0, minY / 100.0)) : 0.8f
				);

			case "delay":
			case "cooldown":
			case "wait":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(10, 60);
				}

			case "frame":
			case "tick":
				if (minX != null && minY != null) {
					return randomInt(minX.intValue(), minY.intValue());
				} else if (minX != null) {
					return randomInt(minX.intValue());
				} else {
					return randomInt(30, 120);
				}

			case "health":
			case "hp":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(10, 100);
				}

			case "damage":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(5, 20);
				}

			case "score":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(100, 1000);
				}

			case "bullet_speed":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(5, 15);
				}

			case "bullet_size":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(3, 8);
				}

			case "enemy_type":
				return randomInt(1, 5);

			case "player_type":
				return randomInt(1, 2);

			case "wave":
				return randomInt(1, 4);

			case "boss_phase":
				return randomInt(1, 3);

			case "item_type":
				return randomInt(1, 10);

			case "power":
			case "pow":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(0, 4);
				}

			case "difficulty":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomFloat(minX);
				} else {
					return randomRange(1, 5);
				}

			case "rotation":
			case "spin":
				if (minX != null && minY != null) {
					return randomRange(minX, minY);
				} else if (minX != null) {
					return randomRange(-minX, minX);
				} else {
					return randomRange(-5, 5);
				}

			default:
				throw new IllegalArgumentException("Unknown value type: " + valueType);
		}
	}

	/**
	 * 生成指定类型的随机数（无参数版本）
	 */
	public Object next(String valueType) {
		return next(valueType, null, null);
	}

	/**
	 * 生成指定类型的随机数（单参数版本）
	 */
	public Object next(String valueType, Float value) {
		return next(valueType, value, null);
	}

	/**
	 * 生成随机整数 [min, max]
	 */
	public int randomInt(int min, int max) {
		return min + random.nextInt(max - min + 1);
	}

	/**
	 * 生成随机整数 [0, max]
	 */
	public int randomInt(int max) {
		return random.nextInt(max + 1);
	}

	/**
	 * 生成随机浮点数 [min, max]
	 */
	public float randomRange(float min, float max) {
		return min + random.nextFloat() * (max - min);
	}

	/**
	 * 生成随机浮点数 [0, max]
	 */
	public float randomFloat(float max) {
		return random.nextFloat() * max;
	}

	/**
	 * 生成随机长整数 [min, max]
	 */
	public long randomLong(long min, long max) {
		return min + (long)(random.nextDouble() * (max - min + 1));
	}

	/**
	 * 随机选择数组中的一个元素
	 */
	public <T> T choice(T[] array) {
		if (array == null || array.length == 0) {
			throw new IllegalArgumentException("Array cannot be null or empty");
		}
		return array[random.nextInt(array.length)];
	}

	/**
	 * 设置随机种子
	 */
	public void setSeed(long seed) {
		random.setSeed(seed);
	}

	/**
	 * 获取底层的Random对象
	 */
	public Random getRandom() {
		return random;
	}
}

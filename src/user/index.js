/**
 * 演示关卡脚本 (src/user/index.js)
 * 展示关卡脚本系统的各种功能
 */

function createLevel() {
	// 关卡配置
	var config = {
		name: "Demo Stage - Multi-Phase Attack",
		difficulty: "normal",
		background: "space"
	};
	
	// 敌人波次定义
	var waves = [
		// 第一波 - 基础敌人
		{
			phase: 1,
			enemies: [
				{ type: "BasicEnemy", x: 0, y: 200, speed: 2.0, frame: 120 },
				{ type: "BasicEnemy", x: -100, y: 220, speed: 2.5, frame: 140 },
				{ type: "BasicEnemy", x: 100, y: 220, speed: 2.5, frame: 140 },
				{ type: "BasicEnemy", x: -200, y: 240, speed: 3.0, frame: 160 },
				{ type: "BasicEnemy", x: 200, y: 240, speed: 3.0, frame: 160 }
			]
		},
		// 第二波 - 更快的敌人
		{
			phase: 2,
			enemies: [
				{ type: "BasicEnemy", x: -150, y: 150, speed: 4.0, frame: 420 },
				{ type: "BasicEnemy", x: 0, y: 180, speed: 4.5, frame: 440 },
				{ type: "BasicEnemy", x: 150, y: 150, speed: 4.0, frame: 420 }
			]
		},
		// 第三波 - 密集编队
		{
			phase: 3,
			enemies: []
		}
	];
	
	// 第三波 - 动态生成密集编队
	for (var i = 0; i < 5; i++) {
		waves[2].enemies.push({
			type: "BasicEnemy",
			x: (i - 2) * 60,
			y: 100,
			speed: 3.5,
			frame: 600 + i * 20
		});
	}
	
	// 第四波 - 随机散布
	var wave4 = { phase: 4, enemies: [] };
	for (var i = 0; i < 8; i++) {
		wave4.enemies.push({
			type: "BasicEnemy",
			x: Math.random() * 300 - 150,  // -150到150随机
			y: Math.random() * 100 + 150,  // 150到250随机
			speed: 2.0 + Math.random() * 2.0,  // 2.0到4.0随机速度
			frame: 720 + i * 30
		});
	}
	waves.push(wave4);
	
	// 关卡事件
	var events = [
		{ type: "message", frame: 60, content: "Stage Start!" },
		{ type: "message", frame: 420, content: "Phase 2: Speed Up!" },
		{ type: "message", frame: 600, content: "Phase 3: Formation!" },
		{ type: "message", frame: 720, content: "Phase 4: Scatter!" },
		{ type: "message", frame: 900, content: "Warning: Boss Approaching!" }
	];
	
	// 合并所有波次的敌人生成数据
	var allEnemies = [];
	for (var i = 0; i < waves.length; i++) {
		allEnemies = allEnemies.concat(waves[i].enemies);
	}
	
	// 返回关卡数据
	return {
		name: config.name,
		difficulty: config.difficulty,
		background: config.background,
		waves: waves.length,
		totalEnemies: allEnemies.length,
		enemies: allEnemies,
		events: events,
		metadata: {
			createdBy: "JavaScript Demo",
			version: "1.0"
		}
	};
}

// 执行并返回关卡数据
createLevel();

package user.stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import stg.game.ui.GameCanvas;

/**
 * 关卡组管理器 - 负责管理所有关卡组，包括自动发现和创建实例
 * @since 2026-01-30
 */
public class StageGroupManager {
    private static StageGroupManager instance;
    private List<StageGroup> stageGroups;

    /**
     * 私有构造函�?     */
    private StageGroupManager() {
        stageGroups = new ArrayList<>();
    }

    /**
     * 获取单例实例
     * @return 关卡组管理器实例
     */
    public static StageGroupManager getInstance() {
        if (instance == null) {
            instance = new StageGroupManager();
        }
        return instance;
    }

    /**
     * 初始化关卡组
     * @param gameCanvas 游戏画布引用
     */
    public void init(GameCanvas gameCanvas) {
        stageGroups.clear();
        
        // 自动发现并创建关卡组实例
        discoverStageGroups(gameCanvas);
        
        // 如果没有自动发现到关卡组，添加默认关卡组
        if (stageGroups.isEmpty()) {
            addDefaultStageGroups(gameCanvas);
        }
    }

    /**
     * 自动发现所有StageGroup的子类并创建实例
     * @param gameCanvas 游戏画布引用
     */
    private void discoverStageGroups(GameCanvas gameCanvas) {
        try {
            // 获取当前包名
            String packageName = StageGroup.class.getPackage().getName();
            String packagePath = packageName.replace('.', '/');
            
            // 获取包下的所有类文件
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                
                if (directory.exists()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile() && file.getName().endsWith(".class")) {
                                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                                try {
                                    Class<?> clazz = Class.forName(className);
                                    // 检查是否是StageGroup的子类且不是抽象�?                                    if (StageGroup.class.isAssignableFrom(clazz) && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()) && clazz != StageGroup.class) {
                                        // 尝试创建实例
                                        try {
                                            StageGroup stageGroup = (StageGroup) clazz.getConstructor(GameCanvas.class).newInstance(gameCanvas);
                                            stageGroups.add(stageGroup);
                                            System.out.println("自动发现关卡�? " + stageGroup.getDisplayName());
                                        } catch (Exception e) {
                                            System.out.println("创建关卡组实例失�? " + className);
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (ClassNotFoundException e) {
                                    System.out.println("加载类失�? " + className);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("自动发现关卡组失�?");
            e.printStackTrace();
        }
    }

    /**
     * 添加默认关卡�?     * @param gameCanvas 游戏画布引用
     */
    private void addDefaultStageGroups(GameCanvas gameCanvas) {
        System.out.println("添加默认关卡�?);
        stageGroups.add(new BeginnerStageGroup(gameCanvas));
        stageGroups.add(new IntermediateStageGroup(gameCanvas));
        stageGroups.add(new AdvancedStageGroup(gameCanvas));
        stageGroups.add(new ExpertStageGroup(gameCanvas));
    }

    /**
     * 获取所有关卡组
     * @return 关卡组列�?     */
    public List<StageGroup> getStageGroups() {
        return stageGroups;
    }

    /**
     * 根据名称获取关卡�?     * @param name 关卡组名�?     * @return 关卡组对象，不存在则返回null
     */
    public StageGroup getStageGroupByName(String name) {
        for (StageGroup group : stageGroups) {
            if (group.getDisplayName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    /**
     * 清理资源
     */
    public void cleanup() {
        for (StageGroup group : stageGroups) {
            group.cleanup();
        }
        stageGroups.clear();
    }
}


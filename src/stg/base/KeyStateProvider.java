package stg.base;

/**
 * 按键状态提供者接口
 * @Time 2026-01-20 支持标题界面的虚拟键盘显示
 */
public interface KeyStateProvider {
	boolean isUpPressed();
	boolean isDownPressed();
	boolean isLeftPressed();
	boolean isRightPressed();
	boolean isZPressed();
}

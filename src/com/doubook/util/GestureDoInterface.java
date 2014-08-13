package com.doubook.util;

/**
 * 手势识别后回调接口定义
 * 
 * @company 山东易网通信息科技有限公司
 * @author 虞贵涛
 * @create at 2012-12-19
 * 
 */
public interface GestureDoInterface {
	/**
	 * 手势指令类型枚举
	 * 
	 * @author 虞贵涛
	 * 
	 */
	public enum GestureType {
		UP(0), // 向上滑动
		DOWN(1), // 向下滑动
		LEFT(2), // 向左滑动
		RIGHT(3), // 向右滑动
		LONG_PRESS(4), // 长按操作
		SINGLE_TAP_CONFIRMED(5), // 单击操作
		SCROLL(6), // 滚动
		NONE(-1); // 初始值，未知手势

		private final int m_type;

		private GestureType(int type) {
			m_type = type;
		}

		public int getGestureType() {
			return m_type;
		}

	};

	/**
	 * 手势处理逻辑接口
	 * 
	 * @param type
	 *            手势的类型 具体枚举值相见GestureType
	 * @param lineLength
	 *            滑屏滑过的长度
	 */
	public abstract void gestureDo(GestureType type, float lineLength);
}

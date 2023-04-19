package com.sxu.baselibrary.commonutils;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ROUND_UNNECESSARY;

/*******************************************************************************
 * 浮点运算工具类
 *
 * @author: Freeman
 *
 * @date: 2020/6/19
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public final class CalculationUtil {

	/**
	 * 浮点数除法默认的小数位数
	 */
	private static final int DEFAULT_DOUBLE_SCALE = 6;

	private CalculationUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 浮点数相加
	 * @param number
	 * @param number2
	 * @return
	 */
	public static double add(double number, double number2) {
		BigDecimal decimal = new BigDecimal(Double.toString(number));
		return decimal.add(new BigDecimal(Double.toString(number2))).doubleValue();
	}

	/**
	 * 浮点数相减
	 * @param number
	 * @param number2
	 * @return
	 */
	public static double sub(double number, double number2) {
		BigDecimal decimal = new BigDecimal(Double.toString(number));
		return decimal.subtract(new BigDecimal(Double.toString(number2))).doubleValue();
	}

	/**
	 * 浮点数相乘
	 * @param number
	 * @param number2
	 * @return
	 */
	public static double mul(double number, double number2) {
		BigDecimal decimal = new BigDecimal(Double.toString(number));
		return decimal.multiply(new BigDecimal(Double.toString(number2))).doubleValue();
	}

	/**
	 * 浮点数除法
	 * @param number
	 * @param number2
	 * @return
	 */
	public static double div(double number, double number2) {
		// 如果除数为0，则返回0
		if (number2 >= 0 && number2 <= 0) {
			return 0;
		}
		return div(number, number2, DEFAULT_DOUBLE_SCALE);
	}

	/**
	 * 浮点数除法
	 * @param number
	 * @param number2
	 * @param scale 需要保留的小数位置（采用四舍五入的方式）
	 * @return
	 */
	public static double div(double number, double number2, int scale) {
		// 如果除数为0，则返回0
		if (number2 >= 0 && number2 <= 0) {
			return 0;
		}
		BigDecimal decimal = new BigDecimal(Double.toString(number));
		return decimal.divide(new BigDecimal(Double.toString(number2)), scale, ROUND_HALF_UP).doubleValue();
	}
}

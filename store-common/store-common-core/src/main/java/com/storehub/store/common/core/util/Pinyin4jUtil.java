package com.storehub.store.common.core.util;

import lombok.experimental.UtilityClass;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@UtilityClass
public class Pinyin4jUtil {
	public String convertToPinyin(String chinese, HanyuPinyinCaseType caseType) {
		StringBuilder sb = new StringBuilder();
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		//设置输出大写字母
		format.setCaseType(caseType);
		//不带声调
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		char[] charArray = chinese.toCharArray();
		for (char ch : charArray) {
			try {
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, format);
				if (pinyinArray != null && pinyinArray.length > 0) {
					//多音字只取第一个拼音
					String str = pinyinArray[0].substring(0,1);
					sb.append(str);
				} else {
					//非汉字直接输出
					sb.append(ch);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}
}

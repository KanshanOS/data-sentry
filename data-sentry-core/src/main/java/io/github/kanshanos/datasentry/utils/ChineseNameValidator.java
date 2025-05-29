package io.github.kanshanos.datasentry.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 中文姓名校验工具类
 *
 * @author Kanshan
 * @since 2025/5/29 13:21
 */
@UtilityClass
public class ChineseNameValidator {
    /**
     * 中文汉字
     * 参照维基百科汉字Unicode范围(https://zh.wikipedia.org/wiki/%E6%B1%89%E5%AD%97 页面右侧)
     */
    private static final Pattern PATTERN = Pattern.compile("[\u2E80-\u2EFF\u2F00-\u2FDF\u31C0-\u31EF\u3400-\u4DBF\u4E00-\u9FFF\uF900-\uFAFF\uD840\uDC00-\uD869\uDEDF\uD869\uDF00-\uD86D\uDF3F\uD86D\uDF40-\uD86E\uDC1F\uD86E\uDC20-\uD873\uDEAF\uD87E\uDC00-\uD87E\uDE1F]+");


    // 常见单姓
    private static final Set<String> SINGLE_SURNAME = new HashSet<>(Arrays.asList(
            "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈",
            "韩", "杨", "朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华",
            "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏",
            "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方",
            "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺",
            "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅",
            "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和", "穆",
            "萧", "尹"
    ));
    // 常见复姓）
    private static final Set<String> DOUBLE_SURNAME = new HashSet<>(Arrays.asList(
            "欧阳", "司马", "上官", "诸葛", "闻人", "东方", "尉迟", "公孙", "慕容", "申屠"
    ));

    /**
     * 常用称呼后缀
     */
    private static final Set<String> HONORIFIC_SUFFIX = new HashSet<>(Arrays.asList(
            "先生", "女士"
    ));


    /**
     * 校验中文名字是否合法
     *
     * @param name 中文名字
     * @return 校验结果，合法返回 true，否则返回 false
     */
    public static boolean isChineseName(String name) {
        // 检查输入是否为空
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // 检查名字长度（2-4位）
        if (name.length() < 2 || name.length() > 4) {
            return false;
        }

        // 检查是否为中文字符
        if (!PATTERN.matcher(name).matches()) {
            return false;
        }

        // 检查是否以常用称呼结尾（如先生、女士）
        String honorific = name.substring(name.length() - 2);
        if (HONORIFIC_SUFFIX.contains(honorific)) {
            return false;
        }

        // 检查是否以百家姓开头（支持单字姓和双字姓）
        String singleSurname = name.substring(0, 1);
        String doubleSurname = name.substring(0, 2);
        return SINGLE_SURNAME.contains(singleSurname) || DOUBLE_SURNAME.contains(doubleSurname);
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例（包含单字姓和双字姓）
        String[] testNames = {
                "赵子龙",
                "李小明",
                "张伟",
                "王芳华",
                "欧阳娜",
                "司马懿",
                "诸葛亮",
                "刘洋",
                "赵",
                "赵子龙华",
                "欧阳子龙华",
                "朱先生",
                "赵123",
                "Smith",
                ""
        };

        for (String name : testNames) {
            System.out.println("名字: " + name + " -> 合法: " + isChineseName(name));
        }
    }
}

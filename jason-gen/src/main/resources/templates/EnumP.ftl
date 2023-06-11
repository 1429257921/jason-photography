package ${enumPackage};

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ${tableName}表${comment!}枚举类
 *
 * @author ${author}
 * @since ${date}
 */
@Getter
@AllArgsConstructor
public enum ${enumFileName} implements IEnum<Integer> {
    /**
     * ${commentDesc!}
     **/
    <#list enumValueList as enumValue>
    ${enumValue.enumName}${enumValue?index}(${enumValue.value}, "${enumValue.desc}"),
    </#list>
    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    public static ${enumFileName} getEnum(Integer value) {
        if (value != null) {
            for (${enumFileName} valueEnum : ${enumFileName}.values()) {
                if (valueEnum.getValue().equals(value)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static ${enumFileName} getEnum(String desc) {
        if (StrUtil.isNotBlank(desc)) {
            for (${enumFileName} valueEnum : ${enumFileName}.values()) {
                if (valueEnum.getDesc().equals(desc)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static String getDesc(Integer value) {
        ${enumFileName} enumValue = getEnum(value);
        return enumValue != null ? enumValue.getDesc() : "";
    }

    public static Integer getValue(String desc) {
        ${enumFileName} enumValue = getEnum(desc);
        return enumValue != null ? enumValue.getValue() : null;
    }
}
package ${packageEnum};

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ${tableName}表${enumColumnDefinition.columnCommentBrief!}枚举类
 *
 * @author ${author}
 * @since ${date}
 */
@Getter
@AllArgsConstructor
public enum ${enumClassName} implements IEnum<Integer> {
    /**
     * ${enumColumnDefinition.columnCommentBrief!}
     **/
    <#list enumColumnDefinition.enumDefinitionList as enumValue>
    ${enumValue.enumName}${enumValue?index}(${enumValue.value}, "${enumValue.desc}"),
    </#list>
    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    public static ${enumFileName} getEnum(Integer value) {
        return Arrays.stream(${enumClassName}.values()).filter(valueEnum -> valueEnum.getValue().equals(value)).findFirst().get();
    }

    public static ${enumFileName} getEnum(String desc) {
        return Arrays.stream(${enumClassName}.values()).filter(valueEnum -> valueEnum.getDesc().equals(desc)).findFirst().get();
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
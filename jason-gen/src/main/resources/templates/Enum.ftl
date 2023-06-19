package ${packageEnum};

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * ${enumColumnDefinition.columnCommentBrief!}枚举类
 *
 * @author ${author}
 * @since ${date}
 */
@Getter
@AllArgsConstructor
@SuppressWarnings("all")
public enum ${enumColumnDefinition.javaTypeName} implements IEnum<Integer> {
    /**
     * ${enumColumnDefinition.columnCommentBrief!}
     **/
    <#list enumColumnDefinition.enumDefinitionList as enumValue>
    ${enumValue.enumName}(${enumValue.value}, "${enumValue.desc}"),
    </#list>
    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    public static ${enumColumnDefinition.javaTypeName} getEnum(Integer value) {
        return Arrays.stream(${enumColumnDefinition.javaTypeName}.values()).filter(valueEnum -> valueEnum.getValue().equals(value)).findFirst().orElse(null);
    }

    public static ${enumColumnDefinition.javaTypeName} getEnum(String desc) {
        return Arrays.stream(${enumColumnDefinition.javaTypeName}.values()).filter(valueEnum -> valueEnum.getDesc().equals(desc)).findFirst().orElse(null);
    }

    public static String getDesc(Integer value) {
        ${enumColumnDefinition.javaTypeName} enumValue = getEnum(value);
        return enumValue != null ? enumValue.getDesc() : "";
    }

    public static Integer getValue(String desc) {
        ${enumColumnDefinition.javaTypeName} enumValue = getEnum(desc);
        return enumValue != null ? enumValue.getValue() : null;
    }
}
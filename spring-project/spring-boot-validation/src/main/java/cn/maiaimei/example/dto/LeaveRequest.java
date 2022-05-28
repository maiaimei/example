package cn.maiaimei.example.dto;

import cn.maiaimei.example.constant.LeaveType;
import cn.maiaimei.example.validation.group.LeaveSequenceProvider;
import cn.maiaimei.framework.validation.constraints.Date;
import cn.maiaimei.framework.validation.constraints.EnumValid;
import cn.maiaimei.framework.validation.constraints.Time;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import lombok.Data;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@GroupSequenceProvider(value = LeaveSequenceProvider.class)
@Data
public class LeaveRequest {
    @NotBlank(groups = {ValidationGroup.Update.class})
    private Long id;

    @NotNull(groups = {ValidationGroup.OrderA.class})
    @EnumValid(enumType = LeaveType.class)
    private String type;

    @NotBlank(groups = {ValidationGroup.OrderA.class})
    private String summary;

    @NotBlank(groups = {ValidationGroup.OrderA.class})
    private String description;

    @NotNull(groups = {ValidationGroup.OrderA.class})
    @Date(groups = {ValidationGroup.OrderB.class})
    private String startDate;

    @NotNull(groups = {ValidationGroup.OrderA.class})
    @Time(groups = {ValidationGroup.OrderB.class})
    private String startTime;

    @NotNull(groups = {ValidationGroup.OrderA.class})
    @Date(groups = {ValidationGroup.OrderB.class})
    private String endDate;

    @NotNull(groups = {ValidationGroup.OrderA.class})
    @Time(groups = {ValidationGroup.OrderB.class})
    private String endTime;

    @NotNull(groups = {WhenTypeIsInLieuLeave.class})
    private Long workOvertimeId;

    public interface WhenTypeIsInLieuLeave {
    }
}
package cn.maiaimei.example.validation.group;

import cn.maiaimei.example.constant.LeaveType;
import cn.maiaimei.example.dto.LeaveRequest;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class LeaveSequenceProvider implements DefaultGroupSequenceProvider<LeaveRequest> {
    @Override
    public List<Class<?>> getValidationGroups(LeaveRequest leaveRequest) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(LeaveRequest.class);

        if (leaveRequest != null) {
            // 当请假类型为调休假（IN_LIEU_LEAVE）时，加班ID（workOvertimeId不能为NULL）
            if (LeaveType.IN_LIEU_LEAVE.equals(leaveRequest.getType())) {
                defaultGroupSequence.add(LeaveRequest.WhenTypeIsInLieuLeave.class);
            }
        }

        return defaultGroupSequence;
    }
}

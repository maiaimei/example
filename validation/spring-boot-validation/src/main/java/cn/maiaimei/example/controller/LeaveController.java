package cn.maiaimei.example.controller;

import cn.maiaimei.example.dto.LeaveRequest;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Api(tags = "请假管理")
@Slf4j
@Validated // 校验简单类型参数
@RestController
@RequestMapping("/leaves")
public class LeaveController {
    @GetMapping("/pageQuery")
    public void pageQuery(@Min(1) @RequestParam Integer current,
                          @Min(10) @Max(100) @RequestParam Integer size) {
        log.info("current={}, size={}", current, size);
    }

    @GetMapping("/{id}")
    public void get(@PathVariable Long id) {
        log.info("{}", id);
    }

    @PostMapping
    public void create(@Validated(value = {ValidationGroup.CheckInOrder.class}) @RequestBody LeaveRequest request) {
        log.info("{}", request);
    }

    @PutMapping
    public void update(@Validated(value = {ValidationGroup.CheckInOrder.class, ValidationGroup.Update.class}) @RequestBody LeaveRequest request) {
        log.info("{}", request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("{}", id);
    }
}

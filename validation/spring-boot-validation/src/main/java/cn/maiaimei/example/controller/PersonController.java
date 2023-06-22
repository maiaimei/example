package cn.maiaimei.example.controller;

import cn.maiaimei.example.dto.PersonRequest;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Api(tags = "人员管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/persons")
public class PersonController {
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
    public void create(@Validated(value = {ValidationGroup.CheckInOrder.class}) @RequestBody PersonRequest request) {
        log.info("{}", request);
    }

    @PutMapping
    public void update(@Validated(value = {ValidationGroup.CheckInOrder.class, ValidationGroup.Update.class}) @RequestBody PersonRequest request) {
        log.info("{}", request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("{}", id);
    }
}

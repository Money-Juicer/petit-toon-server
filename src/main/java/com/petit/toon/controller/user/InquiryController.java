package com.petit.toon.controller.user;

import com.petit.toon.service.user.InquiryService;
import com.petit.toon.service.user.response.TagExistResponse;
import com.petit.toon.service.user.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/api/v1/user/{inquiryUserId}")
    public ResponseEntity<UserDetailResponse> inquiry(@AuthenticationPrincipal(expression = "user.id") long userId,
                                                      @PathVariable("inquiryUserId") long inquiryUserId) {
        return ResponseEntity.ok(inquiryService.inquiryByUserId(userId, inquiryUserId));
    }

    @PostMapping("/api/v1/user/{tag}")
    public ResponseEntity<TagExistResponse> checkDuplicateTag(@PathVariable("tag") String tag) {
        return ResponseEntity.ok(inquiryService.checkTagExist(tag));
    }

}

package com.petit.toon.controller.cartoon;

import com.petit.toon.controller.cartoon.request.CartoonUpdateRequest;
import com.petit.toon.controller.cartoon.request.CartoonUploadRequest;
import com.petit.toon.service.cartoon.CartoonService;
import com.petit.toon.service.cartoon.response.CartoonDetailResponse;
import com.petit.toon.service.cartoon.response.CartoonListResponse;
import com.petit.toon.service.cartoon.response.CartoonUploadResponse;
import com.petit.toon.service.cartoon.response.ImageInsertResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class CartoonController {

    private final CartoonService cartoonService;

    public CartoonController(CartoonService cartoonService) {
        this.cartoonService = cartoonService;
    }

    @PostMapping("/api/v1/toon")
    public ResponseEntity<CartoonUploadResponse> upload(@AuthenticationPrincipal(expression = "user.id") long userId,
                                                        @Valid @ModelAttribute CartoonUploadRequest cartoonUploadRequest) throws IOException {
        CartoonUploadResponse output = cartoonService.save(userId, cartoonUploadRequest.toInput());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/toon/{toonId}")
    public ResponseEntity<CartoonDetailResponse> getToon(@AuthenticationPrincipal(expression = "user.id") long userId,
                                                         @PathVariable("toonId") long toonId) {
        return ResponseEntity.ok(cartoonService.findOne(userId, toonId));
    }

    @PatchMapping("/api/v1/toon/{toonId}")
    public ResponseEntity<Void> updateToon(@AuthenticationPrincipal(expression = "user.id") long userId,
                                           @Valid @RequestBody CartoonUpdateRequest request,
                                           @PathVariable("toonId") long toonId) {
        cartoonService.updateCartoonInfo(request.toServiceRequest(userId, toonId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/toon/user/{userId}")
    public ResponseEntity<CartoonListResponse> getToons(@PathVariable("userId") long userId,
                                                        @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(cartoonService.findToons(userId, pageable));
    }

    @PostMapping("/api/v1/toon/{toonId}/view")
    public ResponseEntity<Void> view(@AuthenticationPrincipal(expression = "user.id") long userId,
                                     @PathVariable("toonId") long toonId) {
        cartoonService.increaseViewCount(userId, toonId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/toon/{toonId}/image/{index}")
    public ResponseEntity<ImageInsertResponse> insertImage(@AuthenticationPrincipal(expression = "user.id") long userId,
                                                           @PathVariable("toonId") long toonId,
                                                           @PathVariable("index") int index,
                                                           @RequestPart MultipartFile image) {
        ImageInsertResponse response = cartoonService.insertImage(userId, toonId, index, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/api/v1/toon/{toonId}")
    public ResponseEntity<Void> deleteToon(@AuthenticationPrincipal(expression = "user.id") long userId,
                                           @PathVariable("toonId") long toonId) {
        cartoonService.delete(userId, toonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/api/v1/toon/{toonId}/image/{index}")
    public ResponseEntity<Void> removeImage(@AuthenticationPrincipal(expression = "user.id") long userId,
                                            @PathVariable("toonId") long toonId,
                                            @PathVariable("index") int index) {
        cartoonService.removeImage(userId, toonId, index);
        return ResponseEntity.noContent().build();
    }
}

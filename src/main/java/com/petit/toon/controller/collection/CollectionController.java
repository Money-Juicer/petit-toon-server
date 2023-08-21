package com.petit.toon.controller.collection;

import com.petit.toon.controller.collection.request.CollectionRequest;
import com.petit.toon.service.collection.CollectionService;
import com.petit.toon.service.collection.response.BookmarkInfoListResponse;
import com.petit.toon.service.collection.response.BookmarkResponse;
import com.petit.toon.service.collection.response.CollectionInfoListResponse;
import com.petit.toon.service.collection.response.CollectionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    /**
     * Collection Create.
     */
    @PostMapping("/api/v1/collection/create")
    public ResponseEntity<CollectionResponse> createCollection(@AuthenticationPrincipal(expression = "user.id") long userId,
                                                               @RequestBody @Valid CollectionRequest collectionRequest) {
        CollectionResponse response = collectionService.createCollection(userId, collectionRequest.getTitle(), collectionRequest.isClosed());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Collection에 Bookmark 등록.
     */
    @PostMapping("/api/v1/collection/{collectionId}/{cartoonId}")
    public ResponseEntity<BookmarkResponse> createBookmark(@PathVariable("collectionId") long collectionId,
                                                           @PathVariable("cartoonId") long cartoonId) {

        BookmarkResponse response = collectionService.createBookmark(collectionId, cartoonId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 유저의 Collections 조회
     */
    @GetMapping("/api/v1/collection/author/{authorId}")
    public ResponseEntity<CollectionInfoListResponse> listCollection(@AuthenticationPrincipal(expression = "user.id") long userId,
                                                                     @PathVariable("authorId") long authorId) {
        CollectionInfoListResponse response = (userId == authorId) ?
                collectionService.viewCollectionList(authorId, true) :
                collectionService.viewCollectionList(authorId, false);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Collection 단건 및 북마크 전체 조회
     */
    @GetMapping("/api/v1/collection/{collectionId}")
    public ResponseEntity<BookmarkInfoListResponse> listBookmarks(@PathVariable("collectionId") long collectionId,
                                                                  @PageableDefault Pageable pageable) {
        BookmarkInfoListResponse response = collectionService.viewBookmarks(collectionId, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Collection 삭제.
     */

    @DeleteMapping("/api/v1/collection/{collectionId}")
    public ResponseEntity<Void> deleteCollection(@PathVariable("collectionId") long collectionId) {
        collectionService.removeCollection(collectionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Bookmark 삭제
     */
    @DeleteMapping("/api/v1/bookmark/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable("bookmarkId") long bookmarkId) {
        collectionService.removeBookmark(bookmarkId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package com.likelion.rebuild.domain.community.controller;

import com.likelion.rebuild.domain.community.dto.PostRequestDto;
import com.likelion.rebuild.domain.community.dto.PostResponseDto;
import com.likelion.rebuild.domain.community.service.PostService;
import com.likelion.rebuild.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Post API", description = "게시글 CRUD 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "사용자가 제목과 내용을 입력해 게시글을 생성합니다.")
    @PostMapping
    public PostResponseDto create(@RequestBody PostRequestDto dto, @AuthenticationPrincipal User user) {
        return postService.create(dto, user);
    }

    @Operation(summary = "게시물 개별 조회")
    @GetMapping("/list/{id}")
    public PostResponseDto get(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @Operation(summary = "게시글 전체 목록")
    @GetMapping("/list")
    public List<PostResponseDto> all() {
        return postService.getAll();
    }

    @Operation(summary = "게시글 수정", description = "사용자가 게시글을 수정합니다.")
    @PutMapping("/{id}/update")
    public PostResponseDto update(
            @PathVariable Long id,
            @RequestBody PostRequestDto dto,
            @AuthenticationPrincipal User user
    ) {
        return postService.update(id, dto, user);
    }
    
    @Operation(summary = "게시글 삭제", description = "사용자가 게시글을 삭제합니다.")
    @DeleteMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        postService.delete(id, user);
        return "삭제 완료";
    }

}

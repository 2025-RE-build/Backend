package com.likelion.rebuild.domain.community.controller;

import com.likelion.rebuild.domain.community.dto.PostRequestDto;
import com.likelion.rebuild.domain.community.dto.PostResponseDto;
import com.likelion.rebuild.domain.community.service.PostService;
import com.likelion.rebuild.global.security.UserPrincipal;
import com.likelion.rebuild.global.service.S3Uploader;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name = "Post API", description = "게시글 CRUD 관련 API")
public class PostController {

    private final PostService postService;
    private final S3Uploader s3Uploader;
    private final UserRepository userRepository;

    // 1) 게시글 생성
    @Operation(summary = "게시글 생성", description = "사용자가 제목과 내용을 입력해 게시글을 생성합니다.")
    @PostMapping
    public PostResponseDto create(
            @RequestBody PostRequestDto dto,
            @AuthenticationPrincipal UserPrincipal principal   // ★ 수정
    ) {
        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow();

        return postService.create(dto, user);
    }

    // 2) 개별 조회
    @Operation(summary = "글 하나 조회")
    @GetMapping("/list/{id}")
    public PostResponseDto get(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 3) 전체 조회
    @Operation(summary = "전체 글 조회")
    @GetMapping("/list")
    public List<PostResponseDto> all() {
        return postService.getAll();
    }

    // 4) 게시글 수정
    @Operation(summary = "게시글 수정", description = "사용자가 게시글을 수정합니다.")
    @PutMapping("/{id}/update")
    public PostResponseDto update(
            @PathVariable Long id,
            @RequestBody PostRequestDto dto,
            @AuthenticationPrincipal UserPrincipal principal  // ★ 수정
    ) {
        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow();

        return postService.update(id, dto, user);
    }

    // 5) 게시글 삭제
    @Operation(summary = "게시글 삭제", description = "사용자가 게시글을 삭제합니다.")
    @DeleteMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal  // ★ 수정
    ) {
        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow();

        postService.delete(id, user);
        return "삭제 완료";
    }

    // 6) 최신 글 3개
    @Operation(summary = "최신글 3개")
    @GetMapping("/latest")
    public List<PostResponseDto> getLatestPosts() {
        return postService.getLatestPosts();
    }

    // 7) 사진 업로드
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String uploadImage(@RequestPart("file") MultipartFile file) throws IOException {
        return s3Uploader.upload(file, "community");
    }
//내가 쓴 게시물

    @Operation(summary = "내가 쓴 게시물 조회")
    @GetMapping("/my")
    public List<PostResponseDto> myPosts(@AuthenticationPrincipal UserPrincipal principal) {

        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow();

        return postService.getMyPosts(user);
    }
}

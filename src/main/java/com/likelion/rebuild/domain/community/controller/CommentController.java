package com.likelion.rebuild.domain.community.controller;

import com.likelion.rebuild.domain.community.dto.CommentRequestDto;
import com.likelion.rebuild.domain.community.dto.CommentResponseDto;
import com.likelion.rebuild.domain.community.service.CommentService;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import com.likelion.rebuild.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "Comment API", description = "댓글 CRUD 관련 API")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @Operation(summary = "댓글 작성", description = "사용자가 댓글을 작성합니다.")
    @PostMapping("/{postId}")
    public CommentResponseDto create(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto dto,
            @AuthenticationPrincipal UserPrincipal principal   // ★ 수정
    ) {
        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow();

        return commentService.create(postId, dto, user);
    }

    @Operation(summary = "댓글 목록", description = "게시글별 댓글 목록.")
    @GetMapping("/{postId}/list")
    public List<CommentResponseDto> list(@PathVariable Long postId) {
        return commentService.list(postId);
    }

    @Operation(summary = "댓글 수정", description = "사용자가 댓글을 수정합니다.")
    @PutMapping("/{commentId}/update")
    public CommentResponseDto update(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto dto,
            @AuthenticationPrincipal UserPrincipal principal  // ★ 수정
    ) {
        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow();

        return commentService.update(commentId, dto, user);
    }

    @Operation(summary = "댓글 삭제", description = "사용자가 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}/delete")
    public String delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal principal  // ★ 수정
    ) {
        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow();

        commentService.delete(commentId, user);
        return "삭제 완료";
    }
//내가 쓴 댓글들
    @Operation(summary = "내가 쓴 댓글 조회")
    @GetMapping("/my")
    public List<CommentResponseDto> myComments(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow();

        return commentService.getMyComments(user);
    }
}

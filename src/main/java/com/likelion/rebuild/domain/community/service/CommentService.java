package com.likelion.rebuild.domain.community.service;

import com.likelion.rebuild.domain.community.dto.CommentRequestDto;
import com.likelion.rebuild.domain.community.dto.CommentResponseDto;
import com.likelion.rebuild.domain.community.entity.Comment;
import com.likelion.rebuild.domain.community.entity.Post;
import com.likelion.rebuild.domain.community.repository.CommentRepository;
import com.likelion.rebuild.domain.community.repository.PostRepository;
import com.likelion.rebuild.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentResponseDto create(Long postId, CommentRequestDto dto, User user) {
        Post post = postRepository.findById(postId).orElseThrow();

        Comment c = Comment.builder()
                .content(dto.getContent())
                .author(user)
                .post(post)
                .build();

        return new CommentResponseDto(commentRepository.save(c));
    }

    public List<CommentResponseDto> list(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto update(Long commentId, CommentRequestDto dto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        comment.update(dto.getContent()); // 변경

        return new CommentResponseDto(comment); // DB에 자동 반영됨
    }

    public void delete(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
//내가 쓴 댓글들
    public List<CommentResponseDto> getMyComments(User user) {
        return commentRepository.findByAuthorOrderByCreatedAtDesc(user)
                .stream().map(CommentResponseDto::new).toList();
    }
}

package com.likelion.rebuild.domain.community.service;

import com.likelion.rebuild.domain.community.dto.PostRequestDto;
import com.likelion.rebuild.domain.community.dto.PostResponseDto;
import com.likelion.rebuild.domain.community.entity.Post;
import com.likelion.rebuild.domain.community.repository.PostRepository;
import com.likelion.rebuild.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponseDto create(PostRequestDto dto, User user) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(user)
                .build();

        return new PostResponseDto(postRepository.save(post));
    }

    public PostResponseDto getPost(Long id) {
        return new PostResponseDto(
                postRepository.findById(id).orElseThrow()
        );
    }

    public List<PostResponseDto> getAll() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .toList();
    }

    public PostResponseDto update(Long id, PostRequestDto dto, User user) {
        Post post = postRepository.findById(id).orElseThrow();

        // 작성자 본인인지 체크 (선택)
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        post.update(dto.getTitle(), dto.getContent());
        return new PostResponseDto(post);
    }
    public void delete(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow();

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

}

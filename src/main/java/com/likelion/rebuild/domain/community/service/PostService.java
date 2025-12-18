package com.likelion.rebuild.domain.community.service;

import com.likelion.rebuild.domain.community.dto.PostRequestDto;
import com.likelion.rebuild.domain.community.dto.PostResponseDto;
import com.likelion.rebuild.domain.community.entity.Post;
import com.likelion.rebuild.domain.community.repository.PostRepository;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.global.service.S3Uploader;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;   // ⭐ 반드시 필요

    //게시글 생성
    @Transactional
    public PostResponseDto create(PostRequestDto dto, User user) {

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(user)
                .imageUrl(dto.getImageUrl())   // URL만 저장
                .build();

        postRepository.save(post);
        return new PostResponseDto(post);
    }


    //게시물 개별 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        return new PostResponseDto(
                postRepository.findById(id).orElseThrow()
        );
    }


    //전체 목록 최신순 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponseDto::new)
                .toList();
    }


    //게시글 수정
    @Transactional
    public PostResponseDto update(Long id, PostRequestDto dto, User user) {
        Post post = postRepository.findById(id).orElseThrow();

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        post.update(dto.getTitle(), dto.getContent(), dto.getImageUrl());

        return new PostResponseDto(post);
    }



    //게시글 삭제 (S3 이미지까지 삭제)
    @Transactional
    public void delete(Long id, User user) {

        Post post = postRepository.findById(id).orElseThrow();

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        // 이미지 삭제
        if (post.getImageUrl() != null) {
            s3Uploader.deleteFile(post.getImageUrl());
        }

        postRepository.delete(post);
    }


    //최신 글 3개 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getLatestPosts() {
        return postRepository.findTop3ByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponseDto::new)
                .toList();
    }
//내가 쓴 게시물
    public List<PostResponseDto> getMyPosts(User user) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(user)
                .stream().map(PostResponseDto::new).toList();
    }
}

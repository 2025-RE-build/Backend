package com.likelion.rebuild.domain.lock.service;

import com.likelion.rebuild.domain.lock.dto.LockDto;
import com.likelion.rebuild.domain.lock.entity.Lock;
import com.likelion.rebuild.domain.lock.repository.LockRepository;
import com.likelion.rebuild.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockService {

    private final LockRepository lockRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void setLockCode(User user, LockDto request) {

        String rawCode = request.getCode();

        // 3자리 숫자만 허용
        if (!rawCode.matches("\\d{3}")) {
            throw new IllegalArgumentException("3자리 숫자만 입력 가능합니다.");
        }

        String hashed = passwordEncoder.encode(rawCode);

        // 이미 자물쇠가 있으면 업데이트, 없으면 새로 생성
        Lock lock = lockRepository.findByUser(user)
                .orElse(new Lock(user, hashed));

        lock.setHashedCode(hashed);

        lockRepository.save(lock);
    }

    public boolean unlock(User user, LockDto request) {
        String inputCode = request.getCode();

        Lock lock = lockRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("비밀번호가 설정되지 않았습니다."));

        return passwordEncoder.matches(inputCode, lock.getHashedCode());
    }
}

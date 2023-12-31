package com.petit.toon.service.user;

import com.petit.toon.entity.user.User;
import com.petit.toon.exception.notfound.UserNotFoundException;
import com.petit.toon.repository.user.FollowRepository;
import com.petit.toon.repository.user.UserRepository;
import com.petit.toon.service.user.response.TagExistResponse;
import com.petit.toon.service.user.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public UserDetailResponse inquiryByUserId(long userId, long inquiryUserId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(UserNotFoundException::new);
        User inquiryUser = userRepository.findUserById(inquiryUserId)
                .orElseThrow(UserNotFoundException::new);

        boolean isFollow = followRepository.findByFollowerIdAndFolloweeId(user.getId(), inquiryUser.getId()).isPresent();
        long followerCount = followRepository.countByFolloweeId(inquiryUserId); // 나를 팔로우 하는 유저 수
        long followCount = followRepository.countByFollowerId(inquiryUserId); // 내가 팔로우 하는 유저 수
        return UserDetailResponse.of(inquiryUser, isFollow, followerCount, followCount);
    }

    public TagExistResponse checkTagExist(String tag) {
        Optional<User> findUser = userRepository.findByTag(tag);
        return new TagExistResponse(findUser.isPresent());
    }
}

package com.petit.toon.repository.user;

import com.petit.toon.entity.user.Follow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, CustomFollowRepository {
    @Query("select f from Follow f join fetch f.followee where f.follower.id = :followerId")
    List<Follow> findByFollowerId(long followerId, Pageable pageable);

    @Query("select f from Follow f join fetch f.follower where f.followee.id = :followeeId")
    List<Follow> findByFolloweeId(long followeeId, Pageable pageable);

    @Query("select f.follower.id from Follow f where f.followee.id = :followeeId")
    List<Long> findFollowerIdsByFolloweeId(long followeeId);

    Optional<Follow> findByFollowerIdAndFolloweeId(long followerId, long followeeId);

    long countByFollowerId(long followerId);

    long countByFolloweeId(long followeeId);

    void deleteByFollowerIdAndFolloweeId(long followerId, long followeeId);
}

package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.GraduatedResponse;
import kg.mir.Mirproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> getUserById(Long id);

    @Query("SELECT new kg.mir.Mirproject.dto.submittedDto.SubmittedResponse(u.id,u.photoUrl, u.userName) FROM User u WHERE u.userStatus = 'SUBMITTED'")
    List<SubmittedResponse> getAllSubmittedUsers();

    List<User> findByUserName(String userName);

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.GraduatedResponse(u.userName, u.totalSum, u.photoUrl) FROM User u where u.userStatus = 'FINISHED'")
    List<GraduatedResponse> getAllGraduatedUsers();

    Optional<User> findByEmail(String email);

    List<UserWorldResponse> findByTotalSumBetween(int totalSum, int totalSum2);

    @Query("SELECT new kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse(u.userName, u.goal, p.status, p.sum ) FROM User u INNER JOIN u.payments p where u.id = :id")
    Optional<UserWorldProfileResponse> findUserById(Long id);
}
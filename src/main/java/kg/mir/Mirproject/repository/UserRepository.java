package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import kg.mir.Mirproject.dto.WorldUserResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.AllReceivedResponse;
import kg.mir.Mirproject.dto.userDto.UserPaymentResponse;
import kg.mir.Mirproject.dto.userDto.UserStatusResponse;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.UserStatusResponse(u.id,u.photoUrl, u.userName) FROM User u where u.userStatus = 'RECEIVED'")
    List<UserStatusResponse> searchReceivedUser(@Param("query") String query);

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.UserStatusResponse(u.id,u.photoUrl, u.userName) FROM User u where u.userStatus = 'FINISHED'")
    List<UserStatusResponse> searchFinishedUser(@Param("query") String query);

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.UserStatusResponse(u.id,u.photoUrl, u.userName) FROM User u where u.userStatus = 'SUBMITTED'")
    List<UserStatusResponse> searchSubmittedUser(@Param("query") String query);

    @Query("SELECT new kg.mir.Mirproject.dto.WorldDto.UserWorldResponse(u.id,u.photoUrl,u.userName, u.email, u.phoneNumber, u.totalSum) " +
            "FROM User u WHERE u.totalSum BETWEEN :minAmount AND :maxAmount " +
            "AND u.userStatus = 'MIR' " +
            "AND u.role = 'USER' " +
            "ORDER BY u.totalSum ASC")
    List<UserWorldResponse> findByTotalSumRange(@Param("minAmount") int minAmount, @Param("maxAmount") int maxAmount);

    @Query("SELECT new kg.mir.Mirproject.dto.WorldDto.UserWorldResponse(u.id,u.photoUrl,u.userName, u.email, u.phoneNumber, u.totalSum) " +
            "FROM User u WHERE u.userStatus = 'MIR' ")
    List<UserWorldResponse> getAllWorldUsers();


    @Query("SELECT new kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse(u.userName, u.goal, p.status, p.sum ) " +
            "FROM User u INNER JOIN u.payments p WHERE u.id = :id AND u.userStatus = 'MIR'")
    Optional<UserWorldProfileResponse> findUserById(Long id);

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.UserPaymentResponse(p.date, p.sum, p.status) " +
            "FROM Payment p WHERE p.user.id = :id")
    List<UserPaymentResponse> getUsersPaymentById(Long id);

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.AllReceivedResponse(u.id,u.photoUrl, u.userName) FROM User u WHERE u.userStatus = 'RECEIVED'")
    List<AllReceivedResponse> getAllReceivedUsers();

    @Query("select u from User u where u.verificationCode = ?1")
    Optional<User> findByResetToken(String resetToken);

    void deleteAllByUserStatus(UserStatus userStatus);
}
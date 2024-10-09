package kg.mir.Mirproject.repository;

import kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.AllReceivedResponse;
import kg.mir.Mirproject.dto.userDto.GraduatedResponseOne;
import kg.mir.Mirproject.dto.userDto.UserPaymentResponse;
import kg.mir.Mirproject.dto.userDto.UserStatusResponse;
import kg.mir.Mirproject.entities.User;
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

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.UserStatusResponse(u.userName, u.totalSum, u.photoUrl, u.userStatus) FROM User u WHERE u.userName LIKE CONCAT('%', :userName, '%')")
    List<UserStatusResponse> findByUserName(@Param("userName") String userName);

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.GraduatedResponseOne(u.userName, u.totalSum, u.photoUrl) FROM User u where u.userStatus = 'FINISHED'")
    List<GraduatedResponseOne> getAllGraduatedUsers();

    @Query("SELECT new kg.mir.Mirproject.dto.WorldDto.UserWorldResponse(u.userName, u.email, u.phoneNumber, u.totalSum) " +
            "FROM User u WHERE u.totalSum BETWEEN :minAmount AND :maxAmount " +
            "ORDER BY u.totalSum ASC")
    List<UserWorldResponse> findByTotalSumRange(@Param("minAmount") int minAmount, @Param("maxAmount") int maxAmount);

    @Query("SELECT new kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse(u.userName, u.goal, p.status, p.sum ) FROM User u INNER JOIN u.payments p where u.id = :id")
    Optional<UserWorldProfileResponse> findUserById(Long id);

    @Query("SELECT new kg.mir.Mirproject.dto.userDto.UserPaymentResponse(p.date, p.sum, p.status) " +
            "FROM Payment p WHERE p.user.id = :id")
    List<UserPaymentResponse> getUsersPaymentById(Long id);

    @Query("SELECT new kg.mir.Mirproject.dto.submittedDto.SubmittedResponse(u.id,u.photoUrl, u.userName) FROM User u WHERE u.userStatus = 'RECEIVED'")
    List<AllReceivedResponse> getAllReceivedUsers();

    @Query("select u from User u where u.verificationCode = ?1")
    Optional<User> findByResetToken(String resetToken);
}
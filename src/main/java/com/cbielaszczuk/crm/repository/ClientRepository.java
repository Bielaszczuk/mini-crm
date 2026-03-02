package com.cbielaszczuk.crm.repository;
import com.cbielaszczuk.crm.model.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface ClientRepository extends JpaRepository<ClientModel, Long> {
    @Query("SELECT c FROM ClientModel c WHERE c.deletedAt IS NULL")
    List<ClientModel> findAllActive();

    @Query("SELECT c FROM ClientModel c WHERE c.user.id = :userId AND c.deletedAt IS NULL")
    List<ClientModel> findAllActiveByUserId(Long userId);

    @Query("SELECT c FROM ClientModel c WHERE c.id = :id AND c.user.id = :userId AND c.deletedAt IS NULL")
    Optional<ClientModel> findByIdAndUserId(Long id, Long userId);

    Optional<ClientModel> findByEmail(String email);
    boolean existsByEmail(String email);
    List<ClientModel> findByCompanyContainingIgnoreCase(String company);
}

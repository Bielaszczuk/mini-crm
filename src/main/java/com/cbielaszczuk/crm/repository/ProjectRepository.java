package com.cbielaszczuk.crm.repository;
import com.cbielaszczuk.crm.model.ProjectModel;
import com.cbielaszczuk.crm.model.ProjectStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {
    @Query("SELECT p FROM ProjectModel p WHERE p.deletedAt IS NULL")
    List<ProjectModel> findAllActive();

    @Query("SELECT p FROM ProjectModel p WHERE p.client.user.id = :userId AND p.deletedAt IS NULL")
    List<ProjectModel> findAllActiveByUserId(Long userId);

    @Query("SELECT p FROM ProjectModel p WHERE p.id = :id AND p.client.user.id = :userId AND p.deletedAt IS NULL")
    Optional<ProjectModel> findByIdAndUserId(Long id, Long userId);

    List<ProjectModel> findByStatus(ProjectStatusEnum status);
    @Query("SELECT p FROM ProjectModel p WHERE p.client.id = :clientId AND p.deletedAt IS NULL")
    List<ProjectModel> findByClientId(Long clientId);
    @Query("SELECT p FROM ProjectModel p WHERE p.client.id = :clientId")
    List<ProjectModel> findAllByClientId(Long clientId);
    List<ProjectModel> findByTitleContainingIgnoreCase(String title);
}
